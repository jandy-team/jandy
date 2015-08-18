import os
import subprocess
import sys
import urllib2

import yaml
from poster.encode import multipart_encode
from poster.streaminghttp import register_openers

travis = os.getenv('TRAVIS')
ci = os.getenv('CI')

if travis == 'true' and ci == 'true':
    URL = "http://jandy.io"
else:
    URL = "http://localhost:3000"
JRAT_JAR_URL = URL+"/jandy-java-profiler.jar"

ownerName, repoName = os.getenv('TRAVIS_REPO_SLUG').split('/')
branchName = os.getenv('TRAVIS_BRANCH')
buildId = os.getenv('TRAVIS_BUILD_ID')
buildNum = os.getenv('TRAVIS_BUILD_NUMBER')

def get_latest_dir(basedir):
    latest_dirname = ''
    for dirname in os.listdir(basedir):
        if latest_dirname == '':
            latest_dirname = dirname
        elif latest_dirname < dirname:
            latest_dirname = dirname
    return latest_dirname

def wget(url, filename):
    with open(filename, 'wb') as dest:
        response = urllib2.urlopen(url)
        dest.write(response.read())
        response.close()

register_openers()
with open(".travis.yml", "r") as stream:
    doc = yaml.load(stream)
    if doc['language'] == 'java':
        wget(JRAT_JAR_URL, 'jandy-java-profiler.jar')
        args = sys.argv[1:]
        args.insert(1, '-javaagent:jandy-java-profiler.jar')
        subprocess.call(args)
        with open('java-profiler-result.jandy', 'rb') as jrat_results:
            params = {
                'results': jrat_results,
                'name': 'java-profiler-result.jandy',
                'ownerName': ownerName,
                'repoName': repoName,
                'branchName': branchName,
                'buildId': buildId,
                'buildNum': buildNum
            }
            datagen, headers = multipart_encode(params)
            request = urllib2.Request(URL+"/rest/travis/java", datagen, headers)
            response = urllib2.urlopen(request)
            print response.read()
