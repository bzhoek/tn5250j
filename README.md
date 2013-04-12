# tn5250j

Meant to simplify Functional Integration Tests with [FitNesse](fitnesse.org) on systems that handle 5250 terminals.
![Image](/doc/rkzh.jpg)

## Usage

The Maven `pom.xml` takes care of running FitNesse with the correct classpath.

    $ mvn integration-test -Dusername={username} -Dpassword={password}

Use `com.hoek.tn5250.TerminalDriver` to driver terminal screens.

## Origin

Source from 0.7.0 pulled from [SourceForge](http://tn5250j.sourceforge.net), and converted to Maven project.

## Issues

`DataStreamProducer` does not handle doubling IAC (0xFF, Interpret As Command) bytes properly.

    FAIL 0000:  03 ff ff 12 a0 00 00 04
    ...
    GOOD 0000:  00 11 12 a0 00 00 04 00
