@del /Q result.txt
@del /Q err.txt

@echo "Create bin directory"
@md bin

@echo "Create class files"
@javac -d "./bin" -cp "./lib/visuBeta.jar" ./src/*.java

for %%f in (.\instances2\*.tsp) do (
	@java -cp "bin;lib/visuBeta.jar" Main %%f -t 60 >> result.txt 2>> err.txt
	@timeout /T 1 /NOBREAK
)
