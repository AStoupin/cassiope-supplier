package ru.cetelem.supplier.ui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.vaadin.flow.component.applayout.AbstractAppRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import ru.cetelem.supplier.util.Configurator;

@Theme(value=Material.class/*, variant=Material.DARK*/)
//@Theme(value=Lumo.class/*, variant=Lumo.DARK*/) 
@PWA(name = "X-Factor Supplier Starter Kit", shortName = "X-Factor Supplier")
public class MainLayout extends AbstractAppRouterLayout  {
	private static final Log log = LogFactory.getLog(MainLayout.class); 

	private Environment environment = null;
	private AppLayoutMenuItem databaseMenuItem;
	
	@Autowired
	public MainLayout(Environment environment){
		this.environment = environment;
		init();
	}
	
	private void init() {
		boolean databaseMenuItemVisibale = 
					environment == null || !"0".equals(environment.getProperty(Configurator.SHOW_DATABASE));

		log.info(String.format("init %s", databaseMenuItemVisibale));

		databaseMenuItem.setVisible(databaseMenuItemVisibale);
	}

	@Override
	protected void configure(AppLayout appLayout, AppLayoutMenu appLayoutMenu) {
		
		Image img = new Image("/icons/logo.png", "X-Factor Logo");
		img.setHeight("44px");
		appLayout.setBranding(img);	
		
		appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Car Models", "dic/model"));
		appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Dealers", "dic/dealers"));
		appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Finance Plans", "dic/plan"));
		appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Cars", ""));
		appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Payloads", "payloads"));
		databaseMenuItem = appLayoutMenu.addMenuItem(new AppLayoutMenuItem("Database", e-> this.getAppLayout().getUI().			
			ifPresent(ui -> ui.getPage().executeJavaScript("window.open('db/login.jsp','_blank')"))));
	}
}
