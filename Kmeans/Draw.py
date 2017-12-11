import matplotlib.pyplot as plt
import os
from os.path import join
from colorName import *

if __name__ == "__main__":
    cp = os.getcwd()
    filepath = join(cp, "Output.txt")
    file = open(join(cp, "Output.txt"), "r")
    lines = file.readlines()
    file.close()
    x = []
    y = []
    num = -1
    c = list(cnames.values())
    for line in lines:
        id = int(line[line.index("[")+1:line.index("]")])
        if id != num:
            if num > -1:
                plt.scatter(x, y, color=c[num])
            num = id
            x = []
            y = []      
        point = line[line.rindex("{")+1:line.rindex("}")]
        point = point.split(",")
        x.append(float(point[0]))
        y.append(float(point[1]))
    if num > -1:
        plt.scatter(x, y)
    plt.show()