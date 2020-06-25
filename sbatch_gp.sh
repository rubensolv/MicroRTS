#!/bin/bash
#SBATCH --time=00-00:05      # time (DD-HH:MM)
for i in {0..3}
do
	for j in {0..4}
	do
		for rep in {0..4} 
		do
							
				sbatch --output=r3/out_${i}_${j}_${rep}.txt --export=i=${i},j=${j} run_gp.sh
			
		done
	done
done
