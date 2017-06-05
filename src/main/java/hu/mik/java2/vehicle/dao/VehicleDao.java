package hu.mik.java2.vehicle.dao;

import java.util.List;



import hu.mik.java2.vehicle.bean.Vehicle;

public interface VehicleDao{
	

	public Vehicle findByid(Integer id);
	
	public List<Vehicle> findAll();
	
	public Vehicle save(Vehicle vehicle);
	
	public void delete(Vehicle vehicle);
	
	public List<Vehicle> findByLicenseplateLike(String licensePlate);
}
