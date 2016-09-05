import threading
import time

import math

from jandy.ThreadObject import ThreadObject


class ThreadContext:
    def __init__(self, threadId, threadName, builder):
        self.threadObject = ThreadObject()
        self.nodes = []

        self.threadObject.setThreadId(threadId)
        self.threadObject.setThreadName(threadName)
        self.context = builder

        self.latest = self.context.getRootTreeNode()
        self.root = self.latest
        self.threadObject.setRootId(self.root.get('id'))

        self.context.save(self.latest)

    def getThreadObject(self):
        return self.threadObject

    def enter(self, frame):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return

        # print('---- ENTER')
        n = self.context.treeNode(frame, self.latest['id'])
        # if self.context.root is None:
        #     self.context.root = n

        n['acc']['t_startTime'] = time.time()
        n['acc']['concurThreadName'] = threading.current_thread().name
        self.latest['childrenIds'].append(n['id'])

        self.nodes.append(self.latest)
        self.latest = n
        # print('ENTER - '+str(self.latest))

    def exit(self, frame, arg, excepted):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return

        # print('---- EXIT: '+str(frame.f_globals))
        startTime = self.latest['acc']['t_startTime']
        elapsedTime = time.time() - startTime

        self.latest['acc']['startTime'] = math.floor(startTime * 1000.0 * 1000.0 * 1000.0)
        self.latest['acc']['elapsedTime'] = math.floor(elapsedTime * 1000.0 * 1000.0 * 1000.0)
        if excepted:
            (exception, value, traceback) = arg
            self.latest['acc']['exceptionId'] = self.context.exceptionObject(exception, value, traceback)['id']

        # print('EXIT - '+str(self.latest))
        if not excepted:
            self.latest = self.nodes.pop()

        self.context.save(self.latest)