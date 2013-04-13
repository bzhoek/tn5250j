package com.hoek.tn5250;

import org.apache.commons.lang.StringUtils;

public class TerminalFixture {

    private final TerminalDriver driver;

    public TerminalFixture(String host, String port) {
        driver = new TerminalDriver(host, port);
    }

    public boolean connect() throws InterruptedException {
        driver.connect();
        return driver.isConnected();
    }

    public boolean loginWithUsernameAndPassword(String username, String password) {
        driver.fillCurrentField(username).sendKeys("[tab]");
        driver.fillCurrentField(password);
        driver.sendKeys("[enter]").waitForUnlock();
        return true;
    }

    public String screenName() {
        return driver.getScreenContent().getLine(0).trim();
    }

    public boolean enter() {
        return enter(1);
    }

    public boolean enter(int count) {
        for (int i = 0; i < count; i++) {
            driver.sendEnter();
        }
        return true;
    }

    public boolean hitWait(String key) {
        driver.sendKeys(String.format("[%s]", key)).waitForUnlock();
        return true;
    }

    public boolean command(String command) {
        driver.sendCommand(command);
        return true;
    }

    public boolean fillWith(String label, String value) {
        driver.fillFieldWith(label, value);
        return true;
    }

    public boolean selectWith(String label, String value) {
        driver.select(label, value);
        return true;
    }

    public String screenContents() {
        StringBuffer sb = new StringBuffer();
        for (String line : driver.getScreenContent().getLines()) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    public String lastReportLine() {
        return driver.lastReportLine().replaceAll("[\\x00-\\x1F]", " ");
    }

}
