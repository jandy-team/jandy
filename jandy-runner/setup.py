from distutils.dir_util import mkpath
import os
import shutil

from setuptools import setup
from target import pom
import yaml
import poster

mkpath('build')
mkpath('build/lib')
shutil.rmtree('build/lib/yaml', ignore_errors=True)
shutil.copytree(os.path.dirname(os.path.realpath(yaml.__file__)), 'build/lib/yaml')
shutil.rmtree('build/lib/poster', ignore_errors=True)
shutil.copytree(os.path.dirname(os.path.realpath(poster.__file__)), 'build/lib/poster')

setup(
    name=pom.__name__,
    version=pom.__version__,
    packages=[''],
    url='https://github.com/jcooky/jandy/jandy-runner',
    license='GPL',
    author='JCooky',
    author_email='bak723@gmail.com',
    description='Jandy Runner',
    requires=['PyYAML', 'poster']
)
