import os
import subprocess
import sys
import urllib2

import yaml
from poster.encode import multipart_encode
from poster.streaminghttp import register_openers

URL = "http://localhost:3000"
JRAT_XML_URL = URL+"/jrat.xml"
JRAT_JAR_URL = URL+"/jrat.jar"

ownerName, repoName = os.getenv('TRAVIS_REPO_SLUG').split('/')
branchName = os.getenv('TRAVIS_BRANCH')
buildId = os.getenv('TRAVIS_BUILD_ID')

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
        wget(JRAT_JAR_URL, 'jrat.jar')
        wget(JRAT_XML_URL, "jrat.xml")
        args = sys.argv[1:]
        args.insert(1, '-javaagent:jrat.jar')
        subprocess.call(args)
        with open('jrat.output/%s/results.jrat' % get_latest_dir('jrat.output'), 'rb') as jrat_results:
            params = {
                'results': jrat_results,
                'name': 'results.jrat',
                'ownerName': ownerName,
                'repoName': repoName,
                'branchName': branchName,
                'buildId': buildId
            }
            datagen, headers = multipart_encode(params)
            request = urllib2.Request(URL+"/travis/java", datagen, headers)
            response = urllib2.urlopen(request)
            print response.read()
