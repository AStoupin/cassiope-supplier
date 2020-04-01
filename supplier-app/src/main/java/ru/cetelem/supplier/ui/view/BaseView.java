package ru.cetelem.supplier.ui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;

import ru.cetelem.supplier.util.Configurator;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

//Override base layout theme. All generic views have specific style
//@HtmlImport("frontend://styles/shared-styles.html")
public class BaseView extends VerticalLayout {
	private static final Log log = LogFactory.getLog(BaseView.class); 
	
	public BaseView() {
		setSizeFull();		
    }

}
