package enity;

public class Location {
	private int x;
	private int y;
	private double total_amount;
	private double lat;
	private double lng;
	private double lat_span;
	private double lng_span;
	

	
	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public Location(int x, int y, double lat, double lng, double lat_span, double lng_span) {
		super();
		this.x = x;
		this.y = y;
		this.lat = lat;
		this.lng = lng;
		this.lat_span = lat_span;
		this.lng_span = lng_span;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat_span() {
		return lat_span;
	}

	public void setLat_span(double lat_span) {
		this.lat_span = lat_span;
	}

	public double getLng_span() {
		return lng_span;
	}

	public void setLng_span(double lng_span) {
		this.lng_span = lng_span;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x == other.x&&y == other.y)
			return true;
		return false;
	}
	
	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	

}
