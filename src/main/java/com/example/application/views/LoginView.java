package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")  // Ansicht dem Login-Pfad zuordnen.
@PageTitle("Login | Engels IT")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();  // LoginForm instanziieren, um Nutzer und Passwort zu erfassen.

    public LoginView() {
        addClassName("login-view");
        // Volle Browsergröße
        setSizeFull();
        // Mittig zentrieren, vertikal und horizontal:
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // LoginForm-Aktion auf "login" setzen, um das Anmeldeformular an Spring Security zu senden:
        login.setAction("login");

        add(new H1("Engels IT"), login);  // Überschrift und Login-Formular setzen.
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Abfrageparameter lesen und einen Fehler anzeigen, wenn ein Anmeldeversuch fehlschlägt.
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
                    login.setError(true);
        }
    }
}
