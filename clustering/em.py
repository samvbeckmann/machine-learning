from scipy.stats import norm
import random

def em_univariate(points, distributions):
    """Executes the Expectation / Maximization Algorithm on univariate data.

    Keyword arguments:
    points -- a list of floats representing the points
    distributions -- the number of distributions to use
    """
    # Init
    tol = .001
    weights = []
    means = []
    for x in range(len(points)):
        temp = [random.random() for i in range(distributions)]
        s = sum(temp)
        weight_vec = [i / s for i in temp]
        weights.append(weight_vec)

    while True:

        # Update phi vector (normalized sum of weights for distribution)
        weight_sums = list(map(sum, zip(*weights)))
        phi = [x / len(points) for x in weight_sums]
        old_means = means
        means = []

        # Update means
        for j in range(distributions):
            total = 0
            for i in range(len(points)):
                total += weights[i][j] * points[i]
            means.append(total / weight_sums[j])

        parameters = []

        # Update parameter matrix
        for j in range(distributions):
            total = 0
            for i in range(len(points)):
                total += weights[i][j] * pow(points[i] - means[j], 2)
            parameters.append(total / weight_sums[j])

        # Break if nothing much has changed
        if (sum(means) - sum(old_means)) < tol:
            break;

        # Perform E step --> Updating weights based on distributions
        for j in range(distributions):
            temp_weights = []
            for i in range(len(points)):
                temp_weights.append(norm.pdf(points[i], means[j], parameters[j]) * phi[j])
            norm_const = sum(temp_weights)
            weights[j] = [x / norm_const for x in temp_weights]

    return (means, weights)
