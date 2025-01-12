from sklearn.cluster import OPTICS
import csv
import math
import pandas as pd
import matplotlib.pyplot as plt


# D = pd.DataFrame(pd.read_csv('data/iris/iris.data', header=None,
#     names=["SepalLength", "SepalWidth", "PetalLength", "PetalWidth", "Class"],
#     dtype={"PetalLength": float, "PetalWidth": float, "SepalLength": float, "SepalWidth": float, "Class": "category"}))

# optics = OPTICS(min_samples=4, max_eps=1, cluster_method='dbscan')
# optics.fit(D[["SepalLength", "SepalWidth", "PetalLength", "PetalWidth"]])

# file = open('output/iris/expected.csv', 'w')
# for o in optics.ordering_:
#     file.write(str(o) + '\n')
# file.close()

D = pd.DataFrame(pd.read_csv('data/2d/2spiral.csv', header=None,
    names=["x", "y", "Class"],
    dtype={"x": float, "y": float, "Class": "category"}))

optics = OPTICS(min_samples=3, max_eps=5.577012574282855, metric="euclidean", cluster_method='xi')
optics.fit(D[["x", "y"]])

file = open('output/2d/2spiral/expectedRDists.csv', 'w')
for rdist in optics.reachability_:
    file.write(str(rdist) + '\n')
file.close()

file = open('output/2d/2spiral/expectedCDists.csv', 'w')
for cdist in optics.core_distances_:
    file.write(str(cdist) + '\n')
file.close()    

# plt.scatter(D.x, D.y, c=optics.labels_)
# plt.show()