package com.example.application.views;

import com.example.application.data.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Engels IT")
@PermitAll  // Zugriff für Benutzer nach Login erlauben.
public class DashboardView extends VerticalLayout {

    private final CrmService crmService;

    public DashboardView(CrmService crmService) {  // CrmService per Constructor-Injection
        this.crmService = crmService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);  // Zentriert den Inhalt mittig.
        add(getContactStats(), getCompaniesChart());
    }

    private Component getContactStats() {
        // Führt Methode countContacts() aus, um die Anzahl der Kontakte wiederzugeben:
        Span stats = new Span(crmService.countContacts() + " contacts");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);  // Chart hinzufügen

        DataSeries dataSeries = new DataSeries();
        crmService.findAllCompanies().forEach(company ->
                dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount())));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
}
