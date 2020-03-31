package ru.cetelem.cassiope.supplier.io;

import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;
//import org.junit.jupiter.api.Test;

import ru.cetelem.cassiope.supplier.io.f950.F950;
import ru.cetelem.cassiope.supplier.io.f950.F950Header;
import ru.cetelem.cassiope.supplier.io.f950.F950Item;
import ru.cetelem.cassiope.supplier.io.f950.F950Trailer;

public class F950Debug {

	@Test
	public void testRead0() throws Exception {
		F950 f950 = new F950();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F950Debug.class.getResourceAsStream("/f950-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F950Debug.class.getResourceAsStream("/F950_test0"))) {
			BeanReader reader = factory.createReader("f950-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f950-header":
					f950.header = (F950Header) record;
					break;
				case "f950-details":
					f950.items.add((F950Item) record);
					break;
				case "f950-trailer":
					f950.trailer = (F950Trailer) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead1() throws Exception {
		F950 f950 = new F950();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F950Debug.class.getResourceAsStream("/f950-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F950Debug.class.getResourceAsStream("/F950_test1"))) {
			BeanReader reader = factory.createReader("f950-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f950-header":
					f950.header = (F950Header) record;
					break;
				case "f950-details":
					f950.items.add((F950Item) record);
					break;
				case "f950-trailer":
					f950.trailer = (F950Trailer) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead2() throws Exception {
		F950 f950 = new F950();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F950Debug.class.getResourceAsStream("/f950-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F950Debug.class.getResourceAsStream("/F950_test2"))) {
			BeanReader reader = factory.createReader("f950-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f950-header":
					f950.header = (F950Header) record;
					break;
				case "f950-details":
					f950.items.add((F950Item) record);
					break;
				case "f950-trailer":
					f950.trailer = (F950Trailer) record;
					break;
				default:
					break;
				}
			}
		}
	}

}
