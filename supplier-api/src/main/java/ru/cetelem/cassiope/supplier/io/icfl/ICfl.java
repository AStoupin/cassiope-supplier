package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

/**
 * 
 * BANK->SUPPLIER <br />
 * In ICFL Invoice
 *
 */
public class ICfl {
	public ICflHeader icflHeader;

	public List<ICfl11Item> icfl11Items = new ArrayList<ICfl11Item>();
	public ICfl11Trail icfl11Trail;

	public List<ICfl22Item> icfl22Items = new ArrayList<ICfl22Item>();
	public ICfl22Trail icfl22Trail;

	public ICfl33Trail icfl33Trail;
	public ICfl44Trail icfl44Trail;

	public ICflTrail icflTrail;

	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();

		result.add(icflHeader);
		result.addAll(icfl11Items);
		result.add(icfl11Trail);
		result.addAll(icfl22Items);
		result.add(icfl22Trail);
		result.add(icfl33Trail);
		result.add(icfl44Trail);
		result.add(icflTrail);

		return result;
	}

	public static class ICflBuilder {
		private ICfl icfl;
		private String creator;

		ICflBuilder(Date batchDate, long sequenceNumber, String creator) {
			icfl = new ICfl();

			this.creator = creator;

			icfl.icflHeader = new ICflHeader();
			icfl.icflHeader.batchDate = batchDate;
			icfl.icflHeader.sequnceNumber = sequenceNumber;

		}

		public static ICflBuilder createICflBuilder(Date batchDate,
				long sequenceNumber, String creator) {
			return new ICflBuilder(batchDate, sequenceNumber, creator);
		}

		public ICflBuilder addICfl22Item(String dealerCode,
				String invoiceNumber, String vin, long invoiceAmount,
				String model, String planCode) {

			return addICfl22Item(dealerCode, invoiceNumber, vin, invoiceAmount,
					model, planCode, null);
		}

		public ICflBuilder addICfl22Item(String dealerCode,
				String invoiceNumber, String vin, long invoiceAmount,
				String model, String planCode, Car car) {

			ICfl22Item icfl22Item = new ICfl22Item();

			icfl22Item.recordId = "22";
			icfl22Item.creator = creator;
			icfl22Item.dealerCode = dealerCode;
			icfl22Item.invoiceNumber = invoiceNumber;
			icfl22Item.invoiceAmount = invoiceAmount;
			icfl22Item.vin = vin;
			if (vin != null && !"".equals(vin.trim()))
				icfl22Item.model = vin.substring(6, 10);
			else
				icfl22Item.model = model;
			icfl22Item.planCode = planCode;

			icfl22Item.car = car;

			icfl.icfl22Items.add(icfl22Item);

			return this;
		}

		public ICflBuilder addICfl11Item(String dealerCode, String invoiceNumber, String vin,
				long paymentAmount) {

			return addICfl11Item(dealerCode, invoiceNumber, vin, paymentAmount, null);
		}

		public ICflBuilder addICfl11Item(String dealerCode, String invoiceNumber, String vin,
				long paymentAmount, Car car) {

			ICfl11Item icfl11Item = new ICfl11Item();

			icfl11Item.recordId = "11";
			icfl11Item.creator = creator;
			icfl11Item.dealerCode = dealerCode;
			icfl11Item.invoiceNumber = invoiceNumber;
			icfl11Item.paymentAmount = paymentAmount;
			icfl11Item.vin = vin;

			icfl11Item.car = car;

			icfl.icfl11Items.add(icfl11Item);

			return this;
		}

		public ICfl build() {

			if (icfl.icfl11Items.size() > 0)
				icfl.icfl11Trail = new ICfl11Trail(creator, icfl.icfl11Items);
			else
				icfl.icfl11Trail = new ICfl11Trail(creator);

			if (icfl.icfl22Items.size() > 0)
				icfl.icfl22Trail = new ICfl22Trail(creator, icfl.icfl22Items);
			else
				icfl.icfl22Trail = new ICfl22Trail(creator);

			icfl.icfl33Trail = new ICfl33Trail(creator);
			icfl.icfl44Trail = new ICfl44Trail(creator);
			icfl.icflTrail = new ICflTrail(icfl, creator);

			return icfl;
		}
	}

}
