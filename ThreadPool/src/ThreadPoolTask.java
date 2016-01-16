import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ThreadPoolTask implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attachData;
	private String attachData2;
	private BufferedWriter writer;
	private ReentrantLock lock;
	private String line;
	ThreadPoolTask(ReentrantLock lock,String tasks,String task1,BufferedWriter writer, String line) {
		this.lock=lock;
		this.attachData = tasks;
		this.attachData2=task1;
		this.writer=writer;
		this.line=line;
	}
	public  String request(String httpUrl) {
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		String result = null;
	    try {
	    	URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	        		.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	public String JsonToString(String json)
	{
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject obj=jsonObject.getJSONObject("result");
		if(!obj.isNullObject())
		{
			JSONObject location=obj.getJSONObject("location");
			String lng=location.getString("lng");
			String lat=location.getString("lat");
			return lat+","+lng;
		}
		else
		{
			return "null,null";
		}
	}
	public void run() {
		
		String temp=request(attachData);
		String loaction=JsonToString(temp);
		String result=line+","+loaction+"\n";
		System.out.print(result);
		try {
				writer.write(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
	}
	public Object getTask() {
		return this.attachData;
	}
}