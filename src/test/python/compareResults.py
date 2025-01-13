from utils import *
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.gridspec import GridSpec
import math
from sklearn.cluster import OPTICS
import sys

datafile = None
argv = pd.DataFrame(pd.read_csv("PythonArgs.csv", header=None,
            names=["Datatype", "Dataset", "MinPts", "Eps"],
            dtype={"Datatype": str, "Dataset": str, "MinPts": int, "Eps": float}))

category = argv["Datatype"][0]
dataset = argv["Dataset"][0]
minPts = argv["MinPts"][0]
eps = argv["Eps"][0]

if (datafile == None):
    datafile = 'data/'+ category + '/' + dataset + '.csv'
    if (category == dataset):
        outputDir = 'output/' + category + '/'
    else:
        outputDir = 'output/' + category + '/' + dataset + '/'


D = readFile(datafile, category)

optics = OPTICS(min_samples=minPts, max_eps=eps, metric="euclidean", cluster_method='xi')
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
    print("\n > The ordered file is correct <\n")
