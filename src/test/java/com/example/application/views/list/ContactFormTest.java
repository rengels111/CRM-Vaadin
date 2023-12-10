package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Unit-Tests für einfache UI Logik:
public class ContactFormTest {
    private List<Company> companies;
    private List<Status> statuses;
    private Contact danielBerg;
    private Company company1;
    private Company company2;
    private Status status1;
    private Status status2;

    @BeforeEach  // Diese Annotation sorgt dafür, dass diese Methode vor jedem Test durchgeführt wird.
    // Diese Methode fügt Dummy Daten für Tests ein.
    public void setupData() {
        // Zwei companies werden zum Test hinzugefügt:
        companies = new ArrayList<>();
        company1 = new Company();
        company1.setName("Engels IT GmbH");
        company2 = new Company();
        company2.setName("Seed and Growth Ltd");
        companies.add(company1);
        companies.add(company2);

        // 2 Status werden hinzugefügt:
        statuses = new ArrayList<>();
        status1 = new Status();
        status1.setName("Status 1");
        status2 = new Status();
        status2.setName("Status 2");
        statuses.add(status1);
        statuses.add(status2);

        // Ein Kontakt wird mit Daten hinzugefügt:
        danielBerg = new Contact();
        danielBerg.setFirstName("Daniel");
        danielBerg.setLastName("Berg");
        danielBerg.setEmail("daniel.berg@gmail.com");
        danielBerg.setStatus(status1);
        danielBerg.setCompany(company2);
    }

    @Test  // Test Annotation wichtig.
    // Ein Formular wird für Testzwecke erstellt und mit dem Kontakt gefüllt.
    public void formFieldsPopulated() {
        ContactForm form = new ContactForm(companies, statuses);
        form.setContact(danielBerg);

        // Standard JUnit Methode assertEquals wird verwendet, um Daten abzugleichen:
        assertEquals("Daniel", form.firstName.getValue());
        assertEquals("Berg", form.lastName.getValue());
        assertEquals("daniel.berg@gmail.com", form.email.getValue());
        assertEquals(company2, form.company.getValue());
        assertEquals(status1, form.status.getValue());
    }

    // Die Speicherfunktion testen:
    @Test
    public void saveEventHasCorrectValues() {
        // Formular instanziieren:
        ContactForm form = new ContactForm(companies, statuses);
        // Neuen Kontakt erstellen:
        Contact contact = new Contact();
        // Kontakt mit Daten füllen:
        form.setContact(contact);
        form.firstName.setValue("Turgay");
        form.lastName.setValue("Ata");
        form.email.setValue("t.ata@swf-fh.de");
        form.company.setValue(company1);
        form.status.setValue(status2);

        // Den gespeicherten Kontakt in einer AtomicReference speichern:
        AtomicReference<Contact> savedContactRef = new AtomicReference<>(null);
        form.addSaveListener(e -> {
            savedContactRef.set(e.getContact());
        });
        form.save.click();  // Button save klicken und den gespeicherten Kontakt lesen.
        Contact savedContact = savedContactRef.get();

        // Anschließend überprüfen, ob die Bean die erwarteten Werte enthält:
        assertEquals("Turgay", savedContact.getFirstName());
        assertEquals("Ata", savedContact.getLastName());
        assertEquals("t.ata@swf-fh.de", savedContact.getEmail());
        assertEquals(company1, savedContact.getCompany());
        assertEquals(status2, savedContact.getStatus());



    }
}