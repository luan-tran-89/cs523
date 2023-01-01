package cs523.miu.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class VehicleRecord implements Serializable {
	
	private String vin;
	private String county;
	private String city;
	private String state;
	private String postCode;
	private String year;
	private String make;
	private String model;
	private String evType;
	private String cafvEligibility;
	
	public VehicleRecord(String vin, String county, String city, String state, String postCode, String year,
			String make, String model, String evType, String cafvEligibility) {
		super();
		this.vin = vin;
		this.county = county;
		this.city = city;
		this.state = state;
		this.postCode = postCode;
		this.year = year;
		this.make = make;
		this.model = model;
		this.evType = evType;
		this.cafvEligibility = cafvEligibility;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEvType() {
		return evType;
	}

	public void setEvType(String evType) {
		this.evType = evType;
	}

	public String getCafvEligibility() {
		return cafvEligibility;
	}

	public void setCafvEligibility(String cafvEligibility) {
		this.cafvEligibility = cafvEligibility;
	}
	
	
	
}
