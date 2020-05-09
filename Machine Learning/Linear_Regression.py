import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('data_1d.csv',header=None)
X = data.iloc[:,:-1].values
y = data.iloc[:,-1:].values
X -= np.mean(X)
X /= np.max(X) - np.min(X)
y -= np.mean(y)
y /= np.max(y) - np.min(y)
X = np.concatenate((np.ones((100,1)),X), axis=1)
m = X.shape[0]
theta = np.zeros((X.shape[1],1))
alpha = 0.9
epochs = 100
plt.scatter(X[:,-1],y)
for i in range(epochs):
    theta -= ((X.dot(theta) - y).T.dot(X).T) * (alpha/m)
    print(theta)
Y = X.dot(theta)
plt.plot(X[:,-1],Y)
plt.show()


##for i in range(epochs):
##    temps = []
##    for j in range(theta.shape[0]):
##        for k in range(m):
##            res = (np.dot(theta.T,X[k]) - y[k]) * X[k,j]
##        res /= m
##        res *= alpha
##        temps.append(theta[j] - res)
##    for j in range(theta.shape[0]):
##        theta[j] = temps[j]
##        print(theta)
##
##plt.scatter(X[:,-1],y)
##Y = np.sum(X.dot(theta),axis = 1)
##plt.plot(X[:,-1],Y,'r')
##plt.show()


        
        
    
    



