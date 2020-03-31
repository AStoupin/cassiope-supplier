package ru.cetelem.cassiope.supplier.io.f950;

import java.util.ArrayList;
import java.util.List;

public class F950 {

	public F950Header header;
	public List<F950Item> items = new ArrayList<>();
	public F950Trailer trailer;

	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>(items.size() + 2);

		result.add(header);
		result.addAll(items);
		result.add(trailer);

		return result;
	}
}
