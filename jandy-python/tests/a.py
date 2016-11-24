# import re, os
# from jandy.profiler import Profiler
#
#
# class Base:
#     def __init__(self):
#         print('init call')
#
#     def compile(self, str):
#         re.compile(str)
#
# #
# p = Profiler("12K", "localhost:3000", 1)
# try:
#     p.start()
#     b = Base()
#     b.compile("foo|bar")
#     print("Hello World!!\n")
# finally:
#     p.done()
#
#
# #try:
# #    b.print_usage()
# #except e.MyException as e:
# #    raise ValueError('failed')
