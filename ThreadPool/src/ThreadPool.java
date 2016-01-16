import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;  
import java.util.concurrent.ThreadPoolExecutor;  
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;  
public class ThreadPool { 
//	private static ReentrantLock lock = new ReentrantLock();
	static ArrayList<String> test=new ArrayList<String> ();
	static int i=0;
   
    public static void main(String[] args) {  
        // 构造一个线程池  
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8,10,3,  
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(150000),  
                new ThreadPoolExecutor.DiscardOldestPolicy());
       /* ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
        		long keepAliveTime, TimeUnit unit,
        		BlockingQueue<Runnable> workQueue,
        		RejectedExecutionHandler handler)
        		corePoolSize： 线程池维护线程的最少数量
        		maximumPoolSize：线程池维护线程的最大数量
        		keepAliveTime： 线程池维护线程所允许的空闲时间
        		unit： 线程池维护线程所允许的空闲时间的单位
        		workQueue： 线程池所使用的缓冲队列
        		handler： 线程池对拒绝任务的处理策略*/
        ReentrantLock lock = new ReentrantLock();
        long start=System.currentTimeMillis();
        int i=0;
        try {
        	String line=null;
        	File file=new File("d://new_closed_mchnt.txt");
			InputStreamReader fr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			BufferedReader br = new BufferedReader(fr);
			File f = new File("d://second_quest1.txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter write=new FileWriter(f);
			BufferedWriter writer=new BufferedWriter(write); 
			while(null!=(line=br.readLine()))
			{ 
				String []temps=line.split(",");
				System.out.println(temps[3]);
				String httpUrl ="http://api.map.baidu.com/geocoder/v2/?address=";
				String param =URLEncoder.encode(temps[3], "UTF-8")+"&region=北京市&output=json&ak=Hs0hVsQ8TQfKm1aULroPPn9G";
				httpUrl=httpUrl+param;
				threadPool.execute(new ThreadPoolTask(lock,httpUrl,temps[3],writer,line));
			}
			threadPool.shutdown();
			
			while(true){
                if(threadPool.isTerminated()){
                    System.out.println("所有的子线程都结束了！");
                    break;
                }
			}
			writer.flush();
			long end=System.currentTimeMillis();
            System.out.println(end-start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
}  
