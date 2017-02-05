from scipy.spatial.distance import *
import numpy as np
import random

def kmeans(points, clusters):
    centers = random.sample(points, clusters)
    prev_assignment = None
    while True:

        # Assign points
        distances = cdist(points, centers, 'euclidean').tolist()
        assignment = [x.index(min(x)) for x in distances]

        # if assignment = prev_assignment end
        if assignment == prev_assignment:
            break

        # Update centers
        for x in range(len(centers)):
            myset = [point for point, assign in zip(points, assignment) if assign == x]
            centers[x] = np.mean(myset, axis=0)
        prev_assignment = assignment

    return  (centers, prev_assignment)

result = kmeans([(1,1),(1,2),(5,1),(5,2)], 2)
print([list(x) for x in result[0]])
print(result[1])
