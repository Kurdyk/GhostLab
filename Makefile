ifeq ($(OS),Windows_NT)
	jfx = Windows
else
	UNAME_S := $(shell uname -s)
	ifeq ($(UNAME_S),Darwin)
		jfx = Mac
	else
		jfx = Linux
	endif
endif

configure:
	./univ.sh


client: build_client run_client

build_client:
	mkdir -p out/production/Client out/production/Client/views/
	javac --add-exports java.base/sun.util.locale.provider=ALL-UNNAMED -encoding utf8 --module-path Dependencies/$(jfx)/lib --add-modules javafx.controls,javafx.media,javafx.fxml -d out/production/Client $(shell find Client/src -name "*.java") && cp Client/src/views/*.fxml out/production/Client/views/ && cp Client/src/assets/* out/production/Client/

run_client:
	java -Dfile.encoding=UTF-8 --module-path Dependencies/$(jfx)/lib --add-modules javafx.controls,javafx.media,javafx.fxml -Djava.library.path=Dependencies/$(jfx)/lib -Dfile.encoding=UTF-8 -classpath out/production/Client:Dependencies/$(jfx)/lib/src.zip:Dependencies/$(jfx)/lib/javafx-swt.jar:Dependencies/$(jfx)/lib/javafx.web.jar:Dependencies/$(jfx)/lib/javafx.base.jar:Dependencies/$(jfx)/lib/javafx.fxml.jar:Dependencies/$(jfx)/lib/javafx.media.jar:Dependencies/$(jfx)/lib/javafx.swing.jar:Dependencies/$(jfx)/lib/javafx.controls.jar:Dependencies/$(jfx)/lib/javafx.graphics.jar Apps.MainApp


serveur: build_server run_server

build_server:
	mkdir -p out/production/Serveur
	javac --add-exports java.base/sun.util.locale.provider=ALL-UNNAMED -encoding utf8 -d out/production/Serveur $(shell find Serveur/src -name "*.java")

run_server:
	java -Dfile.encoding=UTF-8 -classpath out/production/Serveur GhostLabServer