package ru.cetelem.cassiope.supplier.io.f910;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

/**
 * PayloadType.F910
 */
public interface F910Entry extends FileEntry {
	
	@JsonIgnore	
	@Override
	default PayloadType getType() {
		return PayloadType.F910;
	}

}
