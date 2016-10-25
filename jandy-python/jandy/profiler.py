import math
import sys
import threading
import time

from jandy.context import JandyProfilingContext
from jandy.data import ThreadObject


class ThreadContext:
    def __init__(self, threadId, threadName, builder):
        self.nodes = []
        self.context = builder
        self.root = self.latest = self.context.getRootTreeNode()

        self.threadObject = ThreadObject(threadId=threadId, threadName=threadName, rootId=self.root.id)

        self.context.save(self.latest)

    def getThreadObject(self):
        return self.threadObject

    def enter(self, frame):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return False

        # print('---- ENTER')
        n = self.context.getTreeNode(self.latest.id, frame)
        # if self.context.root is None:
        #     self.context.root = n

        n.acc.startTime = time.time()

        self.nodes.append(self.latest)
        self.latest = n
        # print('ENTER - '+str(self.latest))

        return True

    def exit(self, frame, arg, excepted):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return False

        # print('---- EXIT: '+str(frame.f_globals))
        startTime = self.latest.acc.startTime
        elapsedTime = time.time() - startTime

        self.latest.acc.startTime = math.floor(startTime * 1000.0 * 1000.0 * 1000.0)
        self.latest.acc.elapsedTime = math.floor(elapsedTime * 1000.0 * 1000.0 * 1000.0)
        if excepted:
            (exception, value, traceback) = arg
            self.latest.acc.exception = self.context.getExceptionObject(exception, value, traceback)

        # print('EXIT - '+str(self.latest))
        if not excepted:
            self.latest = self.nodes.pop()

        self.context.save(self.latest)

        return True


class Profiler(object):
    def __init__(self, batchSize, URL, id):
        self.context = JandyProfilingContext(batchSize, URL, id)
        self.threadContexts = []
        self.local = threading.local()
        self.ignore = False

    def start(self):
        self.context.start()
        sys.settrace(self.trace)

    def stop(self):
        sys.settrace(None)

    def done(self):
        self.stop()
        self.context.end(self.threadContexts)

    def trace(self, frame, event, arg):
        if event == 'call':
            return self.trace if self.get().enter(frame) else None
        elif event == 'return':
            return self.trace if self.get().exit(frame, arg, False) else None
        elif event == 'exception':
            return self.trace if self.get().exit(frame, arg, True) else None
        return self.trace

    def get(self):
        if hasattr(self.local, 'threadContext') is not True:
            self.local.threadContext = ThreadContext(threading.current_thread().ident,
                                                     threading.current_thread().getName(),
                                                     self.context.getBuilder()
                                                     )
            self.threadContexts.append(self.local.threadContext)
        return self.local.threadContext
