package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

// Klasse, um Kontakte in einem Formular zu bearbeiten.
public class ContactForm extends FormLayout {  // FormLayout verfügt sinnvolle Vaadin FormularLayout Optionen.#


    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Company> company = new ComboBox<>("Company");  // Auswahl-Box Firma
    ComboBox<Status> status = new ComboBox<>("Status");  // Auswahl-Box Status

    // Buttons kreieren:
    Button save = new Button(("Save"));
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);  // Binder erstellen und Eingabefelder verbinden.



    public ContactForm(List<Company> companies, List<Status> statuses) {  // Companies und statuses werden übergeben.
        addClassName("contact-form");  // Name für CSS
        binder.bindInstanceFields(this);  // Methode gleicht Felder in Contact und ContactForm ab.

        company.setItems(companies);  // die companies werden der Company ComboBox zugeordnet.
        company.setItemLabelGenerator(Company::getName);  // Label wird automatisch erzeugt.
        status.setItems(statuses);  // die statuses werden der Status ComboBox zugeordnet.
        status.setItemLabelGenerator(Status::getName);

        add(
                firstName,
                lastName,
                email,
                company,
                status,
                createButtonLayout()
        );

    }

    // Setter-Injection für Contacts
    public void setContact(Contact contact) {
        binder.setBean(contact);  // Binder ruft setBean() auf, um die Werte von
        // Contact.class an die UI-Felder zu binden.
    }

    private Component createButtonLayout() {
        // Unterscheidet die Schaltflächen mithilfe integrierter Designvarianten optisch voneinander:
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Tastatur-Shortcuts hinzufügen:
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        // Der Speicher-Button ruft die validateAndSave()-Methode auf:
        save.addClickListener(event -> validateAndSave());
        // Löschen-Button:
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        // Schließen-Button:
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));  // Validiert das Formular jedes Mal,
        // wenn es sich ändert.
        return new HorizontalLayout(save, delete, close);
    }

    // Speicher-Event wird ausgelöst, sodass die Eltern-Klasse die Aktion ausführen kann:
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    // Events
    // Wir erstellen eine Basis-Klasse "ContactFormEvent" für die Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;  // Contact-Variable, um Änderungen in Contact darin zu speichern.

        protected ContactFormEvent(ContactForm source, Contact contact) { // (1)
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    // Events für Speichern, Löschen und Schließen erstellen:
    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    // Hier kommen die add*Listener()-Methoden, die den typisierten Event-Typ an den Event-Bus von Vaadin übergeben,
    // um die benutzerdefinierten Ereignistypen zu registrieren.
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { // (2)
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}
