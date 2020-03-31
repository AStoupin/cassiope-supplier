package ru.cetelem.supplier.ui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;

import ru.cetelem.supplier.util.Configurator;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

//Override base layout theme. All generic views have specific style
@HtmlImport("frontend://styles/shared-styles.html")
public class BaseView extends VerticalLayout {
	private static final Log log = LogFactory.getLog(BaseView.class); 
	
	public BaseView() {
		setSizeFull();		
    }

	public void showCompanyName(Environment environment) {	
		log.info("showCompanyName started");	
		Html creatorName = new Html("<span><b>" + environment.getProperty(Configurator.CREATOR_NAME) 
			+ "</b>" + getVersion(environment) + "</span>");
		log.debug("creatorName " + creatorName.getInnerHtml().toString());
		creatorName.getElement().getStyle().set("position","absolute");
		creatorName.getElement().getStyle().set("top", "-3px");
		creatorName.getElement().getStyle().set("right", 15 + "px");		
		add(creatorName);
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
