#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Dec 12 20:22:13 2016

@author: domenicoalfano
"""
from PIL import Image
import numpy as np
import os

current_dir= os.getcwd()

im = (Image.open('/Users/domenicoalfano/Università/Master/A.I./Homework_Sec_1/Homework2/map.bmp'))
matrix = np.asarray(im)

matrix.flags.writeable = True

def convert_PDDL_command(comand):
        comand = comand.split()
        x,y = substring_after(comand[4],"-")
        make_blue(x,y)
        
def make_blue(row, coloum):
        matrix[row, coloum] = [0, 0, 255]

def laod_command():
    file = open(current_dir+"/Path-1.txt","r")
    read = file.read()
    read = read.split("\n")
    file.close()
    return read

def find_between(s, start, end):
  return (s.split(start))[1].split(end)[0]


def substring_after(s, delim):
    c = s.partition(delim)
    comand = c[2].partition(delim)
    return int(comand[0]),int(comand[2])        

def create_path():
    commands = laod_command()
    for com in commands:
        if len(com) > 0:
            convert_PDDL_command(com)
        
    im = Image.fromarray(matrix)
    im.save(current_dir + "/trace_map.bmp")
    
    

    
    


    
