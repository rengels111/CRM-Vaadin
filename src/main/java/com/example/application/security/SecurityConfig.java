package com.example.application.security;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity  // Spring Security aktivieren
@Configuration  // Konfigurationsklasse, Bean Factory
// Erweitern um VaadinWebSecurity-Klasse, um Spring Security für Vaadin zu konfigurieren:
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // der Zugriff auf den Pfad /images/* soll von Spring Security autorisiert werden:
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll());
        super.configure(http);  // Http Adapter werden konfiguriert.
        setLoginView(http, LoginView.class);  // Zugriff auf LoginView erlauben, um eigenes Login-Formular zu nutzen.
    }

    // Benutzer konfigurieren:
    @Bean
    public UserDetailsService user() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}password")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);  // In-Memory-Benutzer konfigurieren - nur für Testzwecke!

        /*
        WICHTIG: Verwende niemals fest codierte Anmeldeinformationen in der Produktion!!!
        Du kannst die Spring Security-Konfiguration ändern, um einen Authentifizierungsanbieter für Lightweight
        Directory Access Protocol (LDAP), Java Authentication und Authorization Service (JAAS) und andere reale
        Quellen zu verwenden.
        Erfahre mehr über Spring Security Authentifizierungsanbieter unter:
        https://dzone.com/articles/spring-security-authentication
         */
    }
}
