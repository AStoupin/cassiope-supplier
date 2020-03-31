 package ru.cetelem.cassiope.supplier.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.util.DateUtils;

@Entity
@Table(name = "PAYLOAD")
public class Payload {

	@Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)		
	public int id;

	public PayloadType payloadType;
	public String name;
	public LocalDateTime date;
	public LocalDate processedDate;
	public LocalDate rollbackDate;

	@OneToMany(mappedBy = "payload", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	public List<PayloadItem> payloadItems;
	
	//for payloadType.direction=OUT: NEW->SUBMITTED->PROCESSED | FAILED
	//for IN:  NEW->RECIEVED->PROCESSED | FAILED
	public String state;

	public int sequenceNumber;
	
	public Payload() {
		this.state = "NEW";
		this.date  = LocalDateTime.now();
		this.payloadItems = new ArrayList<PayloadItem>();
	}


	public Payload(PayloadType payloadType, String name, LocalDateTime date) {
		super();
		this.payloadType = payloadType;
		this.name = name;
		this.date = date;
		this.state = "NEW";
		this.payloadItems = new ArrayList<PayloadItem>();
	}


	public PayloadType getPayloadType() {
		return payloadType;
	}


	public void setPayloadType(PayloadType payloadType) {
		this.payloadType = payloadType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.name == null) return false;
        if (this.getClass() != o.getClass()) return false;

        
        Payload payload = (Payload) o;
        if (payload.name==null) return false;

        
        return this.name.equals(payload.name);
    }


	@Override
	public String toString() {
		return "Payload [name=" + name + "]";
	}


	public List<PayloadItem> getPayloadItems() {
		return payloadItems;
	}


	public void setPayloadItems(List<PayloadItem> payloadItems) {
		this.payloadItems = payloadItems;
	}


	public int getSequenceNumber() {
		return sequenceNumber;
	}


	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}		

	public LocalDate getProcessedDate() {
		return processedDate;
	}


	public void setProcessedDate(LocalDate processedDate) {
		this.processedDate = processedDate;
	}


	public LocalDate getRollbackDate() {
		return rollbackDate;
	}


	public void setRollbackDate(LocalDate rollbackDate) {
		this.rollbackDate = rollbackDate;
	}

	public LocalDate getDateOnly() {
		return DateUtils.asLocalDate(date);
	}


	public void setDateOnly(LocalDate date) {
		this.date = DateUtils.asLocalDateTime(date);
	}

}
