# import os
# import unittest
# import re
# import json
#
# from jandy.profiler import Profiler
#
#
# class Tester1(object):
#     def t1(self):
#         print('t1')
#         try:
#             self.t2()
#         except RuntimeError as e:
#             print(e.message)
#         pass
#
#     def t2(self):
#         print('t2')
#         # raise RuntimeError("test exception")
#
#
# class TestJandyProfiler(unittest.TestCase):
#     def test_profile(self):
#         prof = Profiler()
#         prof.start()
#         try:
#             Tester1().t1()
#         finally:
#             prof.done()
#
#         self.assertNotEqual(os.path.getsize('python-profiler-result.jandy'), 0)
#         with open("python-profiler-result.jandy", "r") as file:
#             context = json.load(file)
#
#         print(context)
#         self.assertIsNotNone(context['rootId'])
#         self.assertEquals(len(context['nodes']), 3)
#
#         self.assertEquals(len([n for n in context['nodes'] if n['parentId'] is not None]), 2)
#
# if __name__ == '__main__':
#     unittest.main()
