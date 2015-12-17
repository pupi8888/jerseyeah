package zettasoftware.jerseyeah.model;

public class Position {
	
	//Google maps coordinate format : latitude, longitude
	
	private double latitude;
	private double longitude;
	
	
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public Position(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position() {
		super();
	}
	
	
	
	

}
