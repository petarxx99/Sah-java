

all:
	javac src/framepackage/*.java
	javac src/paketfigure/*.java
	javac src/paketpolje/*.java
	javac src/raznefigure/*.java
	javac src/Mainclass.java

run:
	java src/Mainclass

cleanFP:
	rm src/framepackage/*.class

cleanRF:
	rm src/raznefigure/*.class

cleanPolje:
	rm src/paketpolje/*.class

cleanFigure:
	rm src/paketfigure/*.class

cleanMain:
	rm src/Mainclass.class

clean:
	rm src/framepackage/*.class
	rm src/paketfigure/*.class
	rm src/paketpolje/*.class
	rm src/raznefigure/*.class
	rm src/Mainclass.class