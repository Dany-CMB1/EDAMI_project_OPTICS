from utils import *
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.gridspec import GridSpec
import math
from sklearn.cluster import OPTICS
import sys

datafile = None
for i in range(1, len(sys.argv)):
    
    if sys.argv[i] in ('-dataset', '-d'):
        dataset = sys.argv[i+1]
        
    if sys.argv[i] in ('-category', '-c'):
        category = sys.argv[i+1]
        
    if sys.argv[i] in ('-file', '-f'):
        datafile = sys.argv[i+1]
        filename = datafile.split('.')
        if (filename[-1] != 'csv'):
            raise Exception("Input file must be CSV")
        else:
            outputDir = 'output/' + filename + '/'  

    if sys.argv[i] in ('-eps'):
        eps = float(sys.argv[i+1])
        if eps < 1:
            raise Exception("Epsilon should be greater than 2")
        
    if sys.argv[i] in ('-minPts'):
        minPts = int(sys.argv[i+1])
        if minPts < 0:
            raise Exception("MinPts should be greater than 1")

if (datafile == None):
    datafile = 'data/'+ category + '/' + dataset + '.csv'
    if (category == dataset):
        outputDir = 'output/' + category + '/'
    else:
        outputDir = 'output/' + category + '/' + dataset + '/'


D = readFile(datafile, category)

optics = OPTICS(min_samples=3, max_eps=math.sqrt(2), metric="euclidean", cluster_method='xi')
optics.fit(D.drop(columns=['Class']))

expectedRDists = optics.reachability_
expectedCDists = optics.core_distances_
expectedOrderedFile = optics.ordering_  
obtainedRDists = np.ndarray(len(expectedRDists))
obtainedCDists = np.ndarray(len(expectedCDists))
obtainedOrderedFile = np.ndarray(len(expectedOrderedFile))

file = open(outputDir + 'RDists.csv', 'r')
for i, line in enumerate(file):
    if (line == "NaN\n"):
        obtainedRDists[i] = np.inf
    obtainedRDists[i] =float(line)
file.close()

file = open(outputDir + 'CDists.csv', 'r')
for i, line in enumerate(file):
    if (line == "NaN\n"):
        obtainedRDists[i] = np.inf
    obtainedCDists[i] = float(line)
file.close()

file = open(outputDir + 'orderedFile.csv', 'r')
for i, line in enumerate(file):
    obtainedOrderedFile[i] = float(line)
file.close()

plotGraphs(D, optics, expectedRDists, obtainedRDists, expectedCDists, obtainedCDists)

# Print verification for ordered file
if (expectedOrderedFile.all() == obtainedOrderedFile.all()):
    print("The ordered file is correct")
