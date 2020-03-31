package ru.cetelem.cassiope.supplier.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beanio.BeanIOConfigurationException;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.junit.Assert;
import org.junit.Test;

import ru.cetelem.cassiope.supplier.io.cfl.Cfl;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl11Item;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl11Trail;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl22Trail;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl33Trail;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl44Trail;
import ru.cetelem.cassiope.supplier.io.cfl.CflHeader;
import ru.cetelem.cassiope.supplier.io.cfl.CflTrail;

public class CflTest {
	private static final Logger log = LogManager.getLogger();

	@Test
	public void invoiceCfl22WriteTest() {
		log.info("invoiceInitTest");

		Cfl cfl = readCfl();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(CflTest.class.getResourceAsStream("/cfl-mapping.xml"));

			BeanWriter out = factory.createWriter("cfl-stream", new File(
					"./output/cfl.txt"));

			out.write(cfl.cflHeader);
			for (Cfl11Item item : cfl.cfl11Items) {
				item.filler = "";
				item.paymentAmount = 1234;
				out.write(item);
			}
			out.write(cfl.cfl11Trail);
			for (Cfl22Item item : cfl.cfl22Items) {
				item.filler = "";
				item.invoiceAmount = 12345;
				out.write(item);
			}
			out.write(cfl.cfl22Trail);
			out.write(cfl.cfl33Trail);
			out.write(cfl.cfl44Trail);
			out.write(cfl.cflTrail);
			out.close();

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("createReport finished");

	}

	private Cfl readCfl() {
		log.info("readCfl start");
		Cfl cfl = new Cfl();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(CflTest.class.getResourceAsStream("/cfl-mapping.xml"));

			Reader fstream = new InputStreamReader(
					CflTest.class.getResourceAsStream("/CFL22_20190207.txt"),
					"windows-1251");

			BeanReader in = factory.createReader("cfl-stream", fstream);

			Object record;

			while ((record = in.read()) != null) {

				// process each record
				if ("cfl-header".equals(in.getRecordName())) {
					cfl.cflHeader = (CflHeader) record;
				} else if ("cfl-11-item".equals(in.getRecordName())) {
					Cfl11Item cflItem = (Cfl11Item) record;
					Assert.assertTrue(100000000 == cflItem.paymentAmount);
					cfl.cfl11Items.add(cflItem);
				} else if ("cfl-11-trail".equals(in.getRecordName())) {
					cfl.cfl11Trail = (Cfl11Trail) record;
				} else if ("cfl-22-item".equals(in.getRecordName())) {
					Cfl22Item cflItem = (Cfl22Item) record;
					Assert.assertTrue(100000000 == cflItem.invoiceAmount);
					cfl.cfl22Items.add(cflItem);
				} else if ("cfl-22-trail".equals(in.getRecordName())) {
					cfl.cfl22Trail = (Cfl22Trail) record;
				} else if ("cfl-33-trail".equals(in.getRecordName())) {
					cfl.cfl33Trail = (Cfl33Trail) record;
				} else if ("cfl-44-trail".equals(in.getRecordName())) {
					cfl.cfl44Trail = (Cfl44Trail) record;
				} else if ("cfl-trail".equals(in.getRecordName())) {
					cfl.cflTrail = (CflTrail) record;
				}
			}

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("readCfl finished");
		return cfl;
	}

	@Test
	public void invoiceCfl11WriteTest() {
		log.info("invoiceCfl11InitTest");

		Cfl cfl = readCfl11();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(CflTest.class.getResourceAsStream("/cfl-mapping.xml"));

			BeanWriter out = factory.createWriter("cfl-stream", new File(
					"./output/cfl-11.txt"));

			out.write(cfl.cflHeader);
			for (Cfl11Item item : cfl.cfl11Items) {
				item.filler = "";
				item.paymentAmount = 1234;
				out.write(item);
			}
			out.write(cfl.cfl11Trail);
			for (Cfl22Item item : cfl.cfl22Items) {
				item.filler = "";
				item.invoiceAmount = 12345;
				out.write(item);
			}
			out.write(cfl.cfl22Trail);
			out.write(cfl.cfl33Trail);
			out.write(cfl.cfl44Trail);
			out.write(cfl.cflTrail);
			out.close();

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("createReport Cfl11 finished");

	}

	private Cfl readCfl11() {
		log.info("readCfl-11 start");
		Cfl cfl = new Cfl();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			factory.load(CflTest.class.getResourceAsStream("/cfl-mapping.xml"));

			Reader fstream = new InputStreamReader(
					CflTest.class.getResourceAsStream("/CFL11_25072018.txt"),
					"windows-1251");
			BeanReader in = factory.createReader("cfl-stream", fstream);

			Object record;

			while ((record = in.read()) != null) {

				// process each record
				if ("cfl-header".equals(in.getRecordName())) {
					cfl.cflHeader = (CflHeader) record;
				} else if ("cfl-11-item".equals(in.getRecordName())) {
					Cfl11Item cflItem = (Cfl11Item) record;
					log.info("readCfl-11 record = " + cflItem.paymentAmount);
					Assert.assertTrue(100000000 == cflItem.paymentAmount);
					cfl.cfl11Items.add(cflItem);
				} else if ("cfl-11-trail".equals(in.getRecordName())) {
					cfl.cfl11Trail = (Cfl11Trail) record;
				} else if ("cfl-22-item".equals(in.getRecordName())) {
					Cfl22Item cflItem = (Cfl22Item) record;
					Assert.assertTrue(100000000 == cflItem.invoiceAmount);
					cfl.cfl22Items.add(cflItem);
				} else if ("cfl-22-trail".equals(in.getRecordName())) {
					cfl.cfl22Trail = (Cfl22Trail) record;
				} else if ("cfl-33-trail".equals(in.getRecordName())) {
					cfl.cfl33Trail = (Cfl33Trail) record;
				} else if ("cfl-44-trail".equals(in.getRecordName())) {
					cfl.cfl44Trail = (Cfl44Trail) record;
				} else if ("cfl-trail".equals(in.getRecordName())) {
					cfl.cflTrail = (CflTrail) record;
				}
			}

		} catch (BeanIOConfigurationException | IOException e) {
			log.error("exportResaleApplications error", e);
			throw new RuntimeException("exportResaleApplications error", e);
		}

		log.info("readCfl-11 finished");
		return cfl;
	}
/*
	private List<Cfl11Item> prepareCfl11Items() {
		List<Cfl11Item> result = new ArrayList<Cfl11Item>();

		Cfl11Item item = new Cfl11Item();
		item.creator = "UR";
		item.currency = "RUR";
		item.financeDate = (new GregorianCalendar(2019, 5, 15)).getTime();
		item.processingDate = (new GregorianCalendar(2019, 5, 15)).getTime();
		item.paymentAmount = 1245;
		item.vin = "a243523462362346";

		result.add(item);

		return result;
	}

	private List<Cfl22Item> prepareCfl22Items() {
		List<Cfl22Item> result = new ArrayList<Cfl22Item>();

		Cfl22Item item = new Cfl22Item();
		item.businessType = "";
		item.creator = "UR";
		item.currency = "RUR";
		item.financeDate = (new GregorianCalendar(2019, 5, 15)).getTime();
		item.invoiceAmount = 12456;
		item.model = "MOD";
		item.planCode = "PC";
		item.vin = "a243523462362346";

		result.add(item);

		return result;
	}
*/
}
