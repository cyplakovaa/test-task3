package com.haulmont.testtask.ui;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;


public class StartPageView extends VerticalLayout implements View {


    public StartPageView(){
        buildStartPageView();
    }

    public void buildStartPageView() {

        VerticalLayout welcomeLayout = new VerticalLayout();
        welcomeLayout.setSizeFull();
        Label welcomeLabel = new Label(String.format("<font size = \"15\" color = \"grey\"> Добро пожаловать на сайт поликлиники!"), ContentMode.HTML);
        Label descriptionLabel = new Label(String.format("<font size = \"3\" color = \"grey\"> " +
                "На сайте Вы можете: <ul> " +
                "<li> Ознакомиться со списком пациентов, докторов, рецептов;</li> " +
                "<li> Добавлять, корректировать, удалять соответствующие списки; </li> " +
                "<li> Просмотривать статистику докторов по количеству выписанных рецептов; </li>" +
                "<li> Фильтровать данные по ФИО пациента, описанию рецепта, его приоритету. </li></ul> " +
                "Удачного просмотра!")
                , ContentMode.HTML);

        welcomeLayout.addComponents(welcomeLabel, descriptionLabel);
        welcomeLayout.setComponentAlignment(welcomeLabel, Alignment.MIDDLE_CENTER);
        welcomeLayout.setComponentAlignment(descriptionLabel, Alignment.BOTTOM_LEFT);

        addComponent(welcomeLayout);
    }
}
