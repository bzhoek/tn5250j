package com.hoek.tn5250;

import org.apache.log4j.Logger;
import org.tn5250j.Session5250;
import org.tn5250j.framework.tn5250.Screen5250;

import java.util.ArrayList;
import java.util.List;

public class ScreenContent {

    Logger LOG = Logger.getLogger(ScreenContent.class);

    private final Screen5250 screen;
    private final char[] chars;
    private final String contents;

    public ScreenContent(Session5250 session) {
        this.screen = session.getScreen();
        this.chars = screen.getCharacters();
        this.contents = new String(chars);
    }

    public void dumpScreen() {
        for (String line : getLines()) {
            LOG.info(line);
        }
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<String>(screen.getRows());
        for (int row = 0; row < screen.getRows(); row++) {
            lines.add(getLine(row));
        }
        return lines;
    }

    public String getLine(int row) {
        return String.copyValueOf(chars, row * screen.getColumns(), screen.getColumns()).trim();
    }

    public int length() {
        return chars.length;
    }

    public int indexOf(String text) {
        if (!contents.contains(text)) {
            throw new IllegalStateException(String.format("Could not find text %s on screen", text));
        }
        return contents.indexOf(text);
    }

    public int lineOf(String text) {
        List<String> lines = getLines();
        for (int row = 0; row < lines.size(); row++) {
            if (lines.get(row).contains(text)) {
                return row;
            }
        }
        throw new IllegalStateException(String.format("Could not find row with text %s on screen", text));
    }

}
