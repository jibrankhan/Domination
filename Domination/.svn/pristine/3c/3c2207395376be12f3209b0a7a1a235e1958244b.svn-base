rem -g:none -verbose -Xlint:all -Xlint:unchecked -Xlint:deprecation
rem add -O after javac

set JAVA_OPTS=-source 1.4 -target 1.4 -d build/classes -classpath .;resources/jnlp.jar

javac %JAVA_OPTS% ./risk/engine/translation/*.java
javac %JAVA_OPTS% ./risk/engine/core/*.java
javac %JAVA_OPTS% ./risk/engine/ai/*.java
javac %JAVA_OPTS% ./risk/engine/*.java
javac %JAVA_OPTS% ./risk/engine/guishared/*.java

javac %JAVA_OPTS% ./risk/ui/CommandLine/*.java
javac %JAVA_OPTS% ./risk/ui/FlashGUI/*.java
javac %JAVA_OPTS% ./risk/ui/Increment1GUI/*.java
javac %JAVA_OPTS% ./risk/ui/SimpleGUI/*.java
javac %JAVA_OPTS% ./risk/ui/SwingGUI/*.java

IF EXIST LobbyClient.jar javac -source 1.4 -target 1.4 -d build/lobby/clientclasses -classpath LobbyClient.jar;resources/jnlp.jar;. ./risk/lobby/client/*.java
IF EXIST LobbyServer.jar javac -source 1.5 -target 1.5 -d build/lobby/serverclasses -classpath LobbyServer.jar;resources/jnlp.jar;. ./risk/lobby/server/*.java

pause
