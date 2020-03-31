package ru.cetelem.cassiope.supplier.io.f910;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

/**
 * Список профинансированных за день машин <br />
 * BANK->SUPPLIER <br />
 * IN F910 Daily financing done	<br />
 * Отправляется банком перед закрытия дня (~18-00)
 */
public class F910 {
	
	public F910() {
		
	}
	
	public F910Header f910Header;
	public List<F910Item> f910Items = new ArrayList<F910Item>();
	public F910Trail f910Trail;
	
	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();
		
		result.add(f910Header);
		result.addAll(f910Items);
		result.add(f910Trail);

		return result;
	}
	
	public static class F910Builder{
		F910 f910;
		
		F910Builder(Date batchDate){
			f910 = new F910();
			f910.f910Header = new F910Header(batchDate);			
		}
		
		public static F910Builder createF910Builder(Date batchDate){

			return new F910Builder(batchDate);
		}
		

		public F910Builder addF910Item(String dealerCode, String invoiceID, String vin, long invoiceAmount,
				String vatInvoiceId, Date financingDate) {
			return addF910Item(dealerCode, invoiceID, vin, invoiceAmount, vatInvoiceId, financingDate, null);
		}
		
		public F910Builder addF910Item(String dealerCode, String invoiceID, String vin, long invoiceAmount,
				String vatInvoiceId, Date financingDate, Car car) {
			
			F910Item F910Item = new F910Item();
			F910Item.entryType = "DA";
			F910Item.dealerCode =  dealerCode;
			F910Item.carFinModel = "  ";
			F910Item.invoiceID = invoiceID;
			
			F910Item.vatInvoiceId = vatInvoiceId;			
			F910Item.vin = vin;
			F910Item.invoiceAmount = invoiceAmount;
			F910Item.financingDate = financingDate;
			
			F910Item.car = car;
			
			f910.f910Items.add(F910Item);
			
			return this;
		}
				
		
		public F910 build() {
			this.f910.f910Trail = new F910Trail(this.f910);
			return this.f910;
		}
	}
}
