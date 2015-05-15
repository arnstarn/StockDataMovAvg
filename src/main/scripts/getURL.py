#!/usr/bin/python
import sys

import urllib
page = urllib.urlopen(sys.argv[1])
# print(page.read()) # with newline
sys.stdout.write(page.read()) # no newline