import json
import math
from pprint import pprint
import random
from collections import Counter

def splitDataset(dataset, splitRatio):
	trainSize = int(len(dataset) * splitRatio)
	trainSet = []
	copy = list(dataset)
	while len(trainSet) < trainSize:
		index = random.randrange(len(copy))
		trainSet.append(copy.pop(index))
	return [trainSet, copy]



with open('game_data.json') as data_file:
    data = json.load(data_file)
train, test = splitDataset(data, .8)

by_classification = [[] for i in range(11)]
platforms = set()
genres = set()

for instance in data:
    platforms.add(instance['Platform'])
    for genre in instance['Genre']:
        genres.add(genre)

for instance in train:
    class_val = math.floor(instance['Score'])
    by_classification[class_val].append(instance)

priors = [None] * len(by_classification)
platform_likelihoods = [{} for i in range(len(by_classification))]
genre_likelihoods = [{} for i in range(len(by_classification))]

for i in range(len(by_classification)):
    priors[i] = len(by_classification[i]) / len(train)

    platform_counter = Counter()
    genre_counter = Counter()

    for instance in by_classification[i]:
        platform_counter.update({instance['Platform']})
        for genre in instance['Genre']:
            genre_counter.update({genre})

    for platform in platforms:
        platform_likelihoods[i][platform] = platform_counter[platform] / len(by_classification[i])
    for genre in genres:
        genre_likelihoods[i][genre] = genre_counter[genre] / len(by_classification[i])

correct = 0
total_error = 0

for instance in test:
    posterior = []
    for i in range(len(priors)):
        result = priors[i] * platform_likelihoods[i][instance['Platform']]
        for genre in genres:
            if genre in instance['Genre']:
                result *= genre_likelihoods[i][genre]
            else:
                result *= 1 - genre_likelihoods[i][genre]
        posterior.append(result)
    classification = posterior.index(max(posterior))
    error = classification - math.floor(instance['Score'])
    total_error += abs(error)
    # is_correct = classification == math.floor(instance['Score'])
    if error == 0: correct += 1
    print('{:50} --> {:9} | Predicted: {} | Actual: {} | Error: {}'
          .format(instance['Game'],
                  'CORRECT' if error == 0 else 'INCORRECT',
                  classification,
                  math.floor(instance['Score']),
                  error))

print('\nAccuracy: {:.0f}%'.format(100 * correct / len(test)))
print('Average Error: {:.2f}'.format(total_error / len(test)))

random_correct = 0
random_error = 0
for instance in test:
    selection = random.randrange(11)
    error = selection - instance['Score']
    if error == 0:
        random_correct += 1
    random_error += abs(error)

print('\nRandom Accuracy: {:.0f}%'.format(100 * random_correct / len(test)))
print('Random Average Error: {:.2f}'.format(random_error / len(test)))
