package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// SpringBootTest: Spring Boot helpers starten einen Webserver für den Test. Ein random Port wird verwendet, sodass es während
// der Ausführung nicht zu Konflikten mit einem laufenden Entwicklungsserver kommt:
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// BrowerTestBase: diese Super-Klasse stellt praktische Hilfsmethoden bereit und konfiguriert TestBench:
public class LoginE2ETest extends BrowserTestBase {

    @Autowired
    Environment env;

    static {
        // Verhindern, dass der Vaadin-Entwicklungsmodus das Browserfenster startet:
        System.setProperty("vaadin.launch-browser", "false");
    }

    @BeforeEach  // Methode sollte vor jedem Test durchgeführt werden.
    // Die URL weist auf einen lokalen Test-Server mit einem zufälligen Port. Der Browser sollte automatisch auf das
    // Login Fenster weiterleiten:
    void openBrowser() {
        getDriver().get("http://localhost:" + env.getProperty("local.server.port") + "/");
    }

    @BrowserTest  // Das ist eine Erweiterung der @TestBench Annotation. Diese ist nützlich, wenn man sich entscheidet
    // die End-to-Ent-Tests irgendwann auf mehrere Browser auszudehnen.
    public void loginAsValidUserSucceeds() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        assertTrue(loginView.login("user", "password"));
    }

    @BrowserTest
    public void loginAsInvalidUserFails() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        assertFalse(loginView.login("user", "test"));
    }

}
