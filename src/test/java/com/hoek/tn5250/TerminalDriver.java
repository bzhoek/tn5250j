package com.hoek.tn5250;

import org.apache.log4j.Logger;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.event.ScreenOIAListener;
import org.tn5250j.event.SessionChangeEvent;
import org.tn5250j.event.SessionListener;
import org.tn5250j.framework.common.SessionManager;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenOIA;

import java.util.Properties;

public class TerminalDriver {

    Logger LOG = Logger.getLogger(TerminalDriver.class);

    private final Properties properties;
    private Session5250 session;
    private boolean connected;
    private boolean informed;
    private int informChange;
    private Screen5250 screen;

    public TerminalDriver(String host, String port) {
        properties = new Properties();
        properties.put(TN5250jConstants.SESSION_HOST, host);
        properties.put(TN5250jConstants.SESSION_HOST_PORT, port);
        properties.put(TN5250jConstants.SESSION_CODE_PAGE, "37");
    }

    public TerminalDriver connect() throws InterruptedException {
        createSession();
        session.connect();
        while (!connected) {
            Thread.sleep(100);
        }
        screen = session.getScreen();
        addOperatorInformationAreaListener();
        return this;
    }

    private void createSession() {
        session = SessionManager.instance().openSession(properties, null, null);
        session.addSessionListener(new SessionListener() {
            @Override
            public void onSessionChanged(SessionChangeEvent changeEvent) {
                connected = changeEvent.getState() == TN5250jConstants.STATE_CONNECTED;
            }
        });
    }

    private void addOperatorInformationAreaListener() {
        session.getScreen().getOIA().addOIAListener(new ScreenOIAListener() {
            @Override
            public void onOIAChanged(ScreenOIA oia, int change) {
                LOG.debug(String.format("OIA %d", change));
                informed = informed | informChange == change;
            }
        });
    }

    public void waitForUnlock() {
        informed = false;
        informChange = ScreenOIAListener.OIA_CHANGED_KEYBOARD_LOCKED;
        while (!informed) {
            Thread.yield();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public TerminalDriver sendKeys(String keys) {
        screen.sendKeys(keys);
        return this;
    }

    public TerminalDriver fillCurrentField(String text) {
        screen.getScreenFields().getCurrentField().setString(text);
        return this;
    }

    public void dumpScreen() {
        screen.dumpScreen();
    }
}