package hu.mik.java2.service;

import java.util.List;

import hu.mik.java2.vehicle.bean.Vehicle;


public interface VehicleService {
	
	public List<Vehicle> listVehicles();
	
	public Vehicle getVehicleById(Integer id);
	
	public Vehicle saveVehicle(Vehicle vehicle);
	
	public Vehicle updateVehicle(Vehicle vehicle);
	
	public void deleteVehicle(Vehicle vehicle);
	
	public List<Vehicle> findByLicenseplateLike(String licensePlate);
	
	public List<Vehicle> findByActiveLike(Integer active);
}
