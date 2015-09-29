import os
import sys
from jandy.profiler import Profiler


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
        pr.start()
        sub_globals = {
            '__file__': progname,
            '__name__': '__main__',
            '__package__': None
        }
        sub_locals = None
        try:
            exec(code) in sub_globals, sub_locals
        finally:
            pr.done()
