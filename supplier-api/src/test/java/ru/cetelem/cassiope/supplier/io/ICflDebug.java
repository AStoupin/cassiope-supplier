package ru.cetelem.cassiope.supplier.io;

import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import ru.cetelem.cassiope.supplier.io.icfl.ICfl;
import ru.cetelem.cassiope.supplier.io.icfl.ICfl22Item;
import ru.cetelem.cassiope.supplier.io.icfl.ICflHeader;
import ru.cetelem.cassiope.supplier.io.icfl.ICflTrail;

public class ICflDebug {

	@Test
	public void testRead0() throws Exception {
		System.out.println("ICfl_test0");
		ICfl icfl = new ICfl();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(ICflDebug.class.getResourceAsStream("/icfl-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				ICflDebug.class.getResourceAsStream("/ICFL_20190814.txt"))) {
			BeanReader reader = factory.createReader("icfl-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "icfl-header":
					icfl.icflHeader = (ICflHeader) record;
					break;
				case "icfl-details":
					icfl.icfl22Items.add((ICfl22Item) record);
					break;
				case "icfl-trail":
					icfl.icflTrail = (ICflTrail) record;
					break;
				default:
					break;
				}
			}
		}
	}
}
