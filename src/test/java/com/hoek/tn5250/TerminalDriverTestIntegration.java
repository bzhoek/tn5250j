package com.hoek.tn5250;


import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TerminalDriverTestIntegration {

    private String username;
    private String password;

    @Before
    public void setup() {
        username = System.getProperty("username");
        password = System.getProperty("password");
        assertNotNull("-Dusername= not specified", username);
        assertNotNull("-Dpassword= not specified", password);
    }

    Logger LOG = Logger.getLogger(TerminalDriverTestIntegration.class);

    @Test
    public void should_connect_with_pub1() throws InterruptedException {
//        TerminalDriver driver = new TerminalDriver("localhost", "2023").connect();
        TerminalDriver driver = new TerminalDriver("pub1.rzkh.de", "23").connect();
        assertTrue(driver.isConnected());
        driver.fillFieldWith("Benutzer", username);
        driver.fillFieldWith("Password", password);
        driver.sendEnter();
        driver.assertScreen("Sign-on Information");
        driver.sendEnter();
        driver.assertScreen("GUEST menu");
        driver.sendCommand("WRKQRY");
        driver.fillFieldWith("Option", "1").sendEnter();
        driver.sendEnter();
        driver.fillFieldWith("File", "BLA").sendEnter().sendEnter();
        driver.select("Define result fields", "1").sendEnter();
        driver.sendKeys("[pf5]").waitForUnlock();
        driver.lastLine();
    }
}
