import pandas as pd
def readFile(file, category):
    if (category == '2d'):
            D = pd.DataFrame(pd.read_csv(file, header=None,
            names=["x", "y", "Class"],
            dtype={"x": float, "y": float, "Class": "category"}))
            
    elif (category == 'iris'):
            D = pd.DataFrame(pd.read_csv('data/iris/iris.data', header=None,
            names=["SepalLength", "SepalWidth", "PetalLength", "PetalWidth", "Class"],
            dtype={"PetalLength": float, "PetalWidth": float, "SepalLength": float, "SepalWidth": float, "Class": "category"}))
    else:
        D = None
        print("Category not found")
    return D


import matplotlib.pyplot as plt
import numpy as np
from matplotlib.gridspec import GridSpec
def plotGraphs(D, optics, expectedRDists, obtainedRDists, expectedCDists, obtainedCDists):
        
        attributes = D.columns.tolist()
        
        # 2D data: plottable
        if (len(attributes) == 3):
                numRows = 2
        else:
                numRows = 1
        
        # Create a figure with GridSpec layout
        fig = plt.figure(figsize=(12, 10))
        gs = GridSpec(numRows,2, figure=fig)

        currRow = 0
        # Scatter plot: full width on the first row
        if (numRows == 2):
                ax1 = fig.add_subplot(gs[currRow, :])
                ax1.scatter(D.x, D.y, c=optics.labels_)
                ax1.set_xlabel(attributes[0])
                ax1.set_ylabel(attributes[1])
                ax1.set_title('Dataset')
                currRow+=1

        print(numRows, currRow)
        # Core distances comparison: second row, left
        ax2 = fig.add_subplot(gs[currRow, 0])
        i = np.arange(len(expectedCDists))
        ax2.plot(i, abs(expectedCDists[i] - obtainedCDists[i]))
        ax2.set_xlabel('Object ID')
        ax2.set_ylabel('Difference')
        ax2.set_title('Core Distances Comparison')

        # Reachability distances comparison: second row, right
        ax3 = fig.add_subplot(gs[currRow, 1])
        i = np.arange(len(expectedRDists))
        ax3.plot(i, abs(expectedRDists[i] - obtainedRDists[i]))
        ax3.set_xlabel('Object ID')
        ax3.set_ylabel('Difference')
        ax3.set_title('Reachability Distances Comparison')

        # Adjust layout for readability
        plt.tight_layout()

        # Show the combined figure
        plt.show()