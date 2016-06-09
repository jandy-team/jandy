import json
import threading
import traceback
import uuid

import math
import sys

import jandy.factory as factory
import time


class ProfilingThreadContext(object):
    def __init__(self):
        self.root = None
        self.nodes = []
        self.methods = {}
        self.classes = {}
        self.exceptions = []

    def treeNode(self, frame=None, parentId=None):
        n = factory.treeNode(frame=frame, parentId=parentId)
        self.nodes.append(n)
        return n

    def methodObject(self, name=None, owner=None, frame=None):
        mk = factory.methodObject(name=name, owner=owner, frame=frame)
        if (mk['name'], mk['ownerId']) in self.methods.keys():
            return self.methods[(mk['name'], mk['ownerId'])]

        self.methods[(mk['name'], mk['ownerId'])] = mk

    def classObject(self, name=None, packageName=None, frame=None):
        ck = factory.classObject(name=name, packageName=packageName, frame=frame)
        if (ck['name'], ck['packageName']) in self.classes.keys():
            return self.classes[(ck['name'], ck['packageName'])]

        self.classes[(ck['name'], ck['packageName'])] = ck

    def exceptionObject(self, exception, value, traceback):
        e = factory.exceptionObject(exception, value, traceback)
        self.exceptions.append(e)

    def build(self):
        result = {
            'root': self.root,
            'nodes': self.nodes,
            'methods': self.methods,
            'classes': self.classes,
            'exceptions': self.exceptions
        }
        return result


class ThreadHandler(object):
    def __init__(self):
        self.context = ProfilingThreadContext()
        self.nodes = []
        self.current = {'id': None, 'childrenIds': list()}
        # self.root = None

    def enter(self, frame):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return

        # print('---- ENTER')
        n = self.context.treeNode(frame, self.current['id'])
        if self.context.root is None:
            self.context.root = n

        n['acc']['t_startTime'] = time.time()
        n['acc']['concurThreadName'] = threading.current_thread().name
        self.current['childrenIds'].append(n['id'])

        self.nodes.append(self.current)
        self.current = n
        # print('ENTER - '+str(self.current))

    def exit(self, frame, arg, excepted):
        if '__package__' in frame.f_globals.keys() and frame.f_globals['__package__'] == 'jandy':
            return

        # print('---- EXIT: '+str(frame.f_globals))
        startTime = self.current['acc']['t_startTime']
        elapsedTime = time.time() - startTime

        self.current['acc']['startTime'] = math.floor(startTime * 1000.0 * 1000.0 * 1000.0)
        self.current['acc']['elapsedTime'] = math.floor(elapsedTime * 1000.0 * 1000.0 * 1000.0)
        if excepted:
            (exception, value, traceback) = arg
            self.current['acc']['exceptionId'] = factory.exceptionObject(exception, value, traceback)['id']

        # print('EXIT - '+str(self.current))
        if not excepted:
            self.current = self.nodes.pop()


class ThreadHandlerContext(object):
    def __init__(self):
        self.threadHandlers = []
        self.local = threading.local()

    def get(self):
        if hasattr(self.local, 'threadHandler') is not True:
            self.local.threadHandler = ThreadHandler()
            self.threadHandlers.append(self.local.threadHandler)
        return self.local.threadHandler

    def results(self):
        return [t.context.build() for t in self.threadHandlers]


class Profiler(object):
    def __init__(self):
        self.context = ThreadHandlerContext()

    def start(self):
        sys.settrace(self.trace)

    def stop(self):
        sys.settrace(None)

    def done(self):
        self.stop()
        context = factory.profilingContext(self.context.results())
        with open("python-profiler-result.jandy", "wt") as f:
            json.dump(context, f)

    def trace(self, frame, event, arg):
        if event == 'call':
            self.context.get().enter(frame)
        elif event == 'return':
            self.context.get().exit(frame, arg, False)
        elif event == 'exception':
            self.context.get().exit(frame, arg, True)
        return self.trace
