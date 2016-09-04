package com.hcq.web.interceptors;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.google.gson.Gson;
import com.hcq.vote.entity.JsonModel;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LogInterceptor extends MethodFilterInterceptor {
	private static final long serialVersionUID = -5730733842831342200L;

	@Override
	protected String doIntercept(ActionInvocation  invocation) throws Exception {
		
         //取session中的username，如果等于null则没有登陆，返回input跳转到登陆页面  
         if(ServletActionContext.getRequest().getSession().getAttribute("loginUser")==null){  
             //return "error";跳转页面方案  
        	 HttpServletResponse response = ServletActionContext.getResponse();
        	 JsonModel jsonModel = new JsonModel();
        	 jsonModel.setCode(0);
        	 jsonModel.setMsg(" you have not been logined");
        	 
        	 response.setContentType("text/plain;charset=utf-8");
        	 
        	 PrintWriter out =response.getWriter();
        	 
        	 Gson gson = new Gson();
        	 String json =gson.toJson(jsonModel);
        	 out.println(json);
        	 out.println();
        	 out.close();
         }  
         return invocation.invoke();  
	}
}
