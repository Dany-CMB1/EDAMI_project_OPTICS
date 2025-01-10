from sklearn.cluster import OPTICS
import csv
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

optics = OPTICS(min_samples=4, max_eps=1, cluster_method='dbscan')
optics.fit(D[["x", "y"]])

file = open('output/2d/expectedRDists.csv', 'w')
for o in optics.reachability_:
    file.write(str(o) + '\n')
file.close()

file = open('output/2d/expectedCDists.csv', 'w')
for o in optics.core_distances_:
    file.write(str(o) + '\n')
file.close()    

plt.scatter(D.x, D.y, c=optics.labels_)
plt.show()