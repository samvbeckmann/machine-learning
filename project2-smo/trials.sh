ARGS=(
     "datasets/banknotes.csv 5"
     "datasets/diabetes.csv 5"
     "datasets/diagnostic.csv 5"
     "datasets/mines.csv 5"
     "datasets/spectf.csv 5"
     "datasets/two-spirals.csv 5"
)

for i in "${ARGS[@]}";
	do
	echo "$i" && python3 runner.py $i
done
