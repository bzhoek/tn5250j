# tn5250j

Meant to simplify Functional Integration Tests with [FitNesse](fitnesse.org) on systems that handle 5250 terminals.
![Image](/doc/rkzh.jpg)

## Client

    $ java -jar target/tn5250j.slim-1.0-SNAPSHOT.jar

## FitNesse

The Maven `pom.xml` takes care of running FitNesse with the correct classpath.

    $ mvn -P fitnesse integration-test -Dusername={username} -Dpassword={password}

Use `com.hoek.tn5250.TerminalDriver` to drive terminal screens.

## Origin

Source from 0.7.0 pulled from [SourceForge](http://tn5250j.sourceforge.net), and converted to Maven project.

## Issues

`DataStreamProducer` does not handle doubling IAC (0xFF, Interpret As Command) bytes properly.

    FAIL 0000:  03 ff ff 12 a0 00 00 04
    ...
    GOOD 0000:  00 11 12 a0 00 00 04 00
