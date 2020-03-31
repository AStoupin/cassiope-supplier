package ru.cetelem.cassiope.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PAYLOAD_ITEM")
public class PayloadItem {
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)	
	public int id;
	@ManyToOne(targetEntity = Payload.class,optional = false,fetch = FetchType.EAGER)
	public Payload payload;
	@ManyToOne(targetEntity = Car.class,optional = true,fetch = FetchType.EAGER)
	public Car car;
	@ManyToOne(targetEntity = DealerLimit.class,optional = true,fetch = FetchType.EAGER)
	public DealerLimit limit;

	//For CFL = 22 - initialization, 11 - cancel 
	//
	public String eventCode;
	public int sequenceNum;
	public String errorDescr;

	@Column(length = 3000)
	public String source;	
	public String sourceType;	
	
	public PayloadItem() {
	}	
	
	public PayloadItem(Payload payload) {
		super();
		this.payload = payload;
		this.car = null;
		this.eventCode = null;
	}
	
	public PayloadItem(Payload payload, String source, String sourceType) {
		super();
		this.payload = payload;
		this.sourceType  = sourceType;		
	}
	
	public PayloadItem(Payload payload, Car car, String eventCode) {
		super();
		this.payload = payload;
		this.car = car;
		this.eventCode = eventCode;
	}

	public PayloadItem(Payload payload, DealerLimit limit, String eventCode) {
		super();
		this.payload = payload;
		this.limit = limit;
		this.eventCode = eventCode;
	}
	
	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}	
	
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
        if (o == null) return false;
        if (car==null) return false;
        if (payload==null) return false;

        if (this.getClass() != o.getClass()) return false;
        
        PayloadItem item = (PayloadItem) o;
        if (item.car==null) return false;
        if (item.payload==null) return false;
        
        return (car.equals(item.car) 
          && payload.equals(item.payload));
    }

	@Override
	public String toString() {
		return "PayloadItem [payload=" + payload + ", car=" + car + ", eventCode=" + eventCode + "]";
	}

	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public String getErrorDescr() {
		return errorDescr;
	}

	public void setErrorDescr(String errorDescr) {
		this.errorDescr = errorDescr;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public DealerLimit getLimit() {
		return limit;
	}

	public void setLimit(DealerLimit limit) {
		this.limit = limit;
	}	
	
	public Dealer getDealer() {
		return limit.getDealer();
	}

}
