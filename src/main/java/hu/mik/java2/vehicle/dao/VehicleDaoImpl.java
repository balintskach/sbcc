package hu.mik.java2.vehicle.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.mik.java2.vehicle.bean.Vehicle;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class VehicleDaoImpl implements VehicleDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Vehicle findByid(Integer id) {
		
		return this.entityManager.find(Vehicle.class, id);
	}

	@Override
	public List<Vehicle> findAll() {
		return this.entityManager.createQuery("SELECT v FROM Vehicle v ORDER BY v.id", Vehicle.class).getResultList();
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		if(vehicle.getId() == null) {
			this.entityManager.persist(vehicle);
			
			return vehicle;
		} else {
			return this.entityManager.merge(vehicle);
		}
	}

	@Override
	public void delete(Vehicle vehicle) {
		if(!this.entityManager.contains(vehicle)) {
		this.entityManager.merge(vehicle);
	}
	
	this.entityManager.remove(vehicle);
		
	}

	@Override
	public List<Vehicle> findByLicenseplateLike(String licensePlate) {
		return this.entityManager
				.createQuery("SELECT v FROM Vehicle v WHERE v.licenseplate LIKE :licensePlate", Vehicle.class)
				.setParameter("licensePlate", licensePlate)
				.getResultList();
		}



}
