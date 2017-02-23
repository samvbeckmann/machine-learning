from PIL import Image
from kmeans import kmeans
import sys

img_path = sys.argv[1]

img = Image.open(img_path)
img_data = list(img.getdata())
results = kmeans(img_data, int(sys.argv[2]))
#print(results[0])
colors = [tuple(map(int, x)) for x in results[0]]
#print(colors)
new_data = [colors[x] for x in results[1]]
new_img = Image.new("RGB", img.size)
new_img.putdata(new_data)
new_img.save(sys.argv[3])
new_img.show()
