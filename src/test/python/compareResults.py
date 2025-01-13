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

# Initialize arrays with np.inf as default values
obtainedRDists = np.full(len(D), np.inf)
obtainedCDists = np.full(len(D), np.inf)
obtainedOrderedFile = np.zeros(len(D), dtype=int)

# Read RDists.csv, excluding the last line
with open(outputDir + 'RDists.csv', 'r') as file:
    lines = file.readlines()
    for i, line in enumerate(lines):
        if line.strip() and line != "NaN\n":
            obtainedRDists[i] = float(line.strip())

# Read CDists.csv, excluding the last line
with open(outputDir + 'CDists.csv', 'r') as file:
    lines = file.readlines()
    for i, line in enumerate(lines):
        if line.strip() and line != "NaN\n":
            obtainedCDists[i] = float(line.strip())
            
# Read orderedFile.csv, excluding the last line
with open(outputDir + 'orderedFile.csv', 'r') as file:
    lines = file.readlines()
    for i, line in enumerate(lines):
        if line.strip():  # This ensures we skip empty lines
            obtainedOrderedFile[i] = int(line.strip())

plotGraphs(D, optics, obtainedRDists, obtainedCDists, obtainedOrderedFile, outputDir)

# Write to outputfile verification for ordered file
counter = sum(1 for i in range(len(D)) if obtainedOrderedFile[i] == optics.ordering_[i])

with open(outputDir + 'verification.txt', 'w') as file:
    if counter == len(D):
        file.write('The ordered file is correct')
    else:
        file.write('The ordered file is incorrect')
