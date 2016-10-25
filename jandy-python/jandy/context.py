import io
import os
import json
import tempfile
import threading
import traceback

import time
import re

import math
from jandy.data import ProfilingContext

import sys

from jandy.data import DataObjectBuilder

if sys.version_info.major >= 3:
    import http.client as http
elif sys.version_info.major >= 2:
    import httplib as http


class ProfilingLogCollector:
    def __init__(self, baseUrl, batchSize):
        self.baseUrl = baseUrl
        self.batchSize = self.toBytes(batchSize)
        self.bos = None
        self.batchFile = tempfile.NamedTemporaryFile(prefix="batch", suffix=str(round(time.time() * 1000)),
                                                     delete=True)
        self._lock = threading.Lock()

    def start(self, sampleName, repoSlug, branchName, buildId, buildNum, lang):
        headers = {
            'Content-Type': 'application/json'
        }

        model = {
            'sampleName': sampleName,
            'buildId': buildId,
        }

        con = http.HTTPConnection(self.baseUrl)
        try:
            con.request('POST', '/rest/travis/prof/create', headers=headers, body=json.dumps(model))
            line = con.getresponse().read()
            line = json.loads(line.decode('utf-8'))
            profId = line.get('profId')
            return profId
        finally:
            con.close()

    def end(self, profId, threadObjects):
        with self._lock:
            self.doUpdate()

        headers = {
            'Content-Type': 'application/json'
        }

        profilingContext = ProfilingContext(profId, threadObjects=threadObjects)

        data = profilingContext.toJSON()

        con = http.HTTPConnection(self.baseUrl)
        try:
            con.request('POST', '/rest/travis/prof/save', headers=headers, body=data)
            con.getresponse().read()
        finally:
            con.close()

        self.batchFile.close()

    def update(self, node):
        with self._lock:
            if node:
                data = (node.toJSON() + "\n").encode('utf-8')

                batchFileLength = os.stat(self.batchFile.name).st_size
                if batchFileLength + len(data) >= self.batchSize:
                    self.doUpdate()

                if self.bos is None:
                    self.bos = io.FileIO(self.batchFile.name, mode='w')
                self.bos.write(data)
                return None

    def doUpdate(self):
        if self.bos is None:
            return

        self.bos.close()
        self.bos = None

        headers = {
            'Content-Type': 'application/json'
        }

        with open(self.batchFile.name, 'r') as f:
            con = http.HTTPConnection(self.baseUrl)
            try:
                con.request('POST', '/rest/travis/prof/put', headers=headers, body=f)
            finally:
                con.close()

    def toBytes(self, str):
        prog = re.compile('([\d.]+)([GMK]B)', re.IGNORECASE)
        result = prog.match(str)
        powerMap = {
            'GB': 3,
            'MB': 2,
            'KB': 1
        }
        if result:
            number = result.group(1)
            pow = powerMap.get(result.group(2).upper())
            bytes = int(number) * math.pow(1024, pow)
            return bytes
        else:
            return -1


class JandyProfilingContext(object):
    def __init__(self, batchSize, URL, id):
        self.profId = None
        self.collector = ProfilingLogCollector(URL, batchSize)
        self.id = id

    def start(self):
        sampleName = self.id
        repoSlug = os.getenv('TRAVIS_REPO_SLUG').split('/')[1]
        buildId = os.getenv('TRAVIS_BUILD_ID')
        buildNum = os.getenv('TRAVIS_BUILD_NUMBER')
        branchName = os.getenv('TRAVIS_BRANCH')
        self.profId = self.collector.start(sampleName, repoSlug, branchName, buildId, buildNum, "python")

    def end(self, threadContexts):
        threadObjects = []
        for tc in threadContexts:
            threadObjects.append(tc.getThreadObject())

        self.collector.end(self.profId, threadObjects)

    def getBuilder(self):
        that = self

        class NewDOB(DataObjectBuilder):
            def save(self, node):
                node.profId = that.profId
                that.collector.update(node)

        newDOB = NewDOB()
        return newDOB