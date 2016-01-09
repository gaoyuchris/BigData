package enity;



public class Business {
	private int business_id;
	private String original_business_id;
	
	private String business_name;
	private String business_type;
	
	private int frequency;
	private float total_amount;
	private double lat;
	private double lng;
	private String district;
	
	
	private int placeId;
	
	private int label;
	private int truelabel;
	private int visted;
	private int type;
	private int cluster;
	private double weight;
	private int year;

	
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Business(double lat, double lng,double weight,int year,String business_type) {
		super();
		this.weight=weight;
		this.lat = lat;
		this.lng = lng;
		this.year=year;
		this.business_type=business_type;
	}
	public Business(double lat, double lng, int cluster) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.cluster = cluster;
	}
	public Business(int business_id, String original_business_id, String business_name, String business_type,
			int frequency, float total_amount, double lat, double lng, String district) {
		super();
		this.business_id = business_id;
		this.original_business_id = original_business_id;
		this.business_name = business_name;
		this.business_type = business_type;
		this.frequency = frequency;
		this.total_amount = total_amount;
		this.lat = lat;
		this.lng = lng;
		this.district = district;
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(int business_id) {
		this.business_id = business_id;
	}
	public String getOriginal_business_id() {
		return original_business_id;
	}
	public void setOriginal_business_id(String original_business_id) {
		this.original_business_id = original_business_id;
	}
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public float getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
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
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public int getTruelabel() {
		return truelabel;
	}
	public void setTruelabel(int truelabel) {
		this.truelabel = truelabel;
	}
	public int getVisted() {
		return visted;
	}
	public void setVisted(int visted) {
		this.visted = visted;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	
//	public static double getDistance(Business p1, Business p2){
//		return Math.sqrt((p1.getLat()-p2.getLat())*(p1.getLat()-p2.getLat())) 
//				+ (p1.getLng() - p2.getLng())*(p1.getLng() - p2.getLng());
//	}
//	
	public static double getDistance(Business p1, Business p2){
		return distanceLatLng(p1.getLng(), p1.getLat(), p2.getLng(), p2.getLat());
	}
	@Override
	public String toString() {
		return "(" + lat + ", "+ lng + ")";
	}
	
	/**
	 * �����������������(��γ��)����
	 * 
	 * @param long1
	 *            ��һ�㾭��
	 * @param lat1
	 *            ��һ��γ��
	 * @param long2
	 *            �ڶ��㾭��
	 * @param lat2
	 *            �ڶ���γ��
	 * @return ���ؾ��� ��λ����
	 */
	private static double distanceLatLng(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R;
		R = 6378137; // ����뾶
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2* R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}
	
	
}
