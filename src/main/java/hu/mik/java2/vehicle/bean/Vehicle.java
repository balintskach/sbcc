package hu.mik.java2.vehicle.bean;

import java.util.Date;

//import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_vehicle")
public class Vehicle {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@SequenceGenerator(name="SEQ_GEN", sequenceName = "s_vehicle", allocationSize = 1, initialValue = 50)
	private Integer id; 
	
	@Column(name="licenseplate")
	private String licenseplate;
	
	@Column(name="owner")
	private String owner;
	
	@Column(name="email")
	private String email;
	
	@Column(name="registrydate")
	private Date registrydate;
	
	@Column(name="jobtype")
	private String jobtype;
	
	@Column(name="active")
	private Integer active;
	
	@Column(name="requiredetails")
	private Integer requiredetails;
	
	@Column(name="status")
	private Integer status;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getRegistrydate() {
		return registrydate;
	}

	public void setRegistrydate(Date registrydate) {
		this.registrydate = registrydate;
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getRequiredetails() {
		return requiredetails;
	}

	public void setRequiredetails(Integer requiredetails) {
		this.requiredetails = requiredetails;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", licenseplate=" + licenseplate + ", owner=" + owner + ", email=" + email
				+ ", registrydate=" + registrydate + ", jobtype=" + jobtype + ", active=" + active + ", requiredetails="
				+ requiredetails + ", status=" + status + "]";
	}



	
}
