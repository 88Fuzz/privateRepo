#!/bin/python
import subprocess

copyLocation = 'github/privateRepo/Circle/'
p = subprocess.Popen('git ls-tree --name-only -r HEAD', stdout=subprocess.PIPE, shell=True)

files = p.stdout.read();
for butt in files.split():
    dirs = butt.split('/');
    string = ''
    for i in range(0, len(dirs)-1):
        string = string + dirs[i] + '/'

    print string
    p = subprocess.Popen('mkdir -p ' + copyLocation + string, shell=True);
    p.communicate();
    
    p = subprocess.Popen('cp ' + butt + ' ' + copyLocation + butt, shell=True);
