from kmeans import kmeans
from em import em_univariate
import sys
import csv

clusters = int(sys.argv[2])
points = []
kmeans_acc = []
em_acc = []

with open(sys.argv[1]) as tsv:
    points, sources = zip(*[(float(line[1]), float(line[0])) for line in csv.reader(tsv, delimiter="\t")])

    for _ in range(int(sys.argv[3])):
        em_results = em_univariate(points, clusters)
        kmeans_results = kmeans([[x] for x in points], clusters)

        # print("kmeans clusters:", [x[0] for x in kmeans_results[0]])
        # print("EM clusters:", em_results[0])

        num_pairs = 0
        num_correct = 0;
        for index_one, x in enumerate(kmeans_results[1]):
            for index_two in range(index_one, len(kmeans_results[1])):
                if x == kmeans_results[1][index_two]:
                    num_pairs += 1
                    if sources[index_one] == sources[index_two]:
                        num_correct += 1

        kmeans_acc.append(num_correct / num_pairs)


        num_pairs = 0
        num_correct = 0;
        for index_one, x in enumerate(em_results[1]):
            for index_two in range(index_one, len(em_results[1])):
                y = em_results[1][index_two]
                if x.index(max(x)) == y.index(max(y)):
                    num_pairs += 1
                    if sources[index_one] == sources[index_two]:
                        num_correct += 1

    em_acc.append(num_correct / num_pairs)

    print("kmeans result:", sum(kmeans_acc) / float(len(kmeans_acc)))
    print("EM results:", sum(em_acc) / float(len(em_acc)))
