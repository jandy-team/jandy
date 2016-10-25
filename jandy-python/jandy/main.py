import os
import sys
from jandy.profiler import Profiler
import six

def usage():
    print("# python -m jandy <script file>")

def main():
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('batchSize', help='jandy batch size')
    parser.add_argument('URL', help='jandy base url')
    parser.add_argument('id', help='jandy sample name')
    parser.add_argument('script_arguments', nargs='+', help='.jandy.yml script args')
    args = parser.parse_args()

    if len(sys.argv) < 1:
        usage()
        sys.exit(2)

    sys.argv[:] = args.script_arguments
    progname = sys.argv[0]
    with open(progname, "rb") as f:
        sys.path.insert(0, os.path.dirname(progname))
        code = compile(f.read(), progname, "exec")
        pr = Profiler(args.batchSize, args.URL, args.id)
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