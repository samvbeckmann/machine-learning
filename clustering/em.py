import scipy.stats.norm

def em_univariate(points, distributions):
    """Executes the Expectation / Maximization Algorithm on the data.
    `Points` is a list of tuples representing the points, and `distributions` is
    the number of distributions to use in the algorithm."""

    # TODO: Init: randomly assign weights to points

    while True:

        # Update phi vector (normalized sum of weights for distribution)
        weight_sums = map(sum, zip(*weights))
        phi = [x / len(points) for x in weight_sums]

        # Update means
        for j in range(distributions):
            total = 0
            for i in range(len(points)):
                total += weights[i][j] * points[i]
            means[j] = total / weight_sums[j]

        # TODO: Update parameter matrix
        for j in range(distributions):
            total = 0
            for i in range(len(points)):
                total += weights[i][j] * pow(points[i] - means[j], 2)
            parameters[j] = total / weight_sums[j]

        # TODO: Break if nothing much has changed
        if (last_ll - log_likelihood) - last_ll <= tol:
            break;

        # TODO: Perform E step --> Updating weights based on distributions
        for j in range(distributions):
            for i in range(len(points)):
                temp_weights[i] = norm.pdf(points[i], means[j], parameters[j]) * phi[j]
            norm_const = sum(temp_weights)
            weights = [x / norm_const for x in temp_weights]
