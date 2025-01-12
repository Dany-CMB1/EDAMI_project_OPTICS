import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import math
from sklearn.cluster import OPTICS

D = pd.DataFrame(pd.read_csv('data/2d/2spiral.csv', header=None,
    names=["x", "y", "Class"],
    dtype={"x": float, "y": float, "Class": "category"}))

optics = OPTICS(min_samples=3, max_eps=math.sqrt(2), metric="euclidean", cluster_method='xi')
optics.fit(D[["x", "y"]])

expectedRDists = optics.reachability_
expectedCDists = optics.core_distances_
obtainedRDists = np.ndarray(len(expectedRDists))
obtainedCDists = np.ndarray(len(expectedCDists))

file = open('output/2d/simple/RDists.csv', 'r')
for i, line in enumerate(file):
    if (line == "NaN\n"):
        obtainedRDists[i] = np.inf
    obtainedRDists[i] =float(line)
file.close()

file = open('output/2d/simple/CDists.csv', 'r')
for i, line in enumerate(file):
    if (line == "NaN\n"):
        obtainedRDists[i] = np.inf
    obtainedCDists[i] = float(line)
file.close()

plt.figure()
plt.scatter(D.x, D.y, c=optics.labels_)
plt.show()

plt.figure
i = np.arange(len(expectedCDists))
plt.plot(i, expectedCDists[i]-obtainedCDists[i])
plt.show()