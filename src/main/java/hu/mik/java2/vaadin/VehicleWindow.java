package hu.mik.java2.vaadin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import hu.mik.java2.service.VehicleService;
import hu.mik.java2.vehicle.bean.Vehicle;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope(scopeName = "prototype")
public class VehicleWindow extends Window {

	@Autowired
	@Qualifier("vehicleServiceImpl")
	private VehicleService vehicleService;

	public void showWindow(Vehicle vehicle, String title, boolean isReadonly, VehicleView vehicleView) {
		this.setHeight("75%");
		this.setWidth("50%");
		this.center();
		this.setCaption(title);
		this.setContent(createEditLayout(vehicle, isReadonly, vehicleView));
		UI.getCurrent().addWindow(this);

	}

	private Component createEditLayout(Vehicle vehicle, boolean isReadOnly, final VehicleView vehicleView) {
		final BeanFieldGroup<Vehicle> vehicleBeanField = new BeanFieldGroup<Vehicle>(Vehicle.class);
		vehicleBeanField.setItemDataSource(vehicle);

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		Component formComponent = createFormLayout(vehicleBeanField, isReadOnly);
		formComponent.setSizeUndefined();

		verticalLayout.addComponent(formComponent);
		verticalLayout.setComponentAlignment(formComponent, Alignment.BOTTOM_CENTER);

		Button saveButton = new Button("Mentés");
		saveButton.setVisible(!isReadOnly);

		saveButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					vehicleBeanField.commit();
					Vehicle bean = vehicleBeanField.getItemDataSource().getBean();
					if (bean.getId() == null) {

						bean.setActive(1);
						bean.setStatus(0);
						vehicleService.saveVehicle(bean);
					} else {
						vehicleService.updateVehicle(bean);
					}
					VehicleWindow.this.close();
					Notification.show("Sikeres járműhozzáadás/módosítás!");
					vehicleView.refreshTable();
				} catch (Exception e) {
					Notification.show("Hiba történt mentés/módosítás közben!");
				}
			}
		});
		verticalLayout.addComponent(saveButton);
		verticalLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_CENTER);

		return verticalLayout;
	}

	private Component createFormLayout(BeanFieldGroup<Vehicle> vehicleBeanField, boolean isReadonly) {
		List<String> jobtypes = new ArrayList<>();

		jobtypes.add("Eredetiség vizsgálat");
		jobtypes.add("Szélvédő javítás");
		jobtypes.add("Karosszéria lakatolás");

		FormLayout formLayout = new FormLayout();

		TextField licensePlateField = vehicleBeanField.buildAndBind("Rendszám", "licenseplate", TextField.class);
		licensePlateField.setNullRepresentation("");
		licensePlateField.setReadOnly(isReadonly);
		formLayout.addComponent(licensePlateField);

		TextField ownerField = vehicleBeanField.buildAndBind("Tulajdonos", "owner", TextField.class);
		ownerField.setNullRepresentation("");
		ownerField.setReadOnly(isReadonly);
		formLayout.addComponent(ownerField);

		TextField emailField = vehicleBeanField.buildAndBind("Email", "email", TextField.class);
		emailField.setNullRepresentation("");
		emailField.setReadOnly(isReadonly);
		formLayout.addComponent(emailField);

		DateField registrydateField = vehicleBeanField.buildAndBind("Felvétel időpontja", "registrydate",
				DateField.class);
		registrydateField.setReadOnly(isReadonly);
		registrydateField.setDateFormat("yyyy.MM.dd.");
		formLayout.addComponent(registrydateField);

		NativeSelect jobtypeNativeSelect = new NativeSelect("Típus");

		for (String jobtype : jobtypes) {
			jobtypeNativeSelect.addItem(jobtype);
		}
		vehicleBeanField.bind(jobtypeNativeSelect, "jobtype");
		jobtypeNativeSelect.setReadOnly(isReadonly);
		formLayout.addComponent(jobtypeNativeSelect);

		final CheckBox requiredetailsCheckBox = new CheckBox("Kért részletes információt");
		requiredetailsCheckBox.setImmediate(true);
		requiredetailsCheckBox.setReadOnly(isReadonly);
		formLayout.addComponent(requiredetailsCheckBox);

		final TextField tempRequiredetails = vehicleBeanField.buildAndBind("", "requiredetails", TextField.class);

		if (isReadonly) {
			requiredetailsCheckBox.setReadOnly(!isReadonly);
			requiredetailsCheckBox.setValue(tempRequiredetails.getValue().contains("1"));
			requiredetailsCheckBox.setReadOnly(isReadonly);
		} else {

			requiredetailsCheckBox.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					tempRequiredetails.setValue(requiredetailsCheckBox.getValue() ? "1" : "0");

				}

			});

			tempRequiredetails.setVisible(false);
			tempRequiredetails.setReadOnly(isReadonly);
			formLayout.addComponent(tempRequiredetails);

		}

		return formLayout;
	}

}
