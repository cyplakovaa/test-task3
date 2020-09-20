package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.service.PatientService;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import java.sql.SQLException;
import java.util.List;


public class PatientFormWindow extends Window {

    private Patient patient;
    private Binder<Patient> binder = new Binder<>(Patient.class);
    private Grid<Patient> patientGrid;
    private int definition;

    public PatientFormWindow(Grid<Patient> grid, int i) {
        this.patientGrid = grid;
        this.definition = i;
        buildPatientWindow();
    }

    public void buildPatientWindow(){

        VerticalLayout mainLayout = new VerticalLayout();

        TextField lastName = new TextField("Фамилия");
        binder.forField(lastName).withValidator(ln -> ln != null && !ln.isEmpty(), "Введите фамилию").bind(Patient :: getLastName, Patient :: setLastName);
        TextField firstName = new TextField("Имя");
        binder.forField(firstName).withValidator(fn -> fn != null && !fn.isEmpty(), "Введите имя").bind(Patient :: getFirstName, Patient :: setFirstName);
        TextField patronymic = new TextField("Отчество");
        binder.forField(patronymic).withValidator(p -> p != null && !p.isEmpty(), "Введите отчество").bind(Patient :: getPatronymic, Patient :: setPatronymic);
        TextField phoneNumber = new TextField("Номер телефона (8-9xx-xxx-xx-xx)");
        binder.forField(phoneNumber).withValidator(pn -> pn.length() == 15, "Введите номер телефона").bind(Patient :: getPhoneNumber, Patient :: setPhoneNumber);

        FormLayout formPatientLayout = new FormLayout();
        formPatientLayout.addComponents(lastName, firstName, patronymic, phoneNumber);

        Button saveBtn = new Button("ОК", VaadinIcons.CHECK);
        Button cancelBtn = new Button("Отменить", VaadinIcons.CLOSE_SMALL);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(saveBtn, cancelBtn);

        mainLayout.addComponents(formPatientLayout, btnLayout);
        setContent(mainLayout);

        if(definition == 1) {
            saveBtn.addClickListener(e -> {
                if(binder.validate().isOk()) {
                    try {
                        patient = new Patient();
                        patient.setLastName(lastName.getValue());
                        patient.setFirstName(firstName.getValue());
                        patient.setPatronymic(patronymic.getValue());
                        patient.setPhoneNumber(phoneNumber.getValue());
                        new PatientService().add(patient);
                        List<Patient> patientList = new PatientService().getAll();
                        patientGrid.setItems(patientList);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    close();
                }
            });
        }

        if(definition == 2){
            patient = patientGrid.asSingleSelect().getValue();
            binder.setBean(patient);

            saveBtn.addClickListener(clickEvent -> {
                if(binder.validate().isOk()) {
                    try {
                        Patient alterablePatient = new Patient();
                        alterablePatient.setLastName(lastName.getValue());
                        alterablePatient.setFirstName(firstName.getValue());
                        alterablePatient.setPatronymic(patronymic.getValue());
                        alterablePatient.setPhoneNumber(phoneNumber.getValue());
                        alterablePatient.setId((patient.getId()));
                        new PatientService().update(alterablePatient);
                        List<Patient> patientList = new PatientService().getAll();
                        patientGrid.setItems(patientList);
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

