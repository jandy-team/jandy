import threading
import sys
from thrift.protocol.TBinaryProtocol import TBinaryProtocol
from thrift.protocol.TJSONProtocol import TJSONProtocol
from jandy.exceptions import JandyClassError

from jandy.factory import treeNode, profilingContext, exceptionObject
from jandy.transport import TFileObjectTransport
import time
from thrift.protocol.TCompactProtocol import TCompactProtocol
from jandy.ttypes import TreeNode


class MethodHandler(object):
    def __init__(self):
        self.nodes = []
        self.current = TreeNode(childrenIds=[])
        self.root = self.current

    def enter(self, frame):
        try:
            n = treeNode(frame)
        except JandyClassError:
            return

        n.acc.t_startTime = time.time()
        n.acc.concurThreadName = threading.current_thread().name
        self.current.childrenIds.append(n.id)

        self.nodes.append(self.current)
        self.current = n
        # print('ENTER - '+str(self.current))

    def exit(self, frame, arg, excepted):
        startTime = self.current.acc.t_startTime
        elapsedTime = time.time() - startTime

        self.current.acc.startTime = long(startTime * 1000.0 * 1000.0 * 1000.0)
        self.current.acc.elapsedTime = long(elapsedTime * 1000.0 * 1000.0 * 1000.0)
        if excepted:
            (exception, value, traceback) = arg
            self.current.acc.exceptionId = exceptionObject(exception, value, traceback).id

        # print('EXIT - '+str(self.current))
        if not excepted:
            self.current = self.nodes.pop()


class MethodHandlerContext(object):
    def __init__(self):
        self.roots = []
        self.local = threading.local()

    def get(self):
        if hasattr(self.local, 'methodHandler') is not True:
            self.local.methodHandler = MethodHandler()
            self.roots.append(self.local.methodHandler.root)
        return self.local.methodHandler


class Profiler(object):

    def __init__(self):
        self.context = MethodHandlerContext()

    def start(self):
        sys.settrace(self.trace)

    def stop(self):
        sys.settrace(None)

    def done(self):
        self.stop()
        context = profilingContext(self.context.roots)
        # print(context.methods)
        with open("python-profiler-result.jandy", "w") as f:
            tp = TFileObjectTransport(f)
            context.write(TJSONProtocol(tp))
            tp.flush()

    def trace(self, frame, event, arg):
        if event == 'call':
            self.context.get().enter(frame)
        elif event == 'return':
            self.context.get().exit(frame, arg, False)
        elif event == 'exception':
            self.context.get().exit(frame, arg, True)
        return self.trace
