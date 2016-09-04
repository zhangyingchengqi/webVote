package com.hcq.web.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.hcq.service.VoteService;
import com.hcq.service.impl.VoteServiceImpl;
import com.hcq.vote.entity.JsonModel;
import com.hcq.vote.entity.VoteUser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")
@ParentPackage("mypackge")
@InterceptorRefs({@InterceptorRef("defaultStack"),@InterceptorRef("time")
})
public class VoteUserAction extends ActionSupport implements ModelDriven<VoteUser> {
	private static final long serialVersionUID = -8273104747002544086L;
	private VoteService service = new VoteServiceImpl();
	private VoteUser user;
	private JsonModel jsonModel;
	
	public VoteUserAction() {
	}
	
	
	@Action(value="/voteUser_register",results=@Result(type="json",name="reg",params={"root","jsonModel","excludeNullProperties","true","noCache","true"}))
	public String register(){
		jsonModel = new JsonModel();
		if(user.getUname()!=null&&user.getPwd()!=null){
		try{
			service.saveOrUpdate(user);
			jsonModel.setCode(1);
		}catch(Exception e){
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg("user dose not exit");
			}
		}else{
			jsonModel.setCode(0);
			jsonModel.setMsg("user dose not exit");
		}
		return "reg";
	}
	
	@Action(value="/voteUser_login",results=@Result(type="json",name="loginSuccess",params={"root","jsonModel","excludeNullProperties","true","noCache","true"}))
	public String login(){
		jsonModel = new JsonModel();
		try{
			VoteUser voteuser = service.findUserById(user.getUname());
			if(voteuser == null){
				jsonModel.setCode(0);
				jsonModel.setMsg("user does not exist !");
			}else{
				if(voteuser.getPwd().equals(user.getPwd())){
					ActionContext.getContext().getSession().put("loginUser", voteuser);
					jsonModel.setCode(1);
				}else{
					jsonModel.setCode(0);
					jsonModel.setMsg(" user does not exist");
				}
			}
		}catch( Exception e){
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return "loginSuccess";
	}
	
	public JsonModel getJsonModel() {
		return jsonModel;
	}
	public void setJsonModel(JsonModel jsonModel) {
		this.jsonModel = jsonModel;
	}
	public VoteUser getModel() {
		user =new VoteUser();
		return user;
	}

	public VoteUser getUser() {
		return user;
	}

	public void setUser(VoteUser user) {
		this.user = user;
	}
	
	
}
