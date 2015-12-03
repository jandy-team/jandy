import threading
from jandy.exceptions import JandyClassError
from jandy.ttypes import TreeNode, MethodObject, ClassObject, ExceptionObject, ProfilingContext, Accumulator
import uuid

nodes_lock = threading.Lock()
methods_lock = threading.Lock()
classes_lock = threading.Lock()
exceptions_lock = threading.Lock()

nodes = []
methods = {}
classes = {}
exceptions = []

def treeNode(frame=None):
    n = TreeNode()
    n.id = str(uuid.uuid4())
    n.acc = Accumulator()
    n.methodId = methodObject(frame=frame).id
    n.childrenIds = []

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
            if (name, owner) in methods.keys():
                return methods[(name, owner)]
            mk = MethodObject()
            mk.id = str(uuid.uuid4())
            mk.name = name
            mk.ownerId = owner.id if owner is not None else None
            methods[(name, owner)] = mk
            return mk
        finally:
            methods_lock.release()

def classObject(name=None, packageName=None, frame=None):
    if frame is not None:
        _self = frame.f_locals.get('self')
        if _self is not None:
            cls = getattr(_self, '__class__')
            return classObject(name=cls.__name__, packageName=cls.__module__)
        else:
            # if not '__name__' in frame.f_globals.keys():
            #     print(frame.f_globals)
            moduleName = frame.f_globals['__package__'] if '__package__' in frame.f_globals.keys() else ''
            name = frame.f_globals['__name__']
            return classObject(name=name, packageName=moduleName)
    else:
        if packageName is not None and packageName.startswith('jandy'):
            raise JandyClassError()
        classes_lock.acquire()
        try:
            if (name, packageName) in classes.keys():
                return classes[(name, packageName)]
            ck = ClassObject()
            ck.id = str(uuid.uuid4())
            ck.name = name
            ck.packageName = packageName

            classes[(name, packageName)] = ck
            return ck
        finally:
            classes_lock.release()

def exceptionObject(exception, value, traceback):
    #print('name='+exception.__name__+', module='+exception.__module__+', value='+str(value))
    e = ExceptionObject()
    e.id = str(uuid.uuid4())
    e.message = str(value)
    e.classId = classObject(name=exception.__name__, packageName=exception.__module__).id
    exceptions_lock.acquire()
    try:
        exceptions.append(e)
    finally:
        exceptions_lock.release()
    return e

def profilingContext(roots):
    context = ProfilingContext()
    context.nodes = nodes
    context.methods = methods.values()
    context.classes = classes.values()
    context.exceptions = exceptions

    context.root = TreeNode(id=str(uuid.uuid4()), childrenIds=[])

    for n in roots:
        if n.childrenIds is not None and len(n.childrenIds) > 0:
            context.root.childrenIds.append(n.childrenIds[0])
    return context
