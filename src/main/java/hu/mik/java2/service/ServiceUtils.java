package hu.mik.java2.service;

public class ServiceUtils {
	public static VehicleService getBookService() {
		return new VehicleServiceImpl();
	}
}
