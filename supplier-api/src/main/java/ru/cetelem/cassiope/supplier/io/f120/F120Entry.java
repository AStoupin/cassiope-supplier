package ru.cetelem.cassiope.supplier.io.f120;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

public interface F120Entry extends FileEntry {
	
	@Override
	default PayloadType getType() {
		return PayloadType.F120;
	}

}
