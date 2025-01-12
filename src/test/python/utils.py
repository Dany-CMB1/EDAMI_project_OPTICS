import pandas as pd
def readFile(file, category):
    if (category == '2d'):
            D = pd.DataFrame(pd.read_csv(file, header=None,
            names=["x", "y", "Class"],
            dtype={"x": float, "y": float, "Class": "category"}))
            
    elif (category == 'iris'):
            D = pd.DataFrame(pd.read_csv(file, header=None,
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
        
        # Get absolute and relative differences for core and reachability distances. Set to 0 if division by 0
        absCDists = abs(expectedCDists - obtainedCDists)
        absRDists = abs(expectedRDists - obtainedRDists)
        relCDists = np.zeros(len(expectedCDists))
        relRDists = np.zeros(len(expectedRDists))
        for i in range(len(expectedCDists)):
                if (expectedCDists[i] == 0):
                        relCDists[i] = 0
                else:
                        relCDists[i] = absCDists[i] / expectedCDists[i]
                        
        for i in range(len(expectedRDists)):        
                if (expectedRDists[i] == 0):
                        relRDists[i] = 0
                else:
                        relRDists[i] = absRDists[i] / expectedRDists[i]
        
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
        
        # Core distances comparison: second row, left
        # Absolute and relative differences are plotted on the same graph
        ax2 = fig.add_subplot(gs[currRow, 0])
        i = np.arange(len(expectedCDists))
        ax2.plot(i, absCDists)
        ax2.set_xlabel('Object ID')
        ax2.set_ylabel('Absolute Difference')
        ax2.set_title('Core Distances Comparison')
        ax2.set_ylim(ymin=0)
        ax2.set_xlim(xmin = 0, xmax=len(expectedCDists))
        ax2.grid()
        ax21 = ax2.twinx()
        ax21.plot(i,  relCDists, 'r--', label='Relative Difference')
        ax21.set_ylabel('Relative Difference', color='r')
        ax21.tick_params(axis='y', labelcolor='r')

        # Reachability distances comparison: second row, right
        ax3 = fig.add_subplot(gs[currRow, 1])
        i = np.arange(len(expectedRDists))
        ax3.plot(i, absRDists)
        ax3.set_xlabel('Object ID')
        ax3.set_ylabel('Aboslute Difference')
        ax3.set_title('Reachability Distances Comparison')
        ax3.set_ylim(ymin=0)
        ax3.set_xlim(xmin=0, xmax=len(expectedRDists))
        ax3.grid()
        ax31 = ax3.twinx()
        ax31.plot(i,  relRDists, 'r--', label='Relative Difference')
        ax31.set_ylabel('Relative Difference', color='r')
        ax31.tick_params(axis='y', labelcolor='r')


        # Adjust layout for readability
        plt.tight_layout()

        # Show the combined figure
        plt.show()