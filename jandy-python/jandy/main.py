import os
import sys
from jandy.profiler import Profiler
import six
import time

def usage():
    print("# python -m jandy <script file>")

def main():
    if len(sys.argv) < 1:
        usage()
        sys.exit(2)

    sys.argv[:] = sys.argv[1:]
    progname = sys.argv[0]
    with open(progname, "rb") as f:
        sys.path.insert(0, os.path.dirname(progname))
        code = compile(f.read(), progname, "exec")
        pr = Profiler()

        start = time.time()
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
            print("profiling time", time.time() - start)
            pr.done()
