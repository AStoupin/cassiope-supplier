package ru.cetelem.cassiope.supplier.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REPAYMENT_ITEM")
public class RepaymentItem {
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)		
	public int id;
	public LocalDate date;
	public double value;
	
	@ManyToOne(targetEntity = Car.class,optional = false,fetch = FetchType.EAGER)
	public Car car;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}  
	
	
}
