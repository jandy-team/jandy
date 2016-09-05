import io
import os
import json
import tempfile
import threading
import time

import requests
from contextlib import closing
from jandy.ProfilingContext import ProfilingContext
from jandy.Serializer import Serializer


class ProfilingLogCollector(Serializer):
    def __init__(self, baseUrl, batchSize):
        self.baseUrl = baseUrl+'/rest/travis'
        self.batchSize = int(batchSize)
        self.bos = None
        self.batchFile = tempfile.NamedTemporaryFile(prefix="batch", suffix=str(round(time.time() * 1000)), delete=False)
        self._lock = threading.Lock()

    def start(self, sampleName, repoSlug, branchName, buildId, buildNum, lang):
        headers = {
            'Content-Type': 'application/json'
        }

        model = {
            'sampleName': sampleName,
            'buildId': buildId,
        }

        with closing(requests.post(self.baseUrl, headers=headers,
                                   stream=True, data=json.dumps(model).encode('utf-8'))) as conn:
            for line in conn.iter_lines():
                line = line.decode('utf-8')
                line = json.loads(line)
                profId = line.get('profId')
                return profId

    def end(self, profId, threadObjects):
        self.update()

        headers = {
            'Content-Type': 'application/json'
        }

        profilingContext = ProfilingContext()
        profilingContext.setProfId(profId)
        profilingContext.setThreadObjects(threadObjects)

        data = self.serialize(profilingContext)
        data = self.toJson(data).encode('utf-8')

        with closing(requests.post(self.baseUrl+"/"+str(profId), headers=headers, data=data)) as conn:
            pass

        self.batchFile.close()

    def update(self, node=None):
        if node:
            with self._lock:
                nodeDict = self.serialize(node)
                data = (self.toJson(nodeDict)+"\n").encode('utf-8')

                batchFileLength = os.stat(self.batchFile.name).st_size
                if batchFileLength + len(data) >= self.batchSize:
                    self.update()

                if self.bos is None:
                    self.bos = io.BufferedWriter(self.batchFile)
                self.bos.write(data)
                return None
        else:
            if self.bos is None:
                return

            self.bos.close()
            self.bos = None

            headers = {
                'Content-Length': self.batchSize,
                'Content-Type': 'application/json'
            }

            with open(self.batchFile.name, 'rb') as f:
                requests.put(self.baseUrl, headers=headers, data=self.readInChunks(f))

    def readInChunks(self, fileObj, chunkSize=2048):
        while True:
            data = fileObj.read(chunkSize)
            if not data:
                break
            yield data
