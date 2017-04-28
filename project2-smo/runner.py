import smo
from sys import argv
import csv
import random
import math
import statistics

data = []

with open(argv[1], 'r') as csvfile:
    data_file = csv.reader(csvfile, quoting=csv.QUOTE_NONNUMERIC)
    for row in data_file:
        data.append((row[1:], row[0]))

accuracies = []

for i in range(int(argv[2])):
    train_size = len(data) * .7
    training = []
    test = list(data)
    while len(training) < train_size:
        index = random.randrange(len(test))
        training.append(test.pop(index))

    results = smo.smo(1000000000, 0.0000001, 10, training, smo.dot_kernel)

    correct_num = 0
    for j in range(len(test)):
        prediction = math.floor(smo.classify(results[0], test[j][0], results[1], smo.dot_kernel))
        if prediction == 0:
            prediction = 1

        if (prediction == test[j][1]):
            correct_num += 1

    accuracies.append(100 * correct_num / len(test))

print(accuracies)
print("Mean Accuracy: {}".format(statistics.mean(accuracies)))
print("Standard Dev:  {}".format(statistics.stdev(accuracies)))
