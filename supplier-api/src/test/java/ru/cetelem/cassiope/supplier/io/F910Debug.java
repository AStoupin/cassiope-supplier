package ru.cetelem.cassiope.supplier.io;

import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import ru.cetelem.cassiope.supplier.io.f910.F910;
import ru.cetelem.cassiope.supplier.io.f910.F910Header;
import ru.cetelem.cassiope.supplier.io.f910.F910Item;
import ru.cetelem.cassiope.supplier.io.f910.F910Trail;

public class F910Debug {

	@Test
	public void testRead0() throws Exception {
		System.out.println("F910_test0");
		F910 f910 = new F910();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F910Debug.class.getResourceAsStream("/f910-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F910Debug.class.getResourceAsStream("/F910_test0"))) {
			BeanReader reader = factory.createReader("f910-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f910-header":
					f910.f910Header = (F910Header) record;
					break;
				case "f910-details":
					f910.f910Items.add((F910Item) record);
					break;
				case "f910-trail":
					f910.f910Trail = (F910Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead1() throws Exception {
		System.out.println("F910_test1");
		F910 f910 = new F910();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F910Debug.class.getResourceAsStream("/f910-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F910Debug.class.getResourceAsStream("/F910_test1"))) {
			BeanReader reader = factory.createReader("f910-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f910-header":
					f910.f910Header = (F910Header) record;
					break;
				case "f910-details":
					f910.f910Items.add((F910Item) record);
					break;
				case "f910-trail":
					f910.f910Trail = (F910Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead2() throws Exception {
		System.out.println("F910_test2");
		F910 f910 = new F910();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F910Debug.class.getResourceAsStream("/f910-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F910Debug.class.getResourceAsStream("/F910_test2"))) {
			BeanReader reader = factory.createReader("f910-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f910-header":
					f910.f910Header = (F910Header) record;
					break;
				case "f910-details":
					f910.f910Items.add((F910Item) record);
					break;
				case "f910-trail":
					f910.f910Trail = (F910Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

}
