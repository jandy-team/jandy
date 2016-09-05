from jandy.Serializer import Serializer


class ThreadObject(Serializer):
    def __init__(self):
        self.threadId = ""
        self.threadName = ""
        self.rootId = ""

    def getThreadName(self):
        return self.threadName

    def setThreadName(self, threadName):
        self.threadName = threadName
        return self

    def getThreadId(self):
        return self.threadId

    def setThreadId(self, threadId):
        self.threadId = threadId
        return self

    def getRootId(self):
        return self.rootId

    def setRootId(self, rootId):
        self.rootId = rootId
        return self

    def toDict(self):
        return {
            'threadId': self.threadId,
            'threadName': self.threadName,
            'rootId': self.rootId
        }