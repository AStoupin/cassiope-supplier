package ru.cetelem.cassiope.supplier.io.cfl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

/**
 * 
 * SUPPLIER->BANK <br />
 * Out CFL Invoice (F110)
 *
 */
public class Cfl {
	public CflHeader cflHeader;

	public List<Cfl11Item> cfl11Items = new ArrayList<Cfl11Item>();
	public Cfl11Trail cfl11Trail;

	public List<Cfl22Item> cfl22Items = new ArrayList<Cfl22Item>();
	public Cfl22Trail cfl22Trail;

	public Cfl33Trail cfl33Trail;
	public Cfl44Trail cfl44Trail;

	public CflTrail cflTrail;

	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();

		result.add(cflHeader);
		result.addAll(cfl11Items);
		result.add(cfl11Trail);
		result.addAll(cfl22Items);
		result.add(cfl22Trail);
		result.add(cfl33Trail);
		result.add(cfl44Trail);
		result.add(cflTrail);

		return result;
	}

	public static class CflBuilder {
		private Cfl cfl;
		private String creator;

		CflBuilder(Date batchDate, long sequenceNumber, String creator) {
			cfl = new Cfl();

			this.creator = creator;

			cfl.cflHeader = new CflHeader();
			cfl.cflHeader.batchDate = batchDate;
			cfl.cflHeader.sequenceNumber = sequenceNumber;
			cfl.cflHeader.createrCompany = creator;

		}

		public static CflBuilder createCflBuilder(Date batchDate,
				long sequenceNumber, String creator) {
			return new CflBuilder(batchDate, sequenceNumber, creator);
		}

		public CflBuilder addCfl22Item(String dealerCode, String invoiceNumber,
				String vin, long invoiceAmount, String model, String planCode) {

			return addCfl22Item(dealerCode, invoiceNumber, vin, invoiceAmount,
					invoiceAmount, invoiceAmount, "", "", "", "RUR", null, null, 	// Проверить
					model, planCode, null);
		}

		public CflBuilder addCfl22Item(String dealerCode, String invoiceNumber,
				String vin, long invoiceAmount, 
				long paymentAmount, 
				long vatAmount, 
				String orderNumber, 
				String plateNumber, 
				String businessType, 
				String currency,
				Date financeDate, 
				Date whosaleDate,
				String model, String planCode,
				Car car) {

			Cfl22Item cfl22Item = new Cfl22Item();

			cfl22Item.recordId = "22";
			cfl22Item.creator = creator;
			cfl22Item.dealerCode = dealerCode;
			cfl22Item.invoiceNumber = invoiceNumber;
			cfl22Item.invoiceAmount = invoiceAmount;
			cfl22Item.paymentAmount = paymentAmount;			
			cfl22Item.vatAmount = vatAmount;
			cfl22Item.financeDate = financeDate;
			cfl22Item.whosaleDate = whosaleDate;
			cfl22Item.orderNumber = orderNumber;
			cfl22Item.plateNumber = plateNumber;
			cfl22Item.businessType = businessType;
			cfl22Item.currency = currency;
			
			cfl22Item.vin = vin;
			if (vin != null && !"".equals(vin.trim()))
				cfl22Item.model = vin.substring(6, 10);
			else
				cfl22Item.model = model;
			cfl22Item.planCode = planCode;

			cfl22Item.car = car;

			cfl.cfl22Items.add(cfl22Item);

			return this;
		}

		public CflBuilder addCfl11Item(String dealerCode, String invoiceNumber, String vin,
				long paymentAmount) {

			return addCfl11Item(dealerCode, invoiceNumber, vin, null, null, "RUR", 
					paymentAmount, null);
		}

		public CflBuilder addCfl11Item(String dealerCode, String invoiceNumber, String vin,
				Date financeDate, Date processingDate, String currency,
				long paymentAmount, Car car) {

			Cfl11Item cfl11Item = new Cfl11Item();

			cfl11Item.recordId = "11";
			cfl11Item.creator = creator;
			cfl11Item.dealerCode = dealerCode;
			cfl11Item.invoiceNumber = invoiceNumber;
			cfl11Item.paymentAmount = paymentAmount;
			cfl11Item.vin = vin;
			
			cfl11Item.financeDate = financeDate;
			cfl11Item.processingDate = processingDate;
			cfl11Item.creator = creator;
			cfl11Item.currency = currency;

			cfl11Item.car = car;

			cfl.cfl11Items.add(cfl11Item);

			return this;
		}

		public Cfl build() {

			if (cfl.cfl11Items.size() > 0)
				cfl.cfl11Trail = new Cfl11Trail(creator, cfl.cfl11Items);
			else
				cfl.cfl11Trail = new Cfl11Trail(creator);

			if (cfl.cfl22Items.size() > 0)
				cfl.cfl22Trail = new Cfl22Trail(creator, cfl.cfl22Items);
			else
				cfl.cfl22Trail = new Cfl22Trail(creator);
			
			cfl.cfl33Trail = new Cfl33Trail(creator);
			cfl.cfl44Trail = new Cfl44Trail(creator);

			cfl.cflTrail = new CflTrail(cfl, creator);

			return cfl;
		}
	}

}
