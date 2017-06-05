package hu.mik.java2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.mik.java2.vehicle.bean.Vehicle;
import hu.mik.java2.vehicle.dao.SimpleVehicleDao;


@Service("vehicleServiceImpl")
public class VehicleServiceImpl implements VehicleService{

	@Autowired
	private SimpleVehicleDao vehicleDao;
	
	@Override
	public List<Vehicle> listVehicles() {
		return this.vehicleDao.findAll();
	}
	
	@Override
	public Vehicle getVehicleById(Integer order_id) {
		return this.vehicleDao.findOne(order_id);
	}

	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		return this.vehicleDao.save(vehicle);
	}

	@Override
	public Vehicle updateVehicle(Vehicle vehicle) {
		return this.vehicleDao.save(vehicle);
	}

	@Override
	public void deleteVehicle(Vehicle vehicle) {
		this.vehicleDao.delete(vehicle);
		
	}

	@Override
	public List<Vehicle> findByLicenseplateLike(String licensePlate) {
		return this.vehicleDao.findByLicenseplateLike(licensePlate);
	}

	@Override
	public List<Vehicle> findByActiveLike(Integer active) {
		return this.vehicleDao.findByActiveLike(active);
	}



}
