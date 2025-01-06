from sklearn.cluster import OPTICS
import csv
import pandas as pd


D = pd.DataFrame(pd.read_csv('data/iris/iris.data', header=None,
    names=["SepalLength", "SepalWidth", "PetalLength", "PetalWidth", "Class"],
    dtype={"PetalLength": float, "PetalWidth": float, "SepalLength": float, "SepalWidth": float, "Class": "category"}))

optics = OPTICS(min_samples=4, max_eps=1, cluster_method='dbscan')
optics.fit(D[["SepalLength", "SepalWidth", "PetalLength", "PetalWidth"]])

file = open('res/iris/expected.csv', 'w')
for o in optics.ordering_:
    file.write(str(o) + '\n')
file.close()