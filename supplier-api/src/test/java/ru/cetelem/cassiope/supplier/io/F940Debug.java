package ru.cetelem.cassiope.supplier.io;

import java.io.InputStreamReader;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import ru.cetelem.cassiope.supplier.io.f940.F940;
import ru.cetelem.cassiope.supplier.io.f940.F940Header;
import ru.cetelem.cassiope.supplier.io.f940.F940Item;
import ru.cetelem.cassiope.supplier.io.f940.F940Trail;

public class F940Debug {

	@Test
	public void testRead0() throws Exception {
		System.out.println("F940_test0");
		F940 f940 = new F940();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F940Debug.class.getResourceAsStream("/f940-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F940Debug.class.getResourceAsStream("/F940_test0"))) {
			BeanReader reader = factory.createReader("f940-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f940-header":
					f940.f940Header = (F940Header) record;
					break;
				case "f940-details":
					f940.f940Items.add((F940Item) record);
					break;
				case "f940-trail":
					f940.f940Trail = (F940Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead1() throws Exception {
		System.out.println("F940_test1");
		F940 f940 = new F940();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F940Debug.class.getResourceAsStream("/f940-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F940Debug.class.getResourceAsStream("/F940_test1"))) {
			BeanReader reader = factory.createReader("f940-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f940-header":
					f940.f940Header = (F940Header) record;
					break;
				case "f940-details":
					f940.f940Items.add((F940Item) record);
					break;
				case "f940-trail":
					f940.f940Trail = (F940Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
	public void testRead2() throws Exception {
		System.out.println("F940_test2");
		F940 f940 = new F940();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(F940Debug.class.getResourceAsStream("/f940-mapping.xml"));
		try (InputStreamReader in = new InputStreamReader(
				F940Debug.class.getResourceAsStream("/F940_test2"))) {
			BeanReader reader = factory.createReader("f940-stream", in);
			Object record;
			while ((record = reader.read()) != null) {
				System.out.println(record);
				switch (reader.getRecordName()) {
				case "f940-header":
					f940.f940Header = (F940Header) record;
					break;
				case "f940-details":
					f940.f940Items.add((F940Item) record);
					break;
				case "f940-trail":
					f940.f940Trail = (F940Trail) record;
					break;
				default:
					break;
				}
			}
		}
	}

}
