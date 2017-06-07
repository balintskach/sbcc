package hu.mik.java2.vaadin;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Service;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import hu.mik.java2.service.VehicleService;
import hu.mik.java2.vehicle.bean.Vehicle;
import hu.mik.java2.email.EmailService;

@SuppressWarnings({ "serial", "unchecked" })
@SpringView(name = VehicleView.VEHICLE_VIEW_NAME)
public class VehicleView extends VerticalLayout implements View {

	protected static final String VEHICLE_VIEW_NAME = "vehicle";

	private BeanContainer<Long, Vehicle> vehicleBean;

	Set<Object> selectedItemIds = new HashSet<Object>();

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	@Qualifier("vehicleServiceImpl")
	private VehicleService vehicleService;

	@Autowired
	@Qualifier("emailService")
	private EmailService vehicleEmail;

	Table vehicleTable = new Table("Munkák listája");

	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("Munka lista");
		this.setMargin(true);
		this.setSpacing(true);
		Component functionComponent = createFunctionLayout();
		this.addComponent(functionComponent);
		this.setComponentAlignment(functionComponent, Alignment.TOP_CENTER);
		this.addComponent(createTable());

	}

	private Component createFunctionLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		Component activeVehicleComponent = createActiveField();
		horizontalLayout.addComponent(activeVehicleComponent);
		horizontalLayout.setComponentAlignment(activeVehicleComponent, Alignment.BOTTOM_CENTER);
		Component searchVehicleComponent = createSearchField();
		horizontalLayout.addComponent(searchVehicleComponent);
		horizontalLayout.setComponentAlignment(searchVehicleComponent, Alignment.BOTTOM_CENTER);
		Component newVehicleComponent = createNewVehicleButton();
		horizontalLayout.addComponent(newVehicleComponent);
		horizontalLayout.setComponentAlignment(newVehicleComponent, Alignment.BOTTOM_CENTER);
		/*
		 * Component deleteVehicleComponent = deleteVehicleButton();
		 * horizontalLayout.addComponent(deleteVehicleComponent);
		 * horizontalLayout.setComponentAlignment(deleteVehicleComponent,
		 * Alignment.BOTTOM_CENTER);
		 */
		Component backToMainComponent = backToMainViewButton();
		horizontalLayout.addComponent(backToMainComponent);
		horizontalLayout.setComponentAlignment(backToMainComponent, Alignment.BOTTOM_CENTER);
		Component logOutComponent = logOutButton();
		horizontalLayout.addComponent(logOutComponent);
		horizontalLayout.setComponentAlignment(logOutComponent, Alignment.BOTTOM_CENTER);
		return horizontalLayout;
	}

	private Component createSearchField() {
		final TextField searchfield = new TextField("Rendszám keresése");
		searchfield.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (searchfield.getValue().isEmpty()) {
					refreshTable();
				} else {
					vehicleBean.removeAllItems();
					vehicleBean.addAll(vehicleService.findByLicenseplateLike(searchfield.getValue()));
				}
			}
		});
		return searchfield;
	}
	
	private Component createActiveField() {
		final CheckBox activeCheckBox = new CheckBox("Csak az aktív munkák");
		activeCheckBox.setValue(true);
		activeCheckBox.setImmediate(true);
		activeCheckBox.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (activeCheckBox.getValue()) {
					vehicleBean.removeAllItems();
					vehicleBean.addAll(vehicleService.findByActiveLike(1));
				} else {
					refreshTable();
				}

			}
		});

		return activeCheckBox;
	}

	private Component createNewVehicleButton() {
		Button newVehicleButton = new Button("Új munka hozzáadása");

		newVehicleButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Vehicle vehicle = new Vehicle();
				ctx.getBean(VehicleWindow.class).showWindow(vehicle, "Új munka hozzáadása", false, VehicleView.this);
			}
		});

		return newVehicleButton;
	}

	/*
	 * private Component deleteVehicleButton() { Button deleteVehicleButton =
	 * new Button("Kijelöltek törlése");
	 * 
	 * deleteVehicleButton.addClickListener(new ClickListener() {
	 * 
	 * @Override public void buttonClick(ClickEvent event) { try { for (Object
	 * itemId : selectedItemIds) { BeanItem<Vehicle> vehicleItem =
	 * (BeanItem<Vehicle>) vehicleTable.getItem(itemId); BeanFieldGroup<Vehicle>
	 * vehicleFieldGroup = new BeanFieldGroup<Vehicle>(Vehicle.class);
	 * vehicleFieldGroup.setItemDataSource(vehicleItem);
	 * vehicleFieldGroup.commit(); Vehicle bean =
	 * vehicleFieldGroup.getItemDataSource().getBean();
	 * vehicleService.deleteVehicle(bean); } refreshTable();
	 * selectedItemIds.removeAll(selectedItemIds);
	 * Notification.show("Sikeres jármű törlés!"); } catch (Exception e) {
	 * Notification.show("Hiba törlés közben!"); } } }); return
	 * deleteVehicleButton; }
	 */

	private Component backToMainViewButton() {
		Button backButton = new Button("Vissza a főoldalra");

		backButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(MainView.MAIN_VIEW_NAME);
			}
		});
		return backButton;
	}

	private Component logOutButton() {
		Button logOutButoon = new Button("Kilépés");

		logOutButoon.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getPage().setLocation(
						VaadinServlet.getCurrent().getServletContext().getContextPath() + "/login?logout=1");
			}
		});

		return logOutButoon;
	}

	protected void refreshTable() {
		vehicleBean.removeAllItems();
		vehicleBean.addAll(vehicleService.listVehicles());
		
		
	}

	private Component createTable() {
		vehicleBean = new BeanContainer<Long, Vehicle>(Vehicle.class);
		vehicleBean.setBeanIdProperty("id");
		vehicleBean.addAll(vehicleService.listVehicles());
		vehicleTable.setContainerDataSource(vehicleBean);
		vehicleTable.setSizeFull();

		/*
		 * vehicleTable.addGeneratedColumn("selector", new ColumnGenerator() {
		 * 
		 * @Override public Object generateCell(Table source, final Object
		 * itemId, Object columnId) { CheckBox deleteCB = new CheckBox();
		 * deleteCB.addValueChangeListener(new ValueChangeListener() {
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) { if
		 * (selectedItemIds.contains(itemId)) { selectedItemIds.remove(itemId);
		 * } else { selectedItemIds.add(itemId); } } });
		 * 
		 * return deleteCB; } });
		 */

		vehicleTable.addGeneratedColumn("viewVehicle", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button viewVehicleButton = new Button("Megtekintés");
				viewVehicleButton.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						BeanItem<Vehicle> vehicleItem = (BeanItem<Vehicle>) source.getItem(itemId);
						ctx.getBean(VehicleWindow.class).showWindow(vehicleItem.getBean(), "Megtekintés", true,
								VehicleView.this);
					}
				});

				return viewVehicleButton;
			}
		});

		vehicleTable.addGeneratedColumn("viewEditLayout", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				VerticalLayout viewEditLayout = new VerticalLayout();

				viewEditLayout.setMargin(true);
				viewEditLayout.setSpacing(true);
				Button editVehicleButton = new Button("Szerkesztés");

				editVehicleButton.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						BeanItem<Vehicle> vehicleItem = (BeanItem<Vehicle>) source.getItem(itemId);
						ctx.getBean(VehicleWindow.class).showWindow(vehicleItem.getBean(), "Szerkesztés", false,
								VehicleView.this);
					}
				});
				Button viewVehicleButton = new Button("Megtekintés");
				viewVehicleButton.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						BeanItem<Vehicle> vehicleItem = (BeanItem<Vehicle>) source.getItem(itemId);
						ctx.getBean(VehicleWindow.class).showWindow(vehicleItem.getBean(), "Megtekintés", true,
								VehicleView.this);
					}
				});

				viewEditLayout.addComponent(editVehicleButton);
				viewEditLayout.setComponentAlignment(editVehicleButton, Alignment.MIDDLE_CENTER);
				viewEditLayout.addComponent(viewVehicleButton);
				viewEditLayout.setComponentAlignment(viewVehicleButton, Alignment.MIDDLE_CENTER);
				return viewEditLayout;
			}
		});

		vehicleTable.addGeneratedColumn("increaseStatus", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final BeanItem<Vehicle> vehicleItem = (BeanItem<Vehicle>) source.getItem(itemId);

				HorizontalLayout statusLayout = new HorizontalLayout();
				statusLayout.setMargin(true);
				statusLayout.setSpacing(true);
				if (checkActive(vehicleItem.getBean())) {

					if (!checkStatus(vehicleItem.getBean())) {
						Button increaseStatusButton = new Button("Növelés");
						ProgressBar statusProgressBar = new ProgressBar();
						statusProgressBar.setValue(calculateStatusPercentage(vehicleItem.getBean()));

						increaseStatusButton.addClickListener(new ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {

								vehicleItem.getBean().setStatus(increaseStatus(vehicleItem.getBean()));

								vehicleService.updateVehicle(vehicleItem.getBean());

								Notification.show("Sikeres státusz növelés! jelenlegi státusz: "
										+ getStatusName(vehicleItem.getBean()));
								
								if (vehicleItem.getBean().getRequiredetails() == 1 && vehicleItem.getBean()
										.getStatus() != getMaxStatusByJobtype(vehicleItem.getBean())) {
									vehicleEmail.readyToSendEmail(vehicleItem.getBean().getEmail(),
											"java2balintskach@gmail.com",
											"Jármű státusza: " + vehicleItem.getBean().getStatus(),
											"Tisztelt " + vehicleItem.getBean().getOwner() + "!\n" + "#"
													+ vehicleItem.getBean().getId() + " "
													+ vehicleItem.getBean().getLicenseplate()
													+ " rendszámú jármű jelenlegi státusza: "
													+ getStatusName(vehicleItem.getBean()) + ".");
								}
								refreshTable();

							}

						});

						statusLayout.addComponent(statusProgressBar);
						statusLayout.setComponentAlignment(statusProgressBar, Alignment.MIDDLE_CENTER);

						statusLayout.addComponent(increaseStatusButton);
						statusLayout.setComponentAlignment(increaseStatusButton, Alignment.MIDDLE_CENTER);
					} else {
						Button finishJobButton = new Button("Lezárás");
						finishJobButton.addClickListener(new ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								vehicleItem.getBean().setActive(0);
								vehicleService.updateVehicle(vehicleItem.getBean());
								Notification.show("#" + vehicleItem.getBean().getId() + " "
										+ vehicleItem.getBean().getLicenseplate() + " lezárva.");
								vehicleEmail.readyToSendEmail(vehicleItem.getBean().getEmail(),
										"java2balintskach@gmail.com", "Jármű elkészült",
										"Tisztelt " + vehicleItem.getBean().getOwner() + "!\n" + "#"
												+ vehicleItem.getBean().getId() + " "
												+ vehicleItem.getBean().getLicenseplate()
												+ " rendszámú jármű elkészült és átvehető.");
								refreshTable();
							}
						});

						statusLayout.addComponent(finishJobButton);
						statusLayout.setComponentAlignment(finishJobButton, Alignment.MIDDLE_CENTER);
					}
				} else {
					Label closedLabel = new Label("Lezárva");
					statusLayout.addComponent(closedLabel);
					statusLayout.setComponentAlignment(closedLabel, Alignment.MIDDLE_CENTER);
				}
				return statusLayout;
			}

			private String getStatusName(Vehicle bean) {
				String jobType = bean.getJobtype();
				Integer status = bean.getStatus();
				if (jobType.contains("Eredetiség vizsgálat")) {
					switch (status) {
					case 0:
						return "Felvéve a rendszerbe";
					case 1:
						return "Elkészült";
					}
				} else if (jobType.contains("Szélvédő javítás")) {
					switch (status) {
					case 0:
						return "Felvéve a rendszerbe";
					case 1:
						return "Megérkezett a jármű";
					case 2:
						return "Új szélvédőre várunk";
					case 3:
						return "Munka folyamatban van";
					case 4:
						return "Átvehető a jármű";
					}
				} else {
					switch (status) {
					case 0:
						return "Felvéve a rendszerbe";
					case 1:
						return "Megérkezett a jármű";
					case 2:
						return "Alkatrészre várunk";
					case 3:
						return "Munka folyamatban van";
					case 4:
						return "A jármű a fényezőnél van";
					case 5:
						return "Összeszerelés folyamatban van";
					case 6:
						return "Átvehető a jármű";
					}
				}
				return status.toString();
			}

			private boolean checkActive(Vehicle bean) {
				return bean.getActive() == 1 ? true : false;
			}

			private boolean checkStatus(Vehicle bean) {
				return bean.getStatus() >= getMaxStatusByJobtype(bean) ? true : false;
			}

			private void sendMail(Vehicle bean) {
				String toAddress = bean.getEmail();
				String fromAddress = "java2balintskach@gmail.com";
				String subject = "Order status";
				String msg = bean.getLicenseplate() + " rendszámú jármű " + bean.getJobtype()
						+ "típusú munkának státusza:" + bean.getStatus() + ".";
				vehicleEmail.readyToSendEmail(toAddress, fromAddress, subject, msg);
			}

			private Integer increaseStatus(Vehicle bean) {
				if (bean.getStatus() >= getMaxStatusByJobtype(bean)) {
					return bean.getStatus();
				} else {
					return bean.getStatus() + 1;
				}
			}

			private Float calculateStatusPercentage(Vehicle bean) {
				Integer currentStatus = bean.getStatus();
				Float maxStatus = (float) (getMaxStatusByJobtype(bean));

				if (currentStatus == 0) {
					return 0.0f;
				} else {
					return (float) (currentStatus) / maxStatus;
				}
			}

			private Integer getMaxStatusByJobtype(Vehicle bean) {
				String jobtype = bean.getJobtype();
				if (jobtype.equals("Eredetiség vizsgálat")) {
					return 1;
				} else if (jobtype.equals("Szélvédő javítás")) {
					return 4;
				} else if (jobtype.equals("Karosszéria lakatolás")) {
					return 6;
				} else {
					return 0;
				}
			}

		});

		vehicleTable.setVisibleColumns("id", "licenseplate", "owner", "email", "registrydate", "jobtype",
				"viewEditLayout", "increaseStatus");
		vehicleTable.setColumnHeader("id", "ID");
		vehicleTable.setColumnHeader("licenseplate", "Rendszám");
		vehicleTable.setColumnHeader("owner", "Tulajdonos");
		vehicleTable.setColumnHeader("emailaddress", "Email cím");
		vehicleTable.setColumnHeader("registrydate", "Felvétel dátuma");
		vehicleTable.setColumnHeader("jobtype", "Típus");
		vehicleTable.setColumnHeader("viewEditLayout", "Részletek");
		vehicleTable.setColumnHeader("increaseStatus", "Státusz");

		vehicleTable.setColumnAlignment("id", Align.CENTER);
		vehicleTable.setColumnAlignment("licenseplate", Align.CENTER);
		vehicleTable.setColumnAlignment("owner", Align.CENTER);
		vehicleTable.setColumnAlignment("emailaddress", Align.CENTER);
		vehicleTable.setColumnAlignment("registrydate", Align.CENTER);
		vehicleTable.setColumnAlignment("jobtype", Align.CENTER);
		vehicleTable.setColumnAlignment("viewEditLayout", Align.CENTER);
		vehicleTable.setColumnAlignment("increaseStatus", Align.LEFT);

		return vehicleTable;
	}

}
