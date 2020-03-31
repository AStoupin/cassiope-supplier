package ru.cetelem.cassiope.supplier.io.f150;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

public interface F150Entry extends FileEntry {

	@Override
	default PayloadType getType() {
		return PayloadType.F150;
	}

}
