
import math
import random

def dot_kernel(x1, x2):
    result = 0
    for i in range(len(x1)):
        result += x1[i] * x2[i]
    return result

def gaussian_kernel(x1, x2):
    result = 0
    for i in range(len(x1)):
        result += math.pow(x1[i] - x2[i], 2)
    return math.exp(result * -0.5)

def classify(supports, x, b, kernel):
    result = 0
    for i in range(len(supports)):
        result += supports[i][1] * supports[i][0][1] * kernel(supports[i][0][0], x)
    return result + b

def smo(C, tol, max_passes, data, kernel):
    """Implementation of the SMO algorithm.
    """

    alpha = [0 for x in range(len(data))]# Vector of zeroes
    old_alpha = alpha[:]
    b = 0
    passes = 0
    E = alpha[:]

    def function(x):
        result = 0
        for i in range(len(data)):
            result += alpha[i] * data[i][1] * kernel(data[i][0], x)
        return result + b

    while passes < max_passes:
        num_changed_alphas = 0
        for i in range(len(data)):

            E[i] = function(data[i][0]) - data[i][1]

            if (data[i][1] * E[i] < -tol and alpha[i] < C) or (data[i][1] * E[i] > tol and alpha[i] > 0):
                j = random.randint(0, len(data) - 2)
                if j >= i:
                    j += 1

                E[j] = function(data[j][0]) - data[j][1]

                old_alpha[i] = alpha[i]
                old_alpha[j] = alpha[j]

                L = 0
                H = 0
                if data[i][1] == data[j][1]:
                    L = max(0, alpha[i] + alpha[j] - C)
                    H = min(C, alpha[i] + alpha[j])
                else:
                    L = max(0, alpha[j] - alpha[i])
                    H = min(C, C + alpha[j] - alpha[i])

                if L == H:
                    continue

                temp_n = 2 * kernel(data[i][0], data[j][0])
                temp_n -= kernel(data[i][0], data[i][0])
                n = temp_n - kernel(data[j][0], data[j][0])

                if n >= 0:
                    continue

                new_j = alpha[j] - (data[j][1] * (E[i] - E[j])) / n
                alpha[j] = max(L, min(H, new_j))

                if abs(alpha[j] - old_alpha[j]) < .00001:
                    continue

                alpha[i] = alpha[i] + data[i][1] * data[j][1] * (old_alpha[j] - alpha[j])

                temp_b1 = b - E[i]
                temp_b1 -= data[i][1] * (alpha[i] - old_alpha[i]) * kernel(data[i][0], data[i][0])
                b1 = temp_b1 - data[j][1] * (alpha[j] - old_alpha[j]) * kernel(data[i][0], data[j][0])

                temp_b2 = b - E[j]
                temp_b2 -= data[i][1] * (alpha[i] - old_alpha[i]) * kernel(data[i][0], data[j][0])
                b2 = temp_b2 - data[j][1] * (alpha[j] - old_alpha[j]) * kernel(data[j][0], data[j][0])

                # print("b1: {} b2  : {}".format(b1, b2))
                if 0 < alpha[i] and alpha[i] < C:
                    b = b1
                elif 0 < alpha[j] and alpha[j] < C:
                    b = b2
                else:
                    b = (b1 + b2) / 2

                num_changed_alphas += 1
        if num_changed_alphas == 0:
            passes += 1
        else:
            passes = 0

    supports = []
    for i in range(len(data)):
        if alpha[i] != 0:
            supports.append((data[i], alpha[i]))
    return supports, b
