package ru.cetelem.cassiope.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAR_MODEL")
public class CarModel {
	
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)		
	public int id;
    @Column(name = "code", nullable = false)		    
	public String code;
    @Column(name = "mame", nullable = false)		    
	public String name;

	public CarModel() {
	
	}
	
	public CarModel(int id, String code, String name) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String toString() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}	
	
}
