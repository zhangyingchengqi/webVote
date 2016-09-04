package com.hcq.web.interceptors;

import com.hcq.utils.LuceneUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class CreateIndex extends MethodFilterInterceptor{

	private static final long serialVersionUID = -4962363984402951173L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		String result = invocation.invoke();
		String actionName = invocation.getInvocationContext().getName();
		if(actionName.equals("voteSubject_add")){
		LuceneUtil helloLucene =new LuceneUtil();
		helloLucene.index();
	}
		return result;
	}
}
