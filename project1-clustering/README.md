# Project 1: Clustering
The first project invlolves algorithms for unsupervised clustering. For this project, I implemented two algorithms: K-Means and EM (Expectation Maximization).

## The K-Means Algorithm

K-Means is a discrete clustering algorithm. It accepts as input a list of k-dimensional examples and a number of clusters to group the examples into, `j`. It works in an interative fashion, beginning by randomly placing `j` means, and assigning each example to one of the means. The location of the means is then re-calculated as the mean of the examples clustered together in the previous step. These new means are then used for re-assignment of the points, and the process continues until the algorithm reaches convergence. (Convergence is considered to be when the assignment of points does not change between iterations)

My implementation is written in Python 3, and accepts k-dimensional examples. The code can be found in the `kmeans.py` file.

## The EM algorithm

The EM algorithm not only provides assignments for each example, it also calculates all parameters for the distributions that it uses to cluster examples. As a result, each example has a weight vector associated with it, giving the probabilities the example belongs to each of the distributions it is considering. For the sake of this project, my implementation only handles univariate data and Gaussian distributions.

Similar to the K-Means algorithm, EM accepts as input a list of examples and a number of distributions to assign the examples into, and works in an iterative fashion. The "Expectation" portion of the algorithm uses the means and covarient matrices of the distributions to assign weights to each example. The "Maximization" portion updates the distribution parameters based on the example weights. This process then continues util the algorithm reaches convergeence (Since the weight vectors are continuous, convergence is considered when the sum of the changes to means are less than a set threshhold)

My implementation is written in Python 3, and accepts 1-dimensional examples. The code can be found in the `em.py` file.

_**Note:** EM is currently performing much worse than K-Means on similar datasets. This could be the result of a bug in my EM implementation, though I haven't found it yet. Use at your own risk._

## Report

The report for this project, which includes more discussion of the alogorithms, the test cases I used to compare them, and the results of my tests, is available as the `report.pdf` file.

## Dependancies

Running these implementations will require having `numpy` and `scipi` installed.
