# tn5250j

Source from 0.7.0 pulled from (SourceForge)[http://tn5250j.sourceforge.net], and converted to Maven project.

`DataStreamProducer` does not handle doubling IAC (0xFF, Interpret As Command) bytes properly.

    0000:  03 ff ff 12 a0 00 00 04
    ...
    0000:  00 11 12 a0 00 00 04 00
