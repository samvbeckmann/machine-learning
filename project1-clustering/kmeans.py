from scipy.spatial.distance import *
import numpy as np
import random

def kmeans(points, clusters):
    """Executes the kmeans clustering algorithm.

    Keyword parameters:
    points -- a list of tuples representing the points
    clusters -- the number of clusters to use
    """
    centers = random.sample(points, clusters)
    prev_assignment = None
    counter = 0
    while True:

        # Assign points
        distances = cdist(points, centers, 'euclidean').tolist()
        assignment = [x.index(min(x)) for x in distances]

        # Test end condition
        if assignment == prev_assignment:
            break

        # Update centers
        for x in range(len(centers)):
            myset = [point for point, assign in zip(points, assignment) if assign == x]
            centers[x] = np.mean(myset, axis=0)
        prev_assignment = assignment

        counter += 1

    return (list(map(tuple, centers)), prev_assignment)
