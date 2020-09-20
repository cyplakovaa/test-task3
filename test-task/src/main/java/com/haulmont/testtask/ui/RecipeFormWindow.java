package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.PriorityOfRecipe;
import com.haulmont.testtask.entity.Recipe;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.service.PatientService;
import com.haulmont.testtask.service.PriorityOfRecipeService;
import com.haulmont.testtask.service.RecipeService;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

public class RecipeFormWindow extends Window{
    private Recipe recipe;
    private Binder<Recipe> binder = new Binder<>(Recipe.class);
    private Grid<Recipe> recipeGrid;
    private int init;

    public RecipeFormWindow(Grid<Recipe> grid, int i) {
        this.recipeGrid = grid;
        this.init = i;
        buildRecipeWindow();
    }

    public void buildRecipeWindow(){

        VerticalLayout mainRecipeFormWindowLayout = new VerticalLayout();

        TextField description = new TextField("Описание");
        binder.forField(description).withValidator(d -> d != null && !d.isEmpty(), "Введите описание").bind(Recipe :: getDescription, Recipe :: setDescription);

        ComboBox<Patient> patientComboBox = new ComboBox<>("Пациент");
        binder.forField(patientComboBox).withValidator(p -> p != null, "Выберите пациента").bind(Recipe::getPatient , Recipe :: setPatient);
        try {
            List<Patient> patientList = new PatientService().getAll();
            patientComboBox.setItems(patientList);
            patientComboBox.setItemCaptionGenerator(p -> p.getLastName() + " " + p.getFirstName() + " " + p.getPatronymic());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ComboBox<Doctor> doctorComboBox = new ComboBox<>("Доктор");
        binder.forField(doctorComboBox).withValidator(d -> d != null, "Выберите доктора").bind(Recipe :: getDoctor, Recipe :: setDoctor);

        List<Doctor> doctorList = null;
        try {
            doctorList = new DoctorService().getAll();
            doctorComboBox.setItems(doctorList);
            doctorComboBox.setItemCaptionGenerator(p -> p.getLastName() + " " + p.getFirstName() + " " + p.getPatronymic());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DateField creationDate = new DateField("Дата создания");
        binder.forField(creationDate).withValidator(d -> d != null, "Выберите дату").
                withConverter(new LocalDateToDateConverter(ZoneId.systemDefault())).bind(Recipe :: getCreationDate, Recipe :: setCreationDate);

        TextField validity = new TextField("Срок действия");
        binder.forField(validity).withValidator(v -> v != null, "Введите  срок действия ").withConverter(new StringToIntegerConverter("Невозможно преобразовать в int")).
                bind(Recipe :: getValidity, Recipe :: setValidity);

        ComboBox<PriorityOfRecipe> priority = new ComboBox<>("Приоритет");
        binder.forField(priority).withValidator(p -> p != null, "Выберите приоритет").bind(Recipe :: getPriority, Recipe :: setPriority);

        List<PriorityOfRecipe> priorityOfRecipeList = null;
        try {
            priorityOfRecipeList = new PriorityOfRecipeService().getAll();
            priority.setItems(priorityOfRecipeList);
            priority.setItemCaptionGenerator(PriorityOfRecipe::getPriorityName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FormLayout formRecipeLayout = new FormLayout();
        formRecipeLayout.addComponents(description, patientComboBox, doctorComboBox, creationDate, validity, priority);

        Button saveBtn = new Button("ОК", VaadinIcons.CHECK);
        Button cancelBtn = new Button("Отменить", VaadinIcons.CLOSE_SMALL);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(saveBtn, cancelBtn);

        mainRecipeFormWindowLayout.addComponents(formRecipeLayout, btnLayout);
        setContent(mainRecipeFormWindowLayout);

        if(init == 1) {
            saveBtn.addClickListener(e -> {
                if(binder.validate().isOk()) {
                    try {
                        recipe = new Recipe();
                        recipe.setDescription(description.getValue());
                        recipe.setPatient(patientComboBox.getValue());
                        recipe.setDoctor(doctorComboBox.getValue());
                        recipe.setCreationDate(Date.from(creationDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        recipe.setValidity(Integer.parseInt(validity.getValue()));
                        recipe.setPriority(priority.getValue());

                        new RecipeService().add(recipe);
                        List<Recipe> recipeList = new RecipeService().getAll();
                        recipeGrid.setItems(recipeList);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    close();
                }
            });
        }

        if(init == 2){
            recipe = recipeGrid.asSingleSelect().getValue();
            binder.setBean(recipe);

            saveBtn.addClickListener(clickEvent -> {
                if(binder.validate().isOk()) {
                    try {
                        Recipe alterableRecipe = new Recipe();
                        alterableRecipe.setDescription(description.getValue());
                        alterableRecipe.setPatient(patientComboBox.getValue());
                        alterableRecipe.setDoctor(doctorComboBox.getValue());
                        alterableRecipe.setCreationDate(Date.from(creationDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        alterableRecipe.setValidity(Integer.parseInt(validity.getValue()));
                        alterableRecipe.setPriority(priority.getValue());
                        alterableRecipe.setId(recipe.getId());

                        new RecipeService().update(alterableRecipe);
                        List<Recipe> recipeList = new RecipeService().getAll();
                        recipeGrid.setItems(recipeList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    close();
                }
            });
        }
        cancelBtn.addClickListener((e -> close()));
    }
}
