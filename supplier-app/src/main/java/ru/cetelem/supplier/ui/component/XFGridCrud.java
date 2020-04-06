package ru.cetelem.supplier.ui.component;

import org.vaadin.crudui.crud.impl.GridCrud;

public class XFGridCrud<T> extends GridCrud<T>{

	public XFGridCrud(Class domainType) {
		super(domainType);

		//padding spacing
	}
	

	@Override
	protected void addButtonClicked() {
		//Do nothing
	}

	@Override
	protected void updateButtonClicked() {
		//Do nothing
	}
	
}
