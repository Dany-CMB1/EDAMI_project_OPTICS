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


def calculate_relative_differences(obtained, expected):
    """Calculate relative differences, handling special cases."""
    rel_differences = np.zeros(len(expected))
    for i in range(len(expected)):
        if expected[i] == 0 or abs(obtained[i]) == np.inf or np.isnan(obtained[i]):
            rel_differences[i] = 0
        else:
            rel_differences[i] = 100 * (obtained[i] - expected[i]) / expected[i]
    return rel_differences


import matplotlib.pyplot as plt
import numpy as np
from matplotlib.gridspec import GridSpec

def plotGraphs(D, optics, obtainedRDists, obtainedCDists, obtainedOrdered, outputDir):
        
        attributes = D.columns.tolist()
        expectedCDists = optics.core_distances_
        expectedRDists = optics.reachability_
        
        # Compute absolute and relative differences. Avoid division by zero, nan values and inf values
        relCDists = calculate_relative_differences(obtainedCDists, expectedCDists)
        relRDists = calculate_relative_differences(obtainedRDists, expectedRDists)              
                        
         # 2D data: plottable
        if (len(attributes) == 3):
                numRows = 2
        else:
                numRows = 1

        # Create a figure with GridSpec layout
        fig = plt.figure(figsize=(12, 10))
        gs = GridSpec(numRows,1, figure=fig)

        currRow = 0
        # Scatter plot: full width on the first row
        if (numRows == 2):
                ax1 = fig.add_subplot(gs[currRow])
                ax1.scatter(D.x, D.y, marker='x')
                ax1.set_xlabel(attributes[0])
                ax1.set_ylabel(attributes[1])
                ax1.set_title('Dataset')
                currRow+=1
    
        # Reachability distances vs ordered objects
        # Convert obtainedOrdered to an array of indices

        # Reorder distances based on the ordered indices in obtainedOrdered
        obtainedRDistsOrdered = [obtainedRDists[i] for i in obtainedOrdered]
        expectedRDistsOrdered = [optics.reachability_[i] for i in obtainedOrdered]
        relCDistsOrdered = [relCDists[i] for i in obtainedOrdered]
        relRDistsOrdered = [relRDists[i] for i in obtainedOrdered]
        
        ax2 = fig.add_subplot(gs[currRow])
        ax2.plot(expectedRDistsOrdered, label='Expected', color='blue')
        ax2.set_xlabel('Ordered Objects')
        ax2.set_ylabel('Expected Reachability Distances', color='blue')
        ax2.tick_params(axis='y', labelcolor='blue')
        ax2.set_title('Reachability Distances vs Ordered Objects')
        ax2.set_xlim(0, len(expectedRDists))
        ax2.set_ylim(ymin=0)
        
        ax22 = ax2.twinx()
        ax22.plot(obtainedRDistsOrdered, label='Obtained', color='red')
        ax22.set_ylabel('Obtained Reachability Distances', color='red')
        ax22.tick_params(axis='y', labelcolor='red')
        ax22.set_ylim(ymin=0, ymax=ax2.get_ylim()[1])
        
        ax2.legend(loc='upper left')
        ax22.legend(loc='upper right')
        
        # Save the first figure
        plt.tight_layout()
        plt.savefig(outputDir + 'scatter_and_RDists.png')
        
        # Second figure: Differences in core and reachability distances
        fig2, (ax3, ax4) = plt.subplots(1, 2, figsize=(12, 6))
        
        # Core distances difference
        ax3.plot(np.arange(len(expectedCDists)), relCDistsOrdered, label='Core Distances Difference', color='green')
        ax3.set_xlabel('Ordered Object ID')
        ax3.set_ylabel('Relative Difference (%)')
        ax3.set_title('Core Distances Difference')
        ax3.set_ylim(0)
        ax3.set_xlim(0, len(expectedCDists))
        # Display statistics, exclude nan values
        string = 'Mean: {:.2f}%\nStd: {:.2f}%\nMax: {:.2f}%\nMin: {:.2f}%'.format(np.nanmean(relCDists), np.nanstd(relCDists), np.nanmax(relCDists), np.nanmin(relCDists))
        ax3.text(0.5, 0.5,string, horizontalalignment='center', verticalalignment='center', transform=ax3.transAxes)
        
        # Reachability distances difference
        ax4.plot(np.arange(len(expectedRDists)), relRDistsOrdered, label='Reachability Distances Difference', color='orange')
        ax4.set_xlabel('Ordered Object ID')
        ax4.set_ylabel('Relative Difference (%)')
        ax4.set_title('Reachability Distances Difference')
        ax4.set_ylim(0)
        ax4.set_xlim(0, len(expectedRDists))
        # Display statistics, exclude nan values
        string = 'Mean: {:.2f}%\nStd: {:.2f}%\nMax: {:.2f}%\nMin: {:.2f}%'.format(np.nanmean(relRDists), np.nanstd(relRDists), np.nanmax(relRDists), np.nanmin(relRDists))
        ax4.text(0.5, 0.5, string, horizontalalignment='center', verticalalignment='center', transform=ax4.transAxes)
        
        # Save the second figure
        plt.tight_layout()
        plt.savefig(outputDir + 'differences.png')
