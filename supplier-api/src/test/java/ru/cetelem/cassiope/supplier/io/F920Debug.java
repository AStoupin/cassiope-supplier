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

import ru.cetelem.cassiope.supplier.io.f920.F920;
import ru.cetelem.cassiope.supplier.io.f920.F920Header;
import ru.cetelem.cassiope.supplier.io.f920.F920Item;
import ru.cetelem.cassiope.supplier.io.f920.F920Trail;

public class F920Debug {
	private static final Logger log = LogManager.getLogger();

	@Test
	public void invoiceF920WriteTest() {
		log.info("invoiceF120WriteTest start");

		F920 f920 = readF920();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F920Debug.class
					.getResourceAsStream("/f920-mapping.xml"));

			BeanWriter out = factory.createWriter("f920-stream", new File(
					"./output/F920.txt"));

			out.write(f920.f920Header);

			for (F920Item item : f920.f920Items) {
				out.write(item);
			}
			out.write(f920.f920Trail);

			out.close();

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("invoiceF120WriteTest finished");

	}

	private F920 readF920() {
		log.info("readF920 start");

		F920 f920 = new F920();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(F920Debug.class
					.getResourceAsStream("/f920-mapping.xml"));

			Reader fstream = new InputStreamReader(
					F920Debug.class.getResourceAsStream("/F920.txt"),
					"windows-1251");

			BeanReader in = factory.createReader("f920-stream", fstream);

			Object record;

			while ((record = in.read()) != null) {

				// process each record
				if ("f920-header".equals(in.getRecordName())) {
					f920.f920Header = (F920Header) record;
				} else if ("f920-item".equals(in.getRecordName())) {
					F920Item f920Item = (F920Item) record;
					f920.f920Items.add(f920Item);
				} else if ("f920-trail".equals(in.getRecordName())) {
					f920.f920Trail = (F920Trail) record;
				}

			}

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("readF920 finished");

		return f920;
	}

}
