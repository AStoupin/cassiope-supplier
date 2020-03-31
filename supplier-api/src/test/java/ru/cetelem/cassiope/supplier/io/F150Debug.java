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

import ru.cetelem.cassiope.supplier.io.f150.F150;
import ru.cetelem.cassiope.supplier.io.f150.F150Header;
import ru.cetelem.cassiope.supplier.io.f150.F150Item;
import ru.cetelem.cassiope.supplier.io.f150.F150Trail;

public class F150Debug {
	private static final Logger log = LogManager.getLogger();

	@Test
	public void invoiceF150WriteTest() {
		log.info("invoiceF150WriteTest start");

		F150 f150 = readF150();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F150Debug.class
					.getResourceAsStream("/f150-mapping.xml"));

			BeanWriter out = factory.createWriter("f150-stream", new File(
					"./output/f150.txt"));

			out.write(f150.f150Header);

			for (F150Item item : f150.f150Items) {
				out.write(item);
			}
			out.write(f150.f150Trail);

			out.close();

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("invoiceF150WriteTest finished");
	}

	private F150 readF150() {
		log.info("readF150 start");

		F150 f150 = new F150();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F150Debug.class
					.getResourceAsStream("/f150-mapping.xml"));

			Reader fstream = new InputStreamReader(
					F150Debug.class.getResourceAsStream("/F150_20190816.txt"),
					"windows-1251");
			BeanReader in = factory.createReader("f150-stream", fstream);

			Object record;

			while ((record = in.read()) != null) {

				// process each record
				if ("f150-header".equals(in.getRecordName())) {
					f150.f150Header = (F150Header) record;
				} else if ("f150-item".equals(in.getRecordName())) {
					F150Item f150Item = (F150Item) record;
					f150.f150Items.add(f150Item);
				} else if ("f150-trail".equals(in.getRecordName())) {
					f150.f150Trail = (F150Trail) record;
				}

			}

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("readF150 finished");

		return f150;
	}

}
