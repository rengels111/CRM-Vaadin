package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.services.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PageTitle("Contacts | Engels IT")
@Route(value = "", layout = MainLayout.class)  // Layout Parameter hinzugefügt. (Parent: MainLayout)
@PermitAll  // Zugriff für Benutzer nach Login erlauben.
public class ListView extends VerticalLayout {  // Alle Kind-Komponenten vertikal anordnen

    Grid<Contact> grid = new Grid<>(Contact.class);  // Neues Raster für Kontakte
    TextField filterText = new TextField();  // Textfeld zum Filtern
    ContactForm form;  // Wir integrieren das Formular.
    private final CrmService crmService;

    public ListView(CrmService crmService) {  // Constructor-Injection für CrmService.
        this.crmService = crmService;

        addClassName("list-view");  // Einen CSS-Namen vergeben, um später auf CSS zuzugreifen.
        setSizeFull();  // Volle Browsergröße verwenden.

        configureGrid();  // Methode zur Konfiguration des Rasters.
        configureForm();  // Methode zur Konfiguration des Formulars.

        add(getToolbar(), getContent());  // Symbolleiste und Content einfügen.

        updateList();  // Liste mit crmService.findAllContacts aktualisieren (wird per JPA Database abgerufen)
        closeEditor();  // Wir schließen das Formular, machen es zum Start unsichtbar.
    }

    // Kontakt-Editor schließen:
    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(crmService.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);  // Raster + Formular einfügen.
        content.setFlexGrow(2, grid);  // Raster soll die doppelte Größe des Formulars haben.
        content.setFlexGrow(1, form);
        content.addClassName("content");  // Name für CSS.
        content.setSizeFull();  // Volle Browsergröße.

        return content;
    }

    private void configureForm() {
        // Da noch keine Datenbank mit companies, statuses verbunden ist, erstmal mit leeren Listen füllen:
        form = new ContactForm(crmService.findAllCompanies(), crmService.findAllStatuses());  // mit service... gefüllt.
        form.setWidth("25em");  // Breite festlegen.
        form.addSaveListener(this::saveContact);  // Listener nutzt die SaveEvent Klasse aus ContactForm.
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent saveEvent) {
        crmService.saveContact(saveEvent.getContact());  // Kontakt wird mit saveContact gespeichert.
        updateList();  // Die Liste wird aktualisiert.
        closeEditor();  // Der Editor wird geschlossen.
    }

    private void deleteContact(ContactForm.DeleteEvent deleteEvent) {  // wie saveContact...
        crmService.deleteContact(deleteEvent.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {  // Symbolleiste
        filterText.setPlaceholder("Filter by name...");  // Platzhalter für Textfeld.
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);  // Es soll nicht in Echtzeit gefiltert werden, sondern
        // eine gewisse Zeit auf weitere Eingabe gewartet werden.
        filterText.addValueChangeListener(e -> updateList());  // Toolbar wird geupdated.

        Button addContactButton = new Button("Add contact");  // Button um Kontakte hinzuzufügen.
        addContactButton.addClickListener(e -> addContact());  // Listener für add Contact-Button.

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);  // Komponenten horizontal.
        toolbar.addClassName("toolbar");  // Name für CSS.

        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();  // Wenn auf den Button geklickt wird, sollte kein Kontakt ausgewählt bleiben,
        // daher wird das bereinigt.
        editContact(new Contact());  // Neuer Kontakt wird hinzugefügt.
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");  // Namen einfügen, für späteren CSS Eingriff.
        grid.setSizeFull();  // Ganzen möglichen Browser-Bildschirm verwenden.
        grid.setColumns("firstName", "lastName", "email");  // Spalten bestimmen.
        // Da wir Objekte haben und nicht nur Strings übergeben wollen, erstellen wir den Zugriff auf die Objekte:
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));  // Spaltengröße automatisch anpassen.

        // Wenn ein Kontakt ausgewählt wird, soll er editiert werden können
        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));
    }

    private void editContact(Contact contact) {
        if(contact == null) {  // wenn kein Kontakt ausgewählt ist, soll der Editor geschlossen werden.
            closeEditor();
        } else {
            form.setContact(contact);  // Kontakt-Editor wieder sichtbar.
            form.setVisible(true);
            addClassName("editing");
        }
    }
}