package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class test {

	/**Java多线程读大文件
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t1=new Thread(new MultiThread(),"A");
//		Thread t2=new Thread(new MultiThread(),"B");
//		Thread t3=new Thread(new MultiThread(),"A");
//		Thread t4=new Thread(new MultiThread(),"B");
//		Thread t5=new Thread(new MultiThread(),"A");
//		Thread t6=new Thread(new MultiThread(),"B");
//		Thread t7=new Thread(new MultiThread(),"A");
//		Thread t8=new Thread(new MultiThread(),"B");
//		Thread t9=new Thread(new MultiThread(),"A");
//		Thread t10=new Thread(new MultiThread(),"B");
		long start=System.currentTimeMillis();
		int threadNumber = 2;     
		long startTime= System.currentTimeMillis();//开始时间     
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);         
		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
//		t6.start();
//		t7.start();
//		t8.start();
//		t9.start();
//		t10.start();
//		 while (true)
//	        {
//	            if ( Thread.activeCount() == 1 ) break;
//	        }
		long end=System.currentTimeMillis();
		System.out.println("执行总时间"+(end-start));
	}

}


 class MultiThread implements Runnable{	
	private static BufferedReader br = null;
	
	static{
		try {
			File file=new File("f://c.txt");
			InputStreamReader fr = new InputStreamReader(new FileInputStream(file),"gb2312");
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String request(String httpUrl) {
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		String result = null;
	    try {
	    	URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	        		.openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "5a6e02f1342c317636e347be7ffc5953");
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
	
	public void run() {
		String line = null;
		int count = 0;
		while(true) {
			//System.out.println(Thread.currentThread().getName());
			try {
				while((line = br.readLine()) != null) {
//						if(count<15) {
							String []temps=line.split("\t");
//							System.out.println(temps[0]);
							String httpUrl = "http://apis.baidu.com/apistore/lbswebapi/geocodingapi";
//							String parm1="还款";
							String param = "?callback=%E2%80%98%E2%80%99&address="+URLEncoder.encode(temps[0], "UTF-8") + "&city=%E5%8C%97%E4%BA%AC%E5%B8%82"; 
							httpUrl=httpUrl+param;
//							String httpArg = "callback=%E2%80%98%E2%80%99&address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&city=%E5%8C%97%E4%BA%AC%E5%B8%82";
//							System.out.println(httpUrl);
							String jsonResult = request(httpUrl);
							System.out.print(temps[0]+" "+jsonResult);	
							count++;
//						}else {
//							count = 0;
//							break;
//						}
				}
//					display(this.list);
			} catch (IOException e) {
					e.printStackTrace();
			}
//			try {
//				Thread.sleep(1);
//				display(this.list);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			if(line == null)
				break;
		}
		
		
	}
	
//	public void display(List<String> list) {
//		for(String str:list) {
//			System.out.println(str);
//		}
//		System.out.println(list.size());
//	}
	
}