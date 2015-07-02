import httplib

conn = httplib.HTTPConnection('localhost:3000')
conn.connect()

conn.request("HEAD", "/")
res = conn.getresponse()

if res.status == 302 and dict(res.getheaders()).has_key('location'):
    print res.getheader('location')

