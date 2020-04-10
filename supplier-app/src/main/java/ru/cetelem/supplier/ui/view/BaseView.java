package ru.cetelem.supplier.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

//Override base layout theme. All generic views have specific style
//@HtmlImport("frontend://styles/shared-styles.html")
public class BaseView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	
	public BaseView() {
		setSizeFull();
		setPadding(false);

    }

}
