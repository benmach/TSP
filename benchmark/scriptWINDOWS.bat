del err.txt
del result.txt

FOR %%f IN (".\instances2\*.tsp") DO (
java -cp "bin;lib\TPTSPlib.jar;lib\visuBeta.jar" edu.emn.tsp.Main %%f% -t 60 >> result.txt 2>> err.txt
)