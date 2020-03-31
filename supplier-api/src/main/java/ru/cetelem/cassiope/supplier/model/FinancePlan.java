package ru.cetelem.cassiope.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FINANCE_PLAN")
public class FinancePlan {

	@Id
	@GeneratedValue
	@Column(name = "Id", nullable = false)
	private int id;
	@Column(name = "code", nullable = false)
	private String code;
	@Column(name = "name", nullable = true)
	private String name;

    
	public FinancePlan() {	}
	
	
	public FinancePlan(String code) {
		this.code = code;
	}
	
	public FinancePlan(String code, String name) {
		this.code = code;
		this.name = name;
	}	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
}
