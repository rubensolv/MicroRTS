#!/bin/bash
#SBATCH --time=03-12:00      # time (DD-HH:MM)
#SBATCH --cpus-per-task=2
#SBATCH --mem-per-cpu=8G 
#SBATCH --output=%N-%j.txt  # %N for node name, %j for jobID

echo 'inicio'
sleep 30


module load java/1.8.0_192

java -Xms2g -Xmx8g  -XX:ParallelGCThreads=8 -jar tc.jar ${i} ${j}


echo 'teste'
sleep 30
echo 'FIM'