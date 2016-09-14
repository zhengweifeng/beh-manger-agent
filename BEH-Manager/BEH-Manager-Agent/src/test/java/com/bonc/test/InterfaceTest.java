package com.bonc.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bonc.entity.Disk;
import com.bonc.entity.ExportEntityInterface;

public class InterfaceTest {

	
	public static void main(String[] args) {
		
		
		Object obj = new Disk();
		Class<?>[] clazzs = obj.getClass().getInterfaces();
		System.out.println(clazzs.length);
		for(Class c : clazzs) {
			System.out.println(c.getName());
		}
		System.out.println(ArrayUtils.contains(clazzs, ExportEntityInterface.class));
		String url = "http://localhost:8080/api/v4.0/warn/put/message" + "?dataId=ssss" ;
//		URL httpUrl = new URL(url);
//		 // 打开和URL之间的连接
//        URLConnection connection = httpUrl.openConnection();
//       
//        // 设置通用的请求属性
//        connection.setRequestProperty("accept", "*/*");
//        connection.setRequestProperty("connection", "Keep-Alive");
//        connection.setRequestProperty("user-agent",
//                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        // 建立实际的连接
//        connection.connect();
//        // 获取所有响应头字段
//        System.out.println(connection.getContentType());
//        System.out.println(connection.getContentEncoding());
//        System.out.println(connection.getContent());
//        System.out.println(connection.getHeaderFields());
//        System.out.println(connection.getContentEncoding());
        
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		int i = 3;
		while(i>0){
			try {
				response = httpClient.execute(request);
			} catch (Exception e) {
				System.out.println("http get : " + url + " has expection " + e.toString() + " ,try again after sleep 10s");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				i--;
				continue;
			} 
			if(response.getStatusLine().getStatusCode() == 200) {
				break;
			} else {
				System.out.println("http get : " + url + " has error code " +  
						response.getStatusLine().getStatusCode() + ", try again after sleep 10s");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			
		}
		System.out.println(response.getStatusLine().getStatusCode());
	}
}
