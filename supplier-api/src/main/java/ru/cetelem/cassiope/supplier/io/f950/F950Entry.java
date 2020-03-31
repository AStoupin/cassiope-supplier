package ru.cetelem.cassiope.supplier.io.f950;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

public interface F950Entry extends FileEntry {
	
	@JsonIgnore
	@Override
	default PayloadType getType() {
		return PayloadType.F950;
	}

}
