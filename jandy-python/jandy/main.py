import os
import sys
from jandy.profiler import Profiler
import six


def usage():
    print("# python -m jandy <script file>")


def main():
    batchSize = sys.argv[1]
    baseUrl = sys.argv[2]
    id = sys.argv[3]

    sys.argv = sys.argv[4:]

    progname = sys.argv[0]
    with open(progname, "rb") as f:
        sys.path.insert(0, os.path.dirname(progname))
        code = compile(f.read(), progname, "exec")
        pr = Profiler(batchSize, baseUrl, id)
        pr.start()
        sub_globals = {
            '__file__': progname,
            '__name__': '__main__',
            '__package__': None
        }
        sub_locals = {}
        try:
            six.exec_(code, sub_globals, sub_locals)
        finally:
            pr.done()