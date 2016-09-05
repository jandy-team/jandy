import sys
import threading

from jandy.JandyProfilingContext import JandyProfilingContext
from jandy.ThreadContext import ThreadContext


class Profiler(object):
    def __init__(self, batchSize, URL, id):
        self.context = JandyProfilingContext(batchSize, URL, id)
        self.threadContexts = []
        self.local = threading.local()

    def start(self):
        self.context.start()
        sys.settrace(self.trace)

    def stop(self):
        sys.settrace(None)

    def done(self):
        self.context.end(self.threadContexts)
        self.stop()

    def trace(self, frame, event, arg):
        if event == 'call':
            self.get().enter(frame)
        elif event == 'return':
            self.get().exit(frame, arg, False)
        elif event == 'exception':
            self.get().exit(frame, arg, True)
        return self.trace

    def get(self):
        if hasattr(self.local, 'threadContext') is not True:
            self.local.threadContext = ThreadContext(threading.current_thread().ident,
                                                     threading.current_thread().getName(),
                                                     self.context.getBuilder()
                                                     )
            self.threadContexts.append(self.local.threadContext)
        return self.local.threadContext

