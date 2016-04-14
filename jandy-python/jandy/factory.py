import threading
import uuid

nodes_lock = threading.Lock()
methods_lock = threading.Lock()
classes_lock = threading.Lock()
exceptions_lock = threading.Lock()

nodes = []
methods = {}
classes = {}
exceptions = []


def treeNode(frame=None, parentId=None):
    n = {
        'id': str(uuid.uuid4()),
        'acc': {},
        'methodId': methodObject(frame=frame)['id'],
        'childrenIds': [],
        'parentId': parentId,
        'root': False
    }

    nodes_lock.acquire()
    try:
        nodes.append(n)
    finally:
        nodes_lock.release()

    return n


def methodObject(name=None, owner=None, frame=None):
    if frame is not None:
        co = frame.f_code
        return methodObject(co.co_name, classObject(frame=frame))
        pass
    else:
        methods_lock.acquire()
        try:
            if (name, owner['id']) in methods.keys():
                return methods[(name, owner['id'])]
            mk = {
                'id': str(uuid.uuid4()),
                'name': name,
                'ownerId': owner['id'] if owner is not None else None
            }
            methods[(name, owner['id'])] = mk
            return mk
        finally:
            methods_lock.release()


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
        classes_lock.acquire()
        try:
            if (name, packageName) in classes.keys():
                return classes[(name, packageName)]
            ck = {
                'id': str(uuid.uuid4()),
                'name': name,
                'packageName': packageName
            }
            classes[(name, packageName)] = ck
            return ck
        finally:
            classes_lock.release()


def exceptionObject(exception, value, traceback):
    #print('name='+exception.__name__+', module='+exception.__module__+', value='+str(value))
    e = {
        'id': str(uuid.uuid4()),
        'message': str(value),
        'classId': classObject(name=exception.__name__, packageName=exception.__module__)['id'],
    }
    exceptions_lock.acquire()
    try:
        exceptions.append(e)
    finally:
        exceptions_lock.release()
    return e


def profilingContext(roots):
    root = {
        'id': str(uuid.uuid4()),
        'childrenIds': [],
        'parentId': None,
        'root': True
    }
    context = {
        'nodes': [root] + nodes,
        'methods': list(methods.values()),
        'classes': list(classes.values()),
        'exceptions': exceptions,
        'rootId': root['id']
    }

    for n in roots:
        if n is not None:
            root['childrenIds'].append(n['id'])
            n['parentId'] = root['id']
    return context
