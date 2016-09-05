import os

from jandy.ProfilingLogCollector import ProfilingLogCollector
from jandy.DataObjectBuilder import DataObjectBuilder


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
        this = self

        class NewDOB(DataObjectBuilder):
            def save(self, node):
                node['profId'] = this.profId
                this.collector.update(node)

        newDOB = NewDOB()
        return newDOB