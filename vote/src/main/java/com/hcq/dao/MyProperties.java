package com.hcq.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hcq.utils.LogUtil;


public class MyProperties extends Properties{

	private static final long serialVersionUID = 8677195326257805172L;

	private static MyProperties myproperties;
	
	private static String propertyFileName="db.properties";
	
	private MyProperties(){
		//类加载器，是一个类，这个类用于处理类路径下的一些操作
		InputStream stream=MyProperties.class.getClassLoader().getResourceAsStream(propertyFileName);
		try{
			load(stream);
		}catch(IOException e){
			LogUtil.logger.error("error to read properties file",e);
			throw new RuntimeException(e);
		}
	}
	//确保单例
	//synchronized : 当多线程访问时，保证一次只能有一个请求访问这个方法
	public synchronized static MyProperties getInstance(){
		if(myproperties==null){
			myproperties=new MyProperties();
		}
		return myproperties;
	}
	
}