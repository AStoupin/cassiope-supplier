package ru.cetelem.cassiope.supplier.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beanio.BeanIOConfigurationException;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.junit.Test;

import ru.cetelem.cassiope.supplier.io.f120.F120;
import ru.cetelem.cassiope.supplier.io.f120.F120Header;
import ru.cetelem.cassiope.supplier.io.f120.F120Item;
import ru.cetelem.cassiope.supplier.io.f120.F120Trail;

public class F120Debug {
	private static final Logger log = LogManager.getLogger();

	@Test
	public void invoiceF120WriteTest() {
		log.info("invoiceF120WriteTest start");

		F120 f120 = readF120();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F120Debug.class
					.getResourceAsStream("/f120-mapping.xml"));

			BeanWriter out = factory.createWriter("f120-stream", new File(
					"./output/f120.txt"));

			out.write(f120.f120Header);

			for (F120Item item : f120.f120Items) {
				out.write(item);
			}
			out.write(f120.f120Trail);

			out.close();

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("invoiceF120WriteTest finished");

	}

	private F120 readF120() {
		log.info("readF120 start");

		F120 f120 = new F120();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F120Debug.class
					.getResourceAsStream("/f120-mapping.xml"));

			Reader fstream = new InputStreamReader(
					F120Debug.class.getResourceAsStream("/F120_20190208_1.txt"),
					"windows-1251");

			BeanReader in = factory.createReader("f120-stream", fstream);

			Object record;

			while ((record = in.read()) != null) {

				// process each record
				if ("f120-header".equals(in.getRecordName())) {
					f120.f120Header = (F120Header) record;
				} else if ("f120-item".equals(in.getRecordName())) {
					F120Item f120Item = (F120Item) record;
					f120.f120Items.add(f120Item);
				} else if ("f120-trail".equals(in.getRecordName())) {
					f120.f120Trail = (F120Trail) record;
				}

			}

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("readF120 finished");

		return f120;
	}

}
