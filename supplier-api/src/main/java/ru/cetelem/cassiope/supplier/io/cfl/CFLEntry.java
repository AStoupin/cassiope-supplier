package ru.cetelem.cassiope.supplier.io.cfl;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

public interface CFLEntry extends FileEntry {
	
	@Override
	default PayloadType getType() {
		return PayloadType.CFL;
	}

}
