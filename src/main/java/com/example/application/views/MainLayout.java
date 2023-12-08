package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

// Das MainLayout zeigt die Navigation und die obere Leiste und die integrierte ListView an.
public class MainLayout extends AppLayout { // AppLayout ist ein Vaadin-Layout mit einer Kopfzeile und einem responsiven
    // Fach.

    // SecurityService autowire mit Constructor-Injection:
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();  // Header
        createDrawer();  // Fach
    }

    private void createHeader() {
        H1 logo = new H1("Engels IT CRM");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);  // Lumo Utility Classes für CSS

        // Logout Button erstellen, der die Logout-Methode im SecurityService aufruft:
        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);  // DrawerToggle ist eine
        // Menüschaltfläche, die die Sichtbarkeit der Seitenleiste umschaltet.

        // Zentriert die Komponenten im Header an der vertikalen Achse:
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        // Fügt das Header-Layout zur Navigationsleiste des App-Layouts hinzu:
        addToNavbar(header);
    }

    private void createDrawer() {
        // Erstellt einen RouterLink mit dem Text "List" und ListView.clas als Zielansicht.
        RouterLink listView = new RouterLink("List", ListView.class);
        // Neuer RouterLink zu Dashboard.class:
        RouterLink dashboardView = new RouterLink("Dashboard", DashboardView.class);
        // Hervorhebung des Elements (mittlerweile automatisch bei Vaadin?):
        listView.setHighlightCondition(HighlightConditions.sameLocation());  // CSS hinzugefügt in styles.css
        // Fügt den Router-Link in ein VertikalLayout ein und fügt ihn der AppLayout-Schublade hinzu:
        addToDrawer(new VerticalLayout(
                listView,
                dashboardView));
    }
}
