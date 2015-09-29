import sys
from thrift.transport.TTransport import TTransportBase


class TFileObjectTransport(TTransportBase):
    """Wraps a file-like object to make it work as a Thrift transport."""

    def __init__(self, fileobj):
        self.fileobj = fileobj

    def isOpen(self):
        return True

    def close(self):
        self.fileobj.close()

    def read(self, sz):
        return self.fileobj.read(sz)

    def write(self, buf):
        if sys.version_info[0] is 3:
            if type(buf) is str:
                self.fileobj.write(bytes(buf, "UTF-8"))
            else:
                self.fileobj.write(buf)
        elif sys.version_info[0] is 2:
            self.fileobj.write(buf)
        else:
            raise RuntimeError('Unsupported python version')

    def flush(self):
        self.fileobj.flush()
