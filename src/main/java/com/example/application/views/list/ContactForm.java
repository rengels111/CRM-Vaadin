package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
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

import java.util.List;

// Klasse, um Kontakte in einem Formular zu bearbeiten.
public class ContactForm extends FormLayout {  // FormLayout verfügt sinnvolle Vaadin FormularLayout Optionen.#

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);  // Binder erstellen und Eingabefelder verbinden.

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Company> company = new ComboBox<>("Company");  // Auswahl-Box Firma
    ComboBox<Status> status = new ComboBox<>("Status");  // Auswahl-Box Status

    // Buttons kreieren:
    Button save = new Button(("Save"));
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Contact contact;


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

    public void setContact(Contact contact) {  // Setter-Injection für Contacts.
        this.contact = contact;
        binder.readBean(contact);  // Binder liest per "readBean" diese Bean.
    }

    private Component createButtonLayout() {
        // Unterscheidet die Schaltflächen mithilfe integrierter Designvarianten optisch voneinander:
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Tastatur-Shortcuts hinzufügen:
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

}
