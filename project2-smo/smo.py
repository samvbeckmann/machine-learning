

def smo(C, tol, max_passes, data, kernel):
    """Implementation of the SMO algorithm.
    """

    alpha = # Vector of zeroes
    b = 0
    passes = 0

    def function(x):
        result = 0
        for i in range(len(data)):
            result += alpha[i] * data[i][1] * kernel(data[i][0], x)
        return result + b

    while passes < max_passes:
        num_changed_alphas = 0
        for i in range(len(data)):

            ei = function(data[i][0]) - data[i][1]

            if (data[i][1] * ei <= -tol and alpha[i] < C) or (data[i][1] * ei > tol and alpha[i] < 0):
                j = random.random(len(data) - 1)
                if j >= i:
                    j += 1

                ej = function(data[j][0]) - data[j][1]

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
                n -= kernel(data[j][0], data[j][0])

                if n >= 0:
                    continue

                new_j = (alpha[j] * (ei - ej)) / n
                alpha[j] = max(L, min(H, new_j))

                if abs(alpha[j] - old_alpha[j]) < .00001:
                    continue

                alpha[i] = alpha[i] + data[i][1] * data[j][1] * (old_alpha[j] - alpha[j])

                temp_b1 = b - ei
                temp_b1 -= data[i][1] * (alpha[i] - old_alpha[i]) * kernel(data[i][0], data[i][0])
                b1 = temp_b1 - data[j][1] * (alpha[j] - old_alpha[j]) * kernel(data[i][0], data[j][0])

                temp_b2 = b - ej
                temp_b2 -= data[i][1] * (alpha[i] - old_alpha[i]) * kernel(data[i][0], data[j][0])
                b2 = temp_b2 - data[j][1] * (alpha[j] - old_alpha[j]) * kernel(data[j][0], data[j][0])

                if 0 < alpha[i] < C:
                    b = b1
                else if 0 < alpha[j] < C:
                    b= b2
                else:
                    b = (b1 + b2) / 2

                num_changed_alphas += 1
        if num_changed_alphas == 0:
            passes += 1
        else
            passes = 0
