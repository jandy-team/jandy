import os
import unittest
import re
from thrift.protocol.TBinaryProtocol import TBinaryProtocol

from thrift.protocol.TCompactProtocol import TCompactProtocol
from thrift.protocol.TJSONProtocol import TJSONProtocol

from jandy.ttypes import ProfilingContext
from jandy.transport import TFileObjectTransport
from jandy.profiler import Profiler


class Tester1(object):
    def t1(self):
        print('t1')
        try:
            self.t2()
        except RuntimeError as e:
            print(e.message)
        pass

    def t2(self):
        print('t2')
        raise RuntimeError("test exception")


class TestJandyProfiler(unittest.TestCase):
    def test_profile(self):
        prof = Profiler()
        prof.start()
        try:
            Tester1().t1()
        finally:
            prof.done()

        self.assertNotEqual(os.path.getsize('python-profiler-result.jandy'), 0)
        with open("python-profiler-result.jandy", "r") as file:
            trans = TFileObjectTransport(file)
            context = ProfilingContext()
            context.read(TJSONProtocol(trans))

        print(context)
        self.assertIsNotNone(context.root)


if __name__ == '__main__':
    unittest.main()
