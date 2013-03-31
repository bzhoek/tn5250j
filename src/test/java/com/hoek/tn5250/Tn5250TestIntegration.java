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
import static junit.framework.TestCase.assertNotNull;

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

        public Tn5250Driver(String host, String port) {
            properties = new Properties();
            properties.put(TN5250jConstants.SESSION_HOST, host);
            properties.put(TN5250jConstants.SESSION_HOST_PORT, port);
            properties.put(TN5250jConstants.SESSION_CODE_PAGE, "37");
        }

        public void connect() {
            SessionManager sessionManager = SessionManager.instance();
            session = sessionManager.openSession(properties, null, null);

        }

    }

    Logger LOG = Logger.getLogger(Tn5250TestIntegration.class);

    @Test
    public void should_connect_with_pub1() throws InterruptedException {
        SessionManager sessionManager = SessionManager.instance();
        Properties props = new Properties();
        props.put(TN5250jConstants.SESSION_HOST, "pub1.rzkh.de");
        props.put(TN5250jConstants.SESSION_HOST_PORT, "23");
        props.put(TN5250jConstants.SESSION_CODE_PAGE, "37");

        Session5250 session = sessionManager.openSession(props, null, null);
        assertNotNull(session);
        session.addSessionListener(new SessionListener() {
            @Override
            public void onSessionChanged(SessionChangeEvent changeEvent) {
                LOG.info(changeEvent.getMessage());
            }
        });
        session.connect();
        Thread.sleep(3000);
        assertTrue(session.isConnected());
        Screen5250 screen = session.getScreen();
        screen.getOIA().addOIAListener(new ScreenOIAListener() {
            @Override
            public void onOIAChanged(ScreenOIA oia, int change) {
                LOG.info(change);
            }
        });
        screen.getScreenFields().getCurrentField().setString(username);
        screen.sendKeys("[tab]");
        screen.getScreenFields().getCurrentField().setString(password);
        screen.sendKeys("[enter]");
        LOG.info("ENTER");
        Thread.sleep(3000);
        screen.sendKeys("[enter]");
        Thread.sleep(3000);
        screen.dumpScreen();
    }
}
