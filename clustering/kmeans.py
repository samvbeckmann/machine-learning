from scipy.spatial.distance import *
import numpy as np

def kmeans(points, min_space, max_space, clusters, dims):
    centers = gen_random_centers(min_space, max_space, clusters, dims)
    prev_assignment = None
    while True:
        # Assign points
        distances = cdist(points, centers, 'euclidean').tolist()
        assignment = {x.index(min(x)) for x in distances}
        # if assignment = prev_assignment end
        if assignment == prev_assignment:
            break
        # Update centers
        print(type(centers))
        for x in range(len(centers)):
            myset = {point for point, assign in zip(points, assignment) if assign == x}
            centers[x] = np.mean(myset, axis=0)
        prev_assignment = assignment
    return  (centers, prev_assignment)

def gen_random_centers(min_scale, max_scale, clusters, dims):
    return (max_scale - min_scale) * np.random.rand(clusters, dims) + min_scale
    # Could better support non-square spaces.

result = kmeans([[1,1],[1,2],[5,1],[5,2]], 0, 10, 2, 2)
print(result[0])
print(result[1])
