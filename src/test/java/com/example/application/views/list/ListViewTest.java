package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

// Integrationstests für fortgeschrittene UI Logik:
// Um eine Klasse zu testen, die @Autowired, eine Datenbank oder eine andere von Spring Boot bereitgestellte Funktion
// verwendet, kann man nicht länger einfache JUnit-Tests verwenden. Wir verwenden stattdessen den Spring Boot Test Runner.
// Dies bedeutet zwar einen gewissen Mehraufwand, macht aber mehr Funktionen für den Test verfügbar.
@RunWith(SpringRunner.class)
// Die Annotation @SpringBootTest stellt sicher, dass die Spring Boot-Anwendung initialisiert wird, bevor die Tests
// ausgeführt werden, und ermöglicht uns die Verwendung der Annotation @Autowired im Test.
@SpringBootTest
public class ListViewTest {

    @Autowired
    private ListView listView;

    public ListViewTest(ListView listView) {
        this.listView = listView;
    }

    @Test
    public void formShownWhenContactSelected() {
        Grid<Contact> grid = listView.grid;
        Contact firstContact = getFirstItem(grid);

        ContactForm form = listView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstContact);

        assertTrue(form.isVisible());
        assertEquals(firstContact.getFirstName(), form.firstName.getValue());
    }

    private Contact getFirstItem(Grid<Contact> grid) {
        return( (ListDataProvider<Contact>) grid.getDataProvider()).getItems().iterator().next();
    }

}
