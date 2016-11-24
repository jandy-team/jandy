import math
import sys
import threading
import time

from datetime import datetime

from jandy.context import JandyProfilingContext
from jandy.data import ThreadObject

class ThreadContext:
    def __init__(self, threadId, threadName, builder):
        self.nodes = []
        self.context = builder
        self.root = self.latest = self.context.getRootTreeNode()

        self.threadObject = ThreadObject(threadId=threadId, threadName=threadName, rootId=self.root.id)

        self.context.save(self.latest)
        self.depth = -1

    def getThreadObject(self):
        return self.threadObject

    def enter(self, frame):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return False

        self.nodes.append(self.latest)

        n = self.context.getTreeNode(self.latest.id, frame)
        n.acc.elapsedTime = 0
        n.acc.startTime = time.time()

        self.latest = n

        # self.depth += 1
        # for i in range(self.depth):
        #     print('  ', end='')
        #
        # print('ENTER - %s -- %f' % (str(self.latest), self.latest.acc._start))

        return True

    def exit(self, frame, arg, excepted):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return False

        if self.latest is self.root:
            return True

        # print('---- EXIT: '+str(frame.f_globals))
        startTime = self.latest.acc.startTime
        elapsedTime = self.latest.acc.elapsedTime + time.time() - startTime

        self.latest.acc.elapsedTime = int(round(elapsedTime * 1000 * 1000 * 1000))
        self.latest.acc.startTime = int(round(startTime * 1000 * 1000 * 1000))
        if excepted:
            (exception, value, traceback) = arg
            self.latest.acc.exception = self.context.getExceptionObject(exception, value, traceback)

        # for i in range(self.depth):
        #     print('  ', end='')
        #
        # print('EXIT - %s -- %f / %f' % (str(self.latest), self.latest.acc.elapsedTime, self.latest.acc.startTime))
        # self.depth -= 1

        _time = time.time()
        self.context.save(self.latest)
        _time = time.time() - _time

        # if not excepted:
        self.latest = self.nodes.pop()

        if self.latest.acc is not None:
            self.latest.acc.elapsedTime -= _time

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
