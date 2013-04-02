package com.hoek.tn5250;


import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Tn5250TestIntegration {

    private String username;
    private String password;

    @Before
    public void setup() {
        username = System.getProperty("username");
        password = System.getProperty("password");
        assertNotNull("-Dusername= not specified", username);
        assertNotNull("-Dpassword= not specified", password);
    }

    Logger LOG = Logger.getLogger(Tn5250TestIntegration.class);

    @Test
    public void should_connect_with_pub1() throws InterruptedException {
        TerminalDriver driver = new TerminalDriver("localhost", "2023").connect();
//        TerminalDriver driver = new TerminalDriver("pub1.rzkh.de", "23").connect();
        assertTrue(driver.isConnected());
        driver.fillCurrentField(username).sendKeys("[tab]");
        driver.fillCurrentField(password);
        driver.sendKeys("[enter]").waitForUnlock();
        driver.dumpScreen();
        driver.sendKeys("[enter]").waitForUnlock();
        driver.dumpScreen();
    }
}
