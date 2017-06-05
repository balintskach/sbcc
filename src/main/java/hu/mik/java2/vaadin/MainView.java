package hu.mik.java2.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

//import hu.mik.java2.vehicle.bean.Vehicle;

@SuppressWarnings("serial")
@SpringView(name = MainView.MAIN_VIEW_NAME)
@Theme("valo")
public class MainView extends VerticalLayout implements View {

	protected static final String MAIN_VIEW_NAME = "";

	
	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("Kezdőlap");
		this.setMargin(true);
		this.setSpacing(true);
		Label htmlLabel = new Label("<h1>SB-CAR-CONTROLL Kft.</h1>");
		htmlLabel.setContentMode(ContentMode.HTML);
		htmlLabel.setSizeUndefined();
		this.addComponent(htmlLabel);
		this.setComponentAlignment(htmlLabel, Alignment.TOP_CENTER);

		Button navToVehicleListButton = new Button("Munkák listája");

		navToVehicleListButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(VehicleView.VEHICLE_VIEW_NAME);
			}
		});

		this.addComponent(navToVehicleListButton);
		this.setComponentAlignment(navToVehicleListButton, Alignment.BOTTOM_CENTER);
		
	

		Button logOutButton = new Button("Kilépés");

		logOutButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getPage().setLocation(
						VaadinServlet.getCurrent().getServletContext().getContextPath() + "/login?logout=1");
			}
		});

		this.addComponent(logOutButton);
		this.setComponentAlignment(logOutButton, Alignment.BOTTOM_CENTER);
	}

}
