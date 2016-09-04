package com.hcq.web.interceptors;

import com.hcq.utils.LuceneUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class IndexInterceptor implements Interceptor {
	private static final long serialVersionUID = -950104703862413263L;
	
	public IndexInterceptor(){
		
	}

	public void destroy() {

	}

	public void init() {

	}

	public String intercept(ActionInvocation invocation) throws Exception {
		String result = invocation.invoke();
		//String actionName = invocation.getInvocationContext().getName();
		LuceneUtil helloLucene =new LuceneUtil();
		helloLucene.index();
		return result;
	}

}
