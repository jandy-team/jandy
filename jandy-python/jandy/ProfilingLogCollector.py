import io
import os
import json
import tempfile
import threading
import time
import re

import math
import requests
from contextlib import closing
from jandy.ProfilingContext import ProfilingContext
from jandy.Serializer import Serializer


class ProfilingLogCollector(Serializer):
    def __init__(self, baseUrl, batchSize):
        self.baseUrl = baseUrl+'/rest/travis'
        self.batchSize = self.toBytes(batchSize)
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
        self.batchFile.delete()

    def update(self, node=None):
        with self._lock:
            if node:
                nodeDict = self.serialize(node)
                data = (self.toJson(nodeDict)+"\n").encode('utf-8')

                batchFileLength = os.stat(self.batchFile.name).st_size
                if batchFileLength + len(data) >= self.batchSize:
                    self.update()

                if self.bos is None:
                    self.bos = io.FileIO(self.batchFile.name, mode='w')
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

                with open(self.batchFile.name, 'r') as f:
                    requests.put(self.baseUrl, headers=headers, data=f)


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

