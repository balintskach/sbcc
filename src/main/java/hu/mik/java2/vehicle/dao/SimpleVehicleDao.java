package hu.mik.java2.vehicle.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.mik.java2.vehicle.bean.Vehicle;

public interface SimpleVehicleDao extends JpaRepository<Vehicle, Integer> {
	public List<Vehicle> findByLicenseplateLike(String licenseplate);
	
	public List <Vehicle> findByActiveLike(Integer active);
}
