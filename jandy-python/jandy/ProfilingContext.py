from jandy.Serializer import Serializer


class ProfilingContext(Serializer):
    def __init__(self):
        self.profId = None
        self.threadObjects = None

    def getProfId(self):
        return self.profId

    def setProfId(self, profId):
        self.profId = profId
        return self

    def getThreadObjects(self):
        return self.threadObjects

    def setThreadObjects(self, threadObjects):
        self.threadObjects = threadObjects
        return self

    def toDict(self):
        return {
            'profId': self.profId,
            'threadObjects': self.threadObjects
        }