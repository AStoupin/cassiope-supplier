package ru.cetelem.cassiope.supplier.io.f940;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;

/**
 * PayloadType.F940
 */
public interface F940Entry extends FileEntry {
	
	@JsonIgnore	
	@Override
	default PayloadType getType() {
		return PayloadType.F940;
	}

}
