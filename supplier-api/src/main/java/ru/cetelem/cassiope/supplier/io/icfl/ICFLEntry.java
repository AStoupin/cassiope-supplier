package ru.cetelem.cassiope.supplier.io.icfl;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

public interface ICFLEntry extends FileEntry {

	@Override
	default PayloadType getType() {
		return PayloadType.ICFL;
	}

}
