package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.service.DoctorService;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import java.sql.SQLException;
import java.util.List;

public class DoctorFormWindow extends Window{

    private Doctor doctor;
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    private Grid<Doctor> doctorGrid;
    private int definition;

    public DoctorFormWindow(Grid<Doctor> grid, int i) {
        this.doctorGrid = grid;
        this.definition = i;
        buildDoctorWindow();
    }

    public void buildDoctorWindow(){

        VerticalLayout mainDoctorLayout = new VerticalLayout();

        TextField lastName = new TextField("Фамилия");
        binder.forField(lastName).withValidator(ln -> ln != null && !ln.isEmpty(), "Введите фамилию").bind(Doctor :: getLastName, Doctor :: setLastName);
        TextField firstName = new TextField("Имя");
        binder.forField(firstName).withValidator(fn -> fn != null && !fn.isEmpty(), "Введите имя").bind(Doctor :: getFirstName, Doctor :: setFirstName);
        TextField patronymic = new TextField("Отчество");
        binder.forField(patronymic).withValidator(p -> p != null && !p.isEmpty(), "Введите отчество").bind(Doctor :: getPatronymic, Doctor :: setPatronymic);
        TextField specialization = new TextField("Специализация");
        binder.forField(specialization).withValidator(sp -> sp != null && !sp.isEmpty(), "Введите специализацию").bind(Doctor :: getSpecialization, Doctor :: setSpecialization);

        FormLayout formLayout = new FormLayout();
        formLayout.addComponents(lastName, firstName, patronymic, specialization);

        Button saveBtn = new Button("ОК", VaadinIcons.CHECK);
        Button cancelBtn = new Button("Отменить", VaadinIcons.CLOSE_SMALL);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(saveBtn, cancelBtn);

        mainDoctorLayout.addComponents(formLayout, btnLayout);
        setContent(mainDoctorLayout);

        if(definition == 1) {
            saveBtn.addClickListener(e -> {
                if(binder.validate().isOk()) {
                    try {
                        doctor = new Doctor();
                        doctor.setLastName(lastName.getValue());
                        doctor.setFirstName(firstName.getValue());
                        doctor.setPatronymic(patronymic.getValue());
                        doctor.setSpecialization(specialization.getValue());
                        new DoctorService().add(doctor);
                        List<Doctor> doctorList = new DoctorService().getAll();
                        doctorGrid.setItems(doctorList);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    close();
                }
            });
        }

        if(definition == 2){
            doctor = doctorGrid.asSingleSelect().getValue();
            binder.setBean(doctor);

            saveBtn.addClickListener(clickEvent -> {
                if(binder.validate().isOk()) {
                    try {
                        Doctor alterableDoctor = new Doctor();
                        alterableDoctor.setLastName(lastName.getValue());
                        alterableDoctor.setFirstName(firstName.getValue());
                        alterableDoctor.setPatronymic(patronymic.getValue());
                        alterableDoctor.setSpecialization(specialization.getValue());
                        alterableDoctor.setId((doctor.getId()));
                        new DoctorService().update(alterableDoctor);
                        List<Doctor> doctorList = new DoctorService().getAll();
                        doctorGrid.setItems(doctorList);
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


