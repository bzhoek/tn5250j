package com.hoek.tn5250;


import org.apache.log4j.Logger;

public class Helper {

    private static final Logger LOG = Logger.getLogger(Helper.class);

    public static void dumpBytes(byte[] block) {
        StringBuffer sb = new StringBuffer(String.format("Frame length %d", block.length));

        for (int i = 0; i < block.length; i++) {
            if (i % 32 == 0) {
                LOG.info(sb);
                sb = new StringBuffer(String.format("%04x: ", i / 32 * 32));
            }
            if (i % 8 == 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02x ", block[i] & 0xFF));
        }
        if (block.length % 32 > 0) {
            LOG.info(sb);
        }
    }

}
