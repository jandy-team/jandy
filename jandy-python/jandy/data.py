import json
import uuid
from abc import ABCMeta, abstractmethod


class Serializable:
    def __init__(self):
        pass

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)


class ThreadObject(Serializable):
    def __init__(self, threadId=None, threadName=None, rootId=None):
        self.threadId = threadId
        self.threadName = threadName
        self.rootId = rootId


class ProfilingContext(Serializable):
    def __init__(self, profId, threadObjects=[]):
        self.profId = profId
        self.threadObjects = threadObjects


class ClassObject(Serializable):
    def __init__(self, name, packageName):
        Serializable.__init__(self)
        self.name = name
        self.packageName = packageName


class MethodObject(Serializable):
    def __init__(self, name, owner):
        Serializable.__init__(self)
        self.name = name
        self.owner = owner


class ExceptionObject(Serializable):
    def __init__(self, klass, message, id=str(uuid.uuid4())):
        Serializable.__init__(self)
        self.id = id
        self.klass = klass
        self.message = message


class Accumulator(Serializable):
    def __init__(self, startTime=None, elapsedTime=None, exception=None):
        Serializable.__init__(self)
        self.startTime = startTime
        self.elapsedTime = elapsedTime
        self.exception = exception


class TreeNode(Serializable):
    def __init__(self, parentId, method, acc=Accumulator(), profId=None, id=str(uuid.uuid4()), root=False):
        self.profId = profId
        self.id = id
        self.parentId = parentId
        self.root = root
        self.acc = acc
        self.method = method


class DataObjectBuilder:
    __metaclass__ = ABCMeta

    def __init__(self):
        self.root = None
        self.nodes = []
        self.methods = {}
        self.classes = {}
        self.exceptions = []

    @abstractmethod
    def save(self, node):
        pass

    def getRootTreeNode(self):
        return TreeNode(None, None, acc=None, root=True)

    def getTreeNode(self, parentId, frame):
        return TreeNode(parentId, self.getMethodObject(frame=frame))

    def getMethodObject(self, name=None, owner=None, frame=None):
        if frame is not None:
            co = frame.f_code
            return self.getMethodObject(name=co.co_name, owner=self.getClassObject(frame=frame))
        else:
            return MethodObject(name, owner)

    def getClassObject(self, name=None, packageName=None, frame=None):
        if frame is not None:
            _self = frame.f_locals.get('self')
            if _self is not None:
                cls = getattr(_self, '__class__')
                pkg = cls.__module__
                if pkg is None:
                    pkg = frame.f_globals['__package__']
                return self.getClassObject(name=cls.__name__, packageName=pkg)
            else:
                moduleName = frame.f_globals['__package__'] if '__package__' in frame.f_globals.keys() else ''
                name = frame.f_globals.get('__name__')
                return self.getClassObject(name=name, packageName=moduleName)
        else:
            if packageName is None:
                packageName = ""
            return ClassObject(name, packageName)

    def getExceptionObject(self, exception, value, traceback):
        return ExceptionObject(self.getClassObject(name=exception.__name__, packageName=exception.__module__), str(value))

