import uuid


class ProfilingThreadContext(object):
    def __init__(self):
        self.root = None
        self.nodes = []
        self.methods = {}
        self.classes = {}
        self.exceptions = []

    def treeNode(self, frame=None, parentId=None):
        n = {
            'id': str(uuid.uuid4()),
            'acc': {},
            'methodId': self.methodObject(frame=frame)['id'],
            'childrenIds': [],
            'parentId': parentId,
            'root': False
        }

        self.nodes.append(n)
        return n

    def methodObject(self, name=None, owner=None, frame=None):
        if frame is not None:
            co = frame.f_code
            return self.methodObject(co.co_name, self.classObject(frame=frame))
            pass
        else:
            if (name, owner['id']) in self.methods.keys():
                return self.methods[(name, owner['id'])]
            mk = {
                'id': str(uuid.uuid4()),
                'name': name,
                'ownerId': owner.get('id', None)
            }
            self.methods[(name, owner['id'])] = mk
            return mk

    def classObject(self, name=None, packageName=None, frame=None):
        if frame is not None:
            _self = frame.f_locals.get('self')
            if _self is not None:
                cls = getattr(_self, '__class__')
                pkg = cls.__module__
                if pkg is None:
                    pkg = frame.f_globals['__package__']
                return self.classObject(name=cls.__name__, packageName=pkg)
            else:
                moduleName = frame.f_globals['__package__'] if '__package__' in frame.f_globals.keys() else ''
                name = frame.f_globals.get('__name__')
                return self.classObject(name=name, packageName=moduleName)
        else:
            if packageName is None:
                packageName = ""
            if (name, packageName) in self.classes.keys():
                return self.classes[(name, packageName)]
            ck = {
                'id': str(uuid.uuid4()),
                'name': name,
                'packageName': packageName
            }
            self.classes[(name, packageName)] = ck
            return ck

    def exceptionObject(self, exception, value, traceback):
        e = {
            'id': str(uuid.uuid4()),
            'message': str(value),
            'classId': self.classObject(name=exception.__name__, packageName=exception.__module__)['id'],
        }
        self.exceptions.append(e)
        return e

    def build(self):
        result = {
            'root': self.root,
            'nodes': self.nodes,
            'methods': self.methods,
            'classes': self.classes,
            'exceptions': self.exceptions
        }
        return result


def profilingContext(results):
    root = {
        'id': str(uuid.uuid4()),
        'childrenIds': [],
        'parentId': None,
        'root': True
    }

    context = {
        'nodes': [root],
        'methods': [],
        'classes': [],
        'exceptions': [],
        'rootId': root['id']
    }

    for r in results:
        context['nodes'].extend(r['nodes'])

        removeIfExists(context['methods'], r['methods'])
        context['methods'].extend(list(r['methods'].values()))

        removeIfExists(context['classes'], r['classes'])
        context['classes'].extend(list(r['classes'].values()))

        context['exceptions'].extend(r['exceptions'])

        if r['root'] is not None:
            n = r['root']
            root['childrenIds'].append(n['id'])
            n['parentId'] = root['id']

    return context


def removeIfExists(list1, list2):
    for item in list(list2.values()):
        if item in list1:
            list1.remove(item)
