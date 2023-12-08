package com.example.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// Ich erstelle einen Service für den Zugriff der angemeldeten Benutzer auf Informationen und für die Abmeldung
// des Benutzers:
@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {  // Auth.Context wird autowired.
        this.authenticationContext = authenticationContext;
    }

    public UserDetails getAuthenticatedUser() {  // authentifizierten User erhalten.
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    public void logout() {  // User ausloggen.
        authenticationContext.logout();
    }
}
