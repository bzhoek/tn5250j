package com.hoek.tn5250;

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
        driver.sendEnter();
        return true;
    }

    public boolean command(String command) {
        driver.sendCommand(command);
        return true;
    }

    public boolean fillWith(String field, String value) {
        driver.fillFieldWith(field, value);
        return true;
    }

}
