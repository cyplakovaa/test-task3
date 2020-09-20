package com.haulmont.testtask;

import com.haulmont.testtask.ui.DoctorsTableView;
import com.haulmont.testtask.ui.PatientsTableView;
import com.haulmont.testtask.ui.RecipesTableView;
import com.haulmont.testtask.ui.StartPageView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)

public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {


        String title = "Наша поликлиника";
        getPage().setTitle(title);

        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout headerLayout = new HorizontalLayout();

        Button patientButton = new Button("Пациенты", clickEvent -> getNavigator().navigateTo("PatientsTableView"));
        Button doctorButton = new Button("Доктора", clickEvent -> getNavigator().navigateTo("DoctorsTableView"));
        Button recipeButton = new Button("Рецепты", clickEvent -> getNavigator().navigateTo("RecipesTableView"));

        Label header = new Label(String.format("<font size = \"5\" color = \"grey\">" + title), ContentMode.HTML);

        headerLayout.addComponents(patientButton, doctorButton, recipeButton, header);
        headerLayout.setComponentAlignment(header, Alignment.MIDDLE_RIGHT);

        VerticalLayout viewsLayout = new VerticalLayout();

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(viewsLayout);
        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView("PatientsTableView", new PatientsTableView());
        navigator.addView("DoctorsTableView", new DoctorsTableView());
        navigator.addView("RecipesTableView", new RecipesTableView());
        navigator.addView("", new StartPageView());

        mainLayout.addComponents(headerLayout, viewsLayout);

        setContent(mainLayout);
    }
}