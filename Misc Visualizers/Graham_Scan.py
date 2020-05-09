import matplotlib.pyplot as plt
from random import randrange as rand
from math import atan2 as ang

def create_points(count,max_len=50):
    return [(rand(max_len),rand(max_len)) for _ in range(count)]

def distance(p1):
    return abs(p1[0]-anchor[0]) + abs(p1[1]-anchor[1])

def angle(p1):
    return ang(p1[1]-anchor[1],p1[0]-anchor[0])

def sort(p1):
    return (angle(p1),distance(p1))

def det(p1,p2,p3):
    a = p2[0] - p1[0]
    b = p2[1] - p1[1]
    c = p3[0] - p1[0]
    d = p3[1] - p1[1]
    return a*d - b*c

def scatter_plot(points,hull=None):
    xs,ys = zip(*points)
    plt.scatter(xs,ys)
    if hull != None:
        for i in range(1,len(hull)+1):
            if i == len(hull): i = 0
            p1 = hull[i-1]
            p2 = hull[i]
            plt.plot((p1[0],p2[0]),(p1[1],p2[1]),'r')
    plt.show()

def scan(points,progress=False):  
    global anchor
    y = None
    for p in points:
        if y == None or p[1] < y[1]:
            y = p
            continue
        if y[1] == p[1] and p[0] < y[0]:
            y = p
    anchor = y
    s_points = sorted(points,key=sort)
    s_points.pop(0)
    hull = [anchor,s_points.pop(0)]
    for p in s_points:
        while det(hull[-2],hull[-1],p) < 0 :
            del hull[-1]
        hull.append(p)
        if progress : scatter_plot(points,hull)
    scatter_plot(points,hull)

points = create_points(20)
scan(points,True)
    
    
        
        
        
    

            
            
                


