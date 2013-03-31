package com.hoek.tn5250;


import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.event.ScreenOIAListener;
import org.tn5250j.event.SessionChangeEvent;
import org.tn5250j.event.SessionListener;
import org.tn5250j.framework.common.SessionManager;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenOIA;

import java.util.Properties;

import static junit.framework.Assert.assertTrue;

public class Tn5250TestIntegration {

    private String username;
    private String password;

    @Before
    public void setup() {
        username = System.getProperty("username");
        password = System.getProperty("password");
        Assert.assertNotNull("-Dusername= not specified", username);
        Assert.assertNotNull("-Dpassword= not specified", password);
    }

    public class Tn5250Driver {

        private final Properties properties;
        private Session5250 session;
        private boolean connected;
        private boolean informed;
        private int informChange;
        private Screen5250 screen;

        public Tn5250Driver(String host, String port) {
            properties = new Properties();
            properties.put(TN5250jConstants.SESSION_HOST, host);
            properties.put(TN5250jConstants.SESSION_HOST_PORT, port);
            properties.put(TN5250jConstants.SESSION_CODE_PAGE, "37");
        }

        public Tn5250Driver connect() throws InterruptedException {
            createSession();
            session.connect();
            while (!connected) {
                Thread.yield();
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

        public Tn5250Driver sendKeys(String keys) {
            screen.sendKeys(keys);
            return this;
        }

        public Tn5250Driver fillCurrentField(String text) {
            screen.getScreenFields().getCurrentField().setString(text);
            return this;
        }

        public void dumpScreen() {
            screen.dumpScreen();
        }
    }

    Logger LOG = Logger.getLogger(Tn5250TestIntegration.class);

    @Test
    public void should_connect_with_pub1() throws InterruptedException {
        Tn5250Driver driver = new Tn5250Driver("pub1.rzkh.de", "23").connect();
        assertTrue(driver.isConnected());
        driver.fillCurrentField(username).sendKeys("[tab]");
        driver.fillCurrentField(password);
        driver.sendKeys("[enter]").waitForUnlock();
        driver.dumpScreen();
        driver.sendKeys("[enter]").waitForUnlock();
        driver.dumpScreen();
    }
}
