import numpy as np 
#import mnist
#import matplotlib.pyplot as plt

#train_images = mnist.train_images()/255
#test_images = mnist.test_images()/255
#train_labels = mnist.train_labels()
#test_labels = mnist.test_labels()

size = 0;
weights = []

def oneHotEncode(a):
    b = np.zeros((a.size, a.max()+1))
    b[np.arange(a.size),a] = 1
    return b

#print(oneHotEncode(test_labels[:5]).shape)
    
def sgm(x,derivative = False):
    if not derivative:
        return 1 / (1+np.exp(-x))
    else:
        return sgm(x)*sgm(1-x)

def test(input):
    layerInputs = []
    layerOutputs = []
    samples = len(input)
    for i in range(size):
        if i == 0:
            layerInput = weights[i].dot(np.concatenate((input.T,np.ones((1,samples)))))
        else:
            layerInput = weights[i].dot(np.concatenate((layerOutputs[-1],np.ones((1,samples)))))
        
        layerInputs.append(layerInput)
        layerOutputs.append(sgm(layerInput))
            
    print(layerOutputs[-1].T)    

def train(shape,input,target,rate= 0.2,epochs= 1000):
    global size
    global weights
    size = len(shape) - 1
    weights = []
    
    for i,j in zip(shape[1:],shape[:-1]):
        weights.append(np.random.normal(size= (i,j+1) ))
        
    
    for i in range(epochs):    
        
        layerInputs = []
        layerOutputs = []
        samples = len(input)
        
        for i in range(size):
            if i == 0:
                layerInput = weights[i].dot(np.concatenate((input.T,np.ones((1,samples)))))
            else:
                layerInput = weights[i].dot(np.concatenate((layerOutputs[-1],np.ones((1,samples)))))
            
            layerInputs.append(layerInput)
            layerOutputs.append(sgm(layerInput))
          
        deltas = [] 
           
        for i in reversed(range(size)):
            if i == size -1:
                outputDelta = layerOutputs[i] - target.T
                deltas.append(outputDelta * sgm(layerInputs[i],True))
            else:
                outputDelta = weights[i+1].T.dot(deltas[-1])
                deltas.append(outputDelta[:-1,:] * sgm(layerInputs[i],True))
                
        for i in range(size):
            deltaIndex = size - 1 - i
            if i == 0:
                layerOutput = np.concatenate((input.T,np.ones((1,samples))))
            else:
                layerOutput = np.concatenate((layerOutputs[i-1],np.ones((1,samples))))
                
            weightDelta = layerOutput.dot(deltas[deltaIndex].T).T
            weights[i] -= rate * weightDelta
        
    
       
train((2,5,2),np.array([[0,0],[1,0],[0,1],[1,1]]),np.array([[0,0],[1,0],[0,1],[1,1]]))   
test(np.array([[0,0],[1,1]])) 
