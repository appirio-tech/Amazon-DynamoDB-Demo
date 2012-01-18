package com.cloudspokes.dynamodb.domain;

public class Loan {
	
	int id;
	String name;
	String status;
	double funded_amount;
	String activity;
	String use;
	String country;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getFunded_amount() {
		return funded_amount;
	}
	public void setFunded_amount(double funded_amount) {
		this.funded_amount = funded_amount;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}
