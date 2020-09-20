package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.service.DoctorService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DoctorsTableView extends VerticalLayout implements View {

    private Grid<Doctor> doctorGrid = new Grid(Doctor.class);

    private Button addDoctorBtn;
    private Button updateDoctorBtn;
    private Button deleteDoctorBtn;
    private Button statisticBtnShow;
    private Button statisticBtnRemove;

    public DoctorsTableView(){
        buildDoctorsTable();
    }

    public void buildDoctorsTable(){

        doctorGrid.removeAllColumns();
        doctorGrid.addColumn(Doctor::getLastName).setCaption("Фамилия");
        doctorGrid.addColumn(Doctor::getFirstName).setCaption("Имя");
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
        doctorGrid.setSizeFull();

        addDoctorBtn = new Button("Добавить", VaadinIcons.PLUS_CIRCLE);
        updateDoctorBtn = new Button("Редактировать", VaadinIcons.PENCIL);
        deleteDoctorBtn = new Button("Удалить", VaadinIcons.TRASH);
        statisticBtnShow = new Button("Показать статистику", VaadinIcons.USER_CARD);
        statisticBtnRemove = new Button("Скрыть статистику", VaadinIcons.CLOSE_SMALL);
        updateDoctorBtn.setEnabled(false);
        deleteDoctorBtn.setEnabled(false);
        statisticBtnShow.setEnabled(true);
        statisticBtnRemove.setEnabled(false);
        statisticBtnRemove.setVisible(false);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addDoctorBtn, updateDoctorBtn, deleteDoctorBtn, statisticBtnShow, statisticBtnRemove);

        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponents(doctorGrid, buttonsLayout);

        doctorGrid.addSelectionListener(selectionEvent -> {
            if(!doctorGrid.asSingleSelect().isEmpty()){
                updateDoctorBtn.setEnabled(true);
                deleteDoctorBtn.setEnabled(true);
            }else {
                updateDoctorBtn.setEnabled(false);
                deleteDoctorBtn.setEnabled(false);
            }
        });

        addDoctorBtn.addClickListener(clickEvent -> getUI().addWindow(new DoctorFormWindow(doctorGrid, 1)));
        updateDoctorBtn.addClickListener(clickEvent -> getUI().addWindow(new DoctorFormWindow(doctorGrid, 2)));
        deleteDoctorBtn.addClickListener(clickEvent -> {
            try {
                new DoctorService().remove(doctorGrid.asSingleSelect().getValue());
                updateTableDoctors();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        statisticBtnShow.addClickListener(clickEvent -> {
            try {
                Map<Long, Integer> doctorStatisticMap = new DoctorService().getDoctorStatistic();
                doctorGrid.addColumn(doctor -> {
                    Integer stats = doctorStatisticMap.get(doctor.getId());
                    return stats;
                }).setId("statistics").setCaption("Выписано рецептов");
                statisticBtnShow.setEnabled(false);
                statisticBtnRemove.setEnabled(true);
                statisticBtnRemove.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        statisticBtnRemove.addClickListener(clickEvent -> {
            doctorGrid.removeColumn("statistics");
            statisticBtnRemove.setEnabled(false);
            statisticBtnRemove.setVisible(false);
            statisticBtnShow.setEnabled(true);
        });
    }

    public void updateTableDoctors(){
        try {
            List<Doctor> doctorList = new DoctorService().getAll();
            doctorGrid.setItems(doctorList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateTableDoctors();
    }
}

