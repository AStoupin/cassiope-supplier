package ru.cetelem.cassiope.supplier.io.f120;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

public class F120 {
	
	public F120() {
		
	}
	
	public F120Header f120Header;
	public List<F120Item> f120Items = new ArrayList<F120Item>();
	public F120Trail f120Trail;
	
	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();
		
		result.add(f120Header);
		result.addAll(f120Items);
		result.add(f120Trail);

		return result;
	}
	
	public static class F120Builder{
		F120 f120;
		
		F120Builder(Date batchDate){
			f120 = new F120();
			f120.f120Header = new F120Header(batchDate);			
		}
		
		public static F120Builder createF120Builder(Date batchDate){

			return new F120Builder(batchDate);
		}
		

		public F120Builder addF120Item(String dealerCode, String invoiceID, String vin, long invoiceAmount) {
			return addF120Item(dealerCode, invoiceID, vin, invoiceAmount, null);
		}
		
		public F120Builder addF120Item(String dealerCode, String invoiceID, String vin, long invoiceAmount, Car car) {
			
			F120Item f120Item = new F120Item();
			f120Item.entryType = "DT";
			f120Item.dealerCode =  dealerCode;
			f120Item.invoiceID = invoiceID;
			f120Item.vatInvoiceId = invoiceID;
			f120Item.grCcDate = new Date();
			f120Item.vin = vin;
			f120Item.invoiceAmount = invoiceAmount;
			
			f120Item.car = car;
			
			f120.f120Items.add(f120Item);
			
			return this;
		}
				
		
		public F120 build() {
			this.f120.f120Trail = new F120Trail(this.f120);
			return this.f120;
		}
	}
}
