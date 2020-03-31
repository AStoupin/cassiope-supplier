package ru.cetelem.cassiope.supplier.io.f940;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.cetelem.cassiope.supplier.model.Dealer;

/**
 * Список погашенных за день машин <br />
 * BANK->SUPPLIER <br />
 * IN F940 Dealer limits	<br />
 * Отправляется банком перед закрытия дня (~18-00)
 */
public class F940 {
	
	public F940() {
		
	}
	
	public F940Header f940Header;
	public List<F940Item> f940Items = new ArrayList<F940Item>();
	public F940Trail f940Trail;
	
	public List<Object> getAsList() {
		List<Object> result = new ArrayList<Object>();
		
		result.add(f940Header);
		result.addAll(f940Items);
		result.add(f940Trail);

		return result;
	}
	
	public static class F940Builder{
		F940 f940;
		
		F940Builder(Date batchDate){
			f940 = new F940();
			f940.f940Header = new F940Header(batchDate);			
		}
		
		public static F940Builder createF940Builder(Date batchDate){

			return new F940Builder(batchDate);
		}

		public F940Builder addF940Item(String dealerCode, String invoiceID, long hardLimitATLAS, 
			long softLimitATLAS, long totalFinanced, long amountAvailable, long sublimit_DE_CC, 
			long totalFinanced_DE_CC, long amountAvailable_DE_CC) {
			
				return addF940Item(dealerCode, invoiceID, hardLimitATLAS, softLimitATLAS, totalFinanced, 
						amountAvailable, sublimit_DE_CC, totalFinanced_DE_CC, amountAvailable_DE_CC, null);
		}
		
		public F940Builder addF940Item(String dealerCode, String invoiceID, long hardLimitATLAS, 
				long softLimitATLAS, long totalFinanced, long amountAvailable, long sublimit_DE_CC, 
				long totalFinanced_DE_CC, long amountAvailable_DE_CC, Dealer dealer) {
			
			F940Item F940Item = new F940Item();
			
			F940Item.entryType = "DT";
			F940Item.dealerCode =  dealerCode;
			F940Item.hardLimitATLAS = hardLimitATLAS;
			F940Item.softLimitATLAS = softLimitATLAS;
			
			F940Item.totalFinanced = totalFinanced;
			F940Item.amountAvailable = amountAvailable;
			F940Item.sublimit_DE_CC = sublimit_DE_CC;
			F940Item.totalFinanced_DE_CC = totalFinanced_DE_CC;
			
			F940Item.amountAvailable_DE_CC = amountAvailable_DE_CC;
			
			F940Item.dealer = dealer;
			
			f940.f940Items.add(F940Item);
			
			return this;
		}
				
		
		public F940 build() {
			this.f940.f940Trail = new F940Trail(this.f940);
			return this.f940;
		}
	}
}
