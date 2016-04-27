import uuid
import threading

def treeNode(frame=None, parentId=None):
    n = {
        'id': str(uuid.uuid4()),
        'acc': {},
        'methodId': methodObject(frame=frame)['id'],
        'childrenIds': [],
        'parentId': parentId,
        'root': False
    }

    # nodes.append(n)
    return n


def methodObject(name=None, owner=None, frame=None):
    if frame is not None:
        co = frame.f_code
        return methodObject(co.co_name, classObject(frame=frame))
        pass
    else:
        # if (name, owner['id']) in methods.keys():
        #     return methods[(name, owner['id'])]
        mk = {
            'id': str(uuid.uuid4()),
            'name': name,
            'ownerId': owner.get('id', None)
        }
        # methods[(name, owner['id'])] = mk
        return mk


def classObject(name=None, packageName=None, frame=None):
    if frame is not None:
        _self = frame.f_locals.get('self')
        if _self is not None:
            cls = getattr(_self, '__class__')
            pkg = cls.__module__
            if pkg is None:
                pkg = frame.f_globals['__package__']
            return classObject(name=cls.__name__, packageName=pkg)
        else:
            moduleName = frame.f_globals['__package__'] if '__package__' in frame.f_globals.keys() else ''
            name = frame.f_globals.get('__name__')
            return classObject(name=name, packageName=moduleName)
    else:
        if packageName is None:
            packageName = ""
        # if (name, packageName) in classes.keys():
        #     return classes[(name, packageName)]
        ck = {
            'id': str(uuid.uuid4()),
            'name': name,
            'packageName': packageName
        }
        # classes[(name, packageName)] = ck
        return ck


def exceptionObject(exception, value, traceback):
    #print('name='+exception.__name__+', module='+exception.__module__+', value='+str(value))
    e = {
        'id': str(uuid.uuid4()),
        'message': str(value),
        'classId': classObject(name=exception.__name__, packageName=exception.__module__)['id'],
    }
    # exceptions.append(e)
    return e



threading_lock = threading.Lock()

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
        for item in r['nodes']:
            if item in context['nodes']:
                context['nodes'].remove(item)

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
