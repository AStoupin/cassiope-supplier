package ru.cetelem.supplier.ui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import ru.cetelem.supplier.util.Configurator;

@Theme(value=Material.class/*, variant=Material.DARK*/)
//@Theme(value=Lumo.class/*, variant=Lumo.DARK*/) 
@PWA(name = "X-Factor Supplier Starter Kit", shortName = "X-Factor Supplier")
@HtmlImport("frontend://styles/app-layout-custom.html")
@HtmlImport("frontend://styles/shared-styles.html")
public class MainLayout extends AppLayout   {
	private static final Log log = LogFactory.getLog(MainLayout.class); 

	private Environment environment = null;
	private Tabs tabs;
	private Tab databaseMenuItem;
	
	@Autowired
	MainLayout(Environment environment) {
		this.environment = environment;
		init();

		showDatabaseTab(environment);
		showCompanyName(environment);
	}
	

	
	private void init() {
		setPrimarySection(AppLayout.Section.NAVBAR);
		HorizontalLayout leftLayout = new HorizontalLayout();
		leftLayout.setClassName("leftLayout");
		
		Image img = new Image("/icons/logo.png", "X-Factor Logo");
		img.setHeight("44px");
		 
		this.tabs = new Tabs();
		leftLayout.add(img);

		
		this.tabs.add(new Tab(new RouterLink("Car Models", DictionaryEditorView.class, new String("model"))));
		this.tabs.add(new Tab(new RouterLink("Dealers", DictionaryEditorView.class, new String("dealers"))));
		this.tabs.add(new Tab(new RouterLink("Finance Plans", DictionaryEditorView.class,  new String("plan"))));
		this.tabs.add(new Tab(new RouterLink("Cars", CarListView.class)));
		this.tabs.add(new Tab(new RouterLink("Payloads", PayloadListView.class)));
		


		tabs.setOrientation(Orientation.HORIZONTAL);

		addToNavbar(leftLayout,  tabs);
		


		setDrawerOpened(false);

		
	}



	private void showDatabaseTab(Environment environment) {
		boolean databaseMenuItemVisibale = 
				environment == null || !"0".equals(environment.getProperty(Configurator.SHOW_DATABASE));
		log.info(String.format("init %s", databaseMenuItemVisibale));
		
		if(!databaseMenuItemVisibale)
			return;
		
		Anchor anchor = new Anchor();
		anchor.setText("Database");
		anchor.setHref("db/login.jsp");
		this.databaseMenuItem = new Tab(anchor); 
		
		this.tabs.add(databaseMenuItem);
		
	}



	
	public void showCompanyName(Environment environment) {	
		log.info("showCompanyName started");	
		Html creatorName = new Html("<span><b>" + environment.getProperty(Configurator.CREATOR_NAME) 
			+ "</b>" + getVersion(environment) + "</span>");
		log.debug("creatorName " + creatorName.getInnerHtml().toString());
		creatorName.getElement().getStyle().set("position","absolute");
		//creatorName.getElement().getStyle().set("top", "-3px");
		creatorName.getElement().getStyle().set("right", 15 + "px");		
		
		addToNavbar(creatorName);
	}

	public String getVersion(Environment environment) {	
		log.info("getVersion started");	
		String version = "";	
		try {
			version = environment.getProperty(Configurator.SUPPLIER_CASSIOPE_VERSION);
			if(version == null) {
				version = "";
			} else {
				version = "<font size=\"-1\" color=\"grey\">, v " + version + "</font>";
			}	
		} catch (Exception e) {
			log.error("Exception " + e);
		}
		log.debug("version " + version);
		return version;
	}


}
