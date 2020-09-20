package com.haulmont.testtask.ui;
import com.haulmont.testtask.database.DbException;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.service.PatientService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


public class PatientsTableView extends VerticalLayout implements View {

    private Grid<Patient> patientGrid = new Grid(Patient.class);

    private Button addPatientBtn;
    private Button updatePatientBtn;
    private Button deletePatientBtn;

    public PatientsTableView(){
        buildPatientTable();
    }

    public void buildPatientTable(){

        patientGrid.removeAllColumns();
        patientGrid.addColumn(Patient::getLastName).setCaption("Фамилия");
        patientGrid.addColumn(Patient::getFirstName).setCaption("Имя");
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество");
        patientGrid.addColumn(Patient::getPhoneNumber).setCaption("Номер телефона");
        patientGrid.setSizeFull();

        addPatientBtn = new Button("Добавить", VaadinIcons.PLUS_CIRCLE);
        updatePatientBtn = new Button("Редактировать", VaadinIcons.PENCIL);
        deletePatientBtn = new Button("Удалить", VaadinIcons.TRASH);
        updatePatientBtn.setEnabled(false);
        deletePatientBtn.setEnabled(false);

         HorizontalLayout buttonsLayout = new HorizontalLayout();
         buttonsLayout.addComponents(addPatientBtn, updatePatientBtn, deletePatientBtn);

         setMargin(true);
         setSpacing(true);
         setSizeFull();
         addComponents(patientGrid, buttonsLayout);

        patientGrid.addSelectionListener(selectionEvent -> {
            if(!patientGrid.asSingleSelect().isEmpty()){
                updatePatientBtn.setEnabled(true);
                deletePatientBtn.setEnabled(true);
            } else {
                updatePatientBtn.setEnabled(false);
                deletePatientBtn.setEnabled(false);
            }
        });

        addPatientBtn.addClickListener(clickEvent -> getUI().addWindow(new PatientFormWindow(patientGrid, 1)));
        updatePatientBtn.addClickListener(clickEvent -> getUI().addWindow(new PatientFormWindow(patientGrid, 2)));

        deletePatientBtn.addClickListener(clickEvent -> {
            try {
                new PatientService().remove(patientGrid.asSingleSelect().getValue());
                updateTablePatients();
            } catch (DbException e) {
                if(e.getCause() != null && e.getCause().getClass().equals(SQLIntegrityConstraintViolationException.class)){
                    Notification notification = new Notification("Перед удалением пациента, удалите связанные с ним рецепты");
                    notification.setDelayMsec(1000);
                    notification.show(Page.getCurrent());
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateTablePatients(){
        try {
            List<Patient> patientList = new PatientService().getAll();
            patientGrid.setItems(patientList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateTablePatients();
    }
}
