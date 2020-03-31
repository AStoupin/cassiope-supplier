package ru.cetelem.cassiope.supplier.io.f150;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

public class F150 {

	public F150() {

	}

	public F150Header f150Header;
	public List<F150Item> f150Items = new ArrayList<F150Item>();
	public F150Trail f150Trail;

	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();

		result.add(f150Header);
		result.addAll(f150Items);
		result.add(f150Trail);

		return result;
	}

	public static class F150Builder {
		F150 f150;

		F150Builder(Date batchDate, long sequenceNumber) {
			f150 = new F150();
			f150.f150Header = new F150Header(batchDate);
			f150.f150Header.sequenceNumber = sequenceNumber;
		}

		public static F150Builder createF150Builder(Date batchDate, long sequenceNumbe) {

			return new F150Builder(batchDate, sequenceNumbe);
		}

		public F150Builder addF150Item(String location, String companyCode, 
				String dealerCode, String vin, Date date, String localCodeDHL, String eptsNumber) {
			return addF150Item(location, companyCode, 
					dealerCode, vin, date, localCodeDHL, eptsNumber, null);
		}

		public F150Builder addF150Item(String location, String companyCode, 
				String dealerCode, String vin, Date date, String localCodeDHL, String eptsNumber, Car car) {

			F150Item f150Item = new F150Item();
			f150Item.entryType = "DT";
			f150Item.dealerCode = dealerCode;
			f150Item.location = location;
			f150Item.dealerCode = dealerCode;
			f150Item.vin = vin;
			f150Item.date = date;
			f150Item.localCodeDHL = localCodeDHL;	// всегда должно быть “MOW1”?	да
			f150Item.eptsNumber = eptsNumber;

			f150Item.car = car;

			f150.f150Items.add(f150Item);

			return this;
		}

		public F150 build() {
			this.f150.f150Trail = new F150Trail(this.f150);
			return this.f150;
		}
	}
}
