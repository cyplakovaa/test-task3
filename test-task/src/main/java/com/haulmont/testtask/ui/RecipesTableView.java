package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.PriorityOfRecipe;
import com.haulmont.testtask.entity.Recipe;
import com.haulmont.testtask.service.PatientService;
import com.haulmont.testtask.service.PriorityOfRecipeService;
import com.haulmont.testtask.service.RecipeService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;

public class RecipesTableView extends VerticalLayout implements View {

    private Grid<Recipe> recipeGrid = new Grid(Recipe.class);

    private Button addRecipeBtn;
    private Button updateRecipeBtn;
    private Button deleteRecipeBtn;
    private Button acceptBtn;

    public RecipesTableView(){
        buildRecipesTable();
    }

    public void buildRecipesTable(){

        HorizontalLayout filterLayout = new HorizontalLayout();
        Panel filterPanel = new Panel("Фильтр", filterLayout);

        ComboBox<Patient> patientComboBox = new ComboBox<>("Пациент");
        try {
            List<Patient> patientList = new PatientService().getAll();
            patientComboBox.setItems(patientList);
            patientComboBox.setItemCaptionGenerator(p -> p.getFirstName() + " " + p.getLastName() + " " + p.getPatronymic());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ComboBox<PriorityOfRecipe> priorityOfRecipeComboBox = new ComboBox<>("Приоритет");
        try {
            List<PriorityOfRecipe> priorityOfRecipeList = new PriorityOfRecipeService().getAll();
            priorityOfRecipeComboBox.setItems(priorityOfRecipeList);
            priorityOfRecipeComboBox.setItemCaptionGenerator(p -> p.getPriorityName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextField descriptionFilterField = new TextField("Описание");

        recipeGrid.removeAllColumns();
        recipeGrid.addColumn(Recipe ::getDescription).setCaption("Описание");
        recipeGrid.addColumn(r -> r.getPatient().getLastName() + " " + r.getPatient().getFirstName() + " " + r.getPatient().getPatronymic()).setCaption("Пациент");
        recipeGrid.addColumn(r -> r.getDoctor().getLastName()+ " " + r.getDoctor().getFirstName() + " " + r.getDoctor().getPatronymic()).setCaption("Доктор");
        recipeGrid.addColumn(Recipe::getCreationDate).setCaption("Дата создания");
        recipeGrid.addColumn(Recipe::getValidity).setCaption("Срок действия");
        recipeGrid.addColumn(r -> r.getPriority().getPriorityName()).setCaption("Приоритет");
        recipeGrid.setSizeFull();

        addRecipeBtn = new Button("Добавить", VaadinIcons.PLUS_CIRCLE);
        updateRecipeBtn = new Button("Редактировать", VaadinIcons.PENCIL);
        deleteRecipeBtn = new Button("Удалить", VaadinIcons.TRASH);
        updateRecipeBtn.setEnabled(false);
        deleteRecipeBtn.setEnabled(false);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addRecipeBtn, updateRecipeBtn, deleteRecipeBtn);

        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponents(filterPanel, recipeGrid, buttonsLayout);

        recipeGrid.addSelectionListener(selectionEvent -> {
            if(!recipeGrid.asSingleSelect().isEmpty()){
                updateRecipeBtn.setEnabled(true);
                deleteRecipeBtn.setEnabled(true);
            }else {
                updateRecipeBtn.setEnabled(false);
                deleteRecipeBtn.setEnabled(false);
            }
        });

        addRecipeBtn.addClickListener(clickEvent -> getUI().addWindow(new RecipeFormWindow(recipeGrid, 1)));
        updateRecipeBtn.addClickListener(clickEvent -> getUI().addWindow(new RecipeFormWindow(recipeGrid, 2)));

        deleteRecipeBtn.addClickListener(clickEvent -> {
            try {
                new RecipeService().remove(recipeGrid.asSingleSelect().getValue());
                updateTableRecipes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        acceptBtn = new Button("Применить");
        acceptBtn.addClickListener(clickEvent -> {
            Patient patient = patientComboBox.getValue();
            PriorityOfRecipe priorityOfRecipe = priorityOfRecipeComboBox.getValue();
            String description = descriptionFilterField.getValue();

            Long patientId = patient == null ? -1L : patient.getId();
            Long priorityId = priorityOfRecipe == null ? -1L : priorityOfRecipe.getId();

            List<Recipe> recipeList;
            RecipeService recipeService = new RecipeService();
            recipeList = recipeService.filterRecipe(patientId, priorityId, description);

            recipeGrid.setItems(recipeList);
            recipeGrid.getDataProvider().refreshAll();
        });

        filterLayout.addComponents(patientComboBox, priorityOfRecipeComboBox, descriptionFilterField, acceptBtn);
        filterLayout.setComponentAlignment(acceptBtn, Alignment.BOTTOM_RIGHT);
    }

    public void updateTableRecipes(){
        try {
            List<Recipe> recipeList = new RecipeService().getAll();
            recipeGrid.setItems(recipeList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateTableRecipes();
    }

}
