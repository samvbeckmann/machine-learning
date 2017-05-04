import numpy as np
import csv
import random
from sys import argv
from scipy import optimize
from pprint import pprint

# Modified version of Steven's code, orginally found in:
# Neural Networks Demystified
# Part 6: Training
#
# Supporting code for short YouTube series on artificial neural networks.
#
# Stephen Welch
# @stephencwelch

# Get data from file into np arrays
with open(argv[1], 'r') as csvfile:
    data_file = csv.reader(csvfile)
    inX = []
    inY = []

    for row in data_file:
        inX.append(row[1:])
        inY.append([row[0]])

train_size = len(inX) * .7
trainSplitX = []
trainSplitY = []
testSplitX = list(inX)
testSplitY = list(inY)

while len(trainSplitX) < train_size:
    index = random.randrange(len(testSplitX))
    trainSplitX.append(testSplitX.pop(index))
    trainSplitY.append(testSplitY.pop(index))

trainX = np.array(trainSplitX, dtype=float)
trainY = np.array(trainSplitY, dtype=float)

testX = np.array(testSplitX, dtype=float)
testY = np.array(testSplitY, dtype=float)

trainX = trainX/np.amax(trainX, axis=0)
testX = testX/np.amax(testX, axis=0)

class Neural_Network(object):
    def __init__(self, Lambda=0, numInputs=5):
        #Define Hyperparameters
        self.inputLayerSize = numInputs
        self.outputLayerSize = 1
        self.hiddenLayerSize = 3

        #Weights (parameters)
        self.W1 = np.random.randn(self.inputLayerSize,self.hiddenLayerSize)
        self.W2 = np.random.randn(self.hiddenLayerSize,self.outputLayerSize)

        #Regularization parameter
        self.Lambda = Lambda

    def forward(self, X):
        #Propogate inputs though network
        self.z2 = np.dot(X, self.W1)
        self.a2 = self.sigmoid(self.z2)
        self.z3 = np.dot(self.a2, self.W2)
        yHat = self.sigmoid(self.z3)
        return yHat

    def sigmoid(self, z):
        #Apply sigmoid activation function to scalar, vector, or matrix
        return 1/(1+np.exp(-z))

    def sigmoidPrime(self,z):
        #Gradient of sigmoid
        return np.exp(-z) / ((1+np.exp(-z))**2)

    def costFunction(self, X, y):
        #Compute cost for given X,y, use weights already stored in class.
        self.yHat = self.forward(X)
        J = 0.5 * sum((y - self.yHat) ** 2) / X.shape[0] + (self.Lambda/2) * \
            (np.sum(self.W1**2) + np.sum(self.W2**2))
        return J

    def costFunctionPrime(self, X, y):
        #Compute derivative with respect to W and W2 for a given X and y:
        self.yHat = self.forward(X)

        delta3 = np.multiply(-(y-self.yHat), self.sigmoidPrime(self.z3))
        dJdW2 = np.dot(self.a2.T, delta3)/X.shape[0] + self.Lambda * self.W2

        delta2 = np.dot(delta3, self.W2.T)*self.sigmoidPrime(self.z2)
        dJdW1 = np.dot(X.T, delta2)/X.shape[0] + self.Lambda * self.W1

        return dJdW1, dJdW2

    #Helper Functions for interacting with other classes:
    def getParams(self):
        #Get W1 and W2 unrolled into vector:
        params = np.concatenate((self.W1.ravel(), self.W2.ravel()))
        return params

    def setParams(self, params):
        #Set W1 and W2 using single paramater vector.
        W1_start = 0
        W1_end = self.hiddenLayerSize * self.inputLayerSize
        self.W1 = np.reshape(params[W1_start:W1_end], (self.inputLayerSize , \
                                                       self.hiddenLayerSize))
        W2_end = W1_end + self.hiddenLayerSize*self.outputLayerSize
        self.W2 = np.reshape(params[W1_end:W2_end], (self.hiddenLayerSize, \
                                                     self.outputLayerSize))

    def computeGradients(self, X, y):
        dJdW1, dJdW2 = self.costFunctionPrime(X, y)
        return np.concatenate((dJdW1.ravel(), dJdW2.ravel()))

def computeNumericalGradient(N, X, y):
        paramsInitial = N.getParams()
        numgrad = np.zeros(paramsInitial.shape)
        perturb = np.zeros(paramsInitial.shape)
        e = 1e-4

        for p in range(len(paramsInitial)):
            #Set perturbation vector
            perturb[p] = e
            N.setParams(paramsInitial + perturb)
            loss2 = N.costFunction(X, y)

            N.setParams(paramsInitial - perturb)
            loss1 = N.costFunction(X, y)

            #Compute Numerical Gradient
            numgrad[p] = (loss2 - loss1) / (2*e)

            #Return the value we changed to zero:
            perturb[p] = 0

        #Return Params to original value:
        N.setParams(paramsInitial)

        return numgrad



class trainer(object):
    def __init__(self, N):
        #Make Local reference to network:
        self.N = N

    def callbackF(self, params):
        self.N.setParams(params)
        self.J.append(self.N.costFunction(self.X, self.y))
        self.testJ.append(self.N.costFunction(self.testX, self.testY))

    def costFunctionWrapper(self, params, X, y):
        self.N.setParams(params)
        cost = self.N.costFunction(X, y)
        grad = self.N.computeGradients(X,y)
        return cost, grad

    def train(self, trainX, trainy, testX, testY):
        #Make an internal variable for the callback function:
        self.X = trainX
        self.y = trainy

        self.testX = testX
        self.testY = testY

        #Make empty list to store costs:
        self.J = []
        self.testJ = []

        params0 = self.N.getParams()

        options = {'maxiter': 500, 'disp' : False}
        _res = optimize.minimize(self.costFunctionWrapper, params0, jac=True, \
                                 method='BFGS', args=(trainX, trainy), \
                                 options=options, callback=self.callbackF)

        self.N.setParams(_res.x)
        self.optimizationResults = _res

for i in range(int(argv[2])):
    NN = Neural_Network(Lambda=0.0001, numInputs=len(trainX[0]))
    T = trainer(NN)
    T.train(trainX,trainY,testX,testY)

    avgErrors = []
    avgStdevs = []
    randomErrors = []
    randomStdevs = []

    errors = abs(NN.forward(testX) - testY)
    avgErrors.append(np.mean(errors))
    avgStdevs.append(np.std(errors))
    rndErrors = abs(np.random.rand(len(testY), 1) - testY)
    randomErrors.append(np.mean(rndErrors))
    randomStdevs.append(np.std(rndErrors))

print('Average Error: {:.2f}%'.format(100 * np.mean(avgErrors)))
print('Average Stdev: {:.2f}'.format(np.mean(avgStdevs)))
print('Random Error:  {:.2f}%'.format(100 * np.mean(randomErrors)))
print('Random Stdev:  {:.2f}'.format(np.mean(randomStdevs)))
