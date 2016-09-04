package com.hcq.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import com.hcq.utils.LuceneUtil;
import com.hcq.vote.entity.JsonModel;
import com.hcq.vote.entity.SearchBean;
import com.hcq.vote.entity.VoteSubject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")
@ParentPackage("mypackge")
public class SearchAction extends ActionSupport implements ModelDriven<SearchBean>{
	private static final long serialVersionUID = 4250254057478257322L;
	private SearchBean searchBean;
	private JsonModel jsonModel;
	LuceneUtil helloLucene;
	
	@Action(value="/search_subject",results=@Result(type="json",name="success",params={"root","jsonModel","excludeNullProperties","true","noCache","true"}))
	public String findAll(){
		jsonModel = new JsonModel();
		helloLucene=new LuceneUtil();
		try{
			//传入关键字，返回靠前的十个结果
			List<VoteSubject>list=helloLucene.search(searchBean.getKeyword(), 10);
			jsonModel.setCode(1);
			jsonModel.setObj(list);
		}catch(Exception e){
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return ActionSupport.SUCCESS;
	}
	
	public JsonModel getJsonModel() {
		return jsonModel;
	}
	public void setJsonModel(JsonModel jsonModel) {
		this.jsonModel = jsonModel;
	}

	public SearchBean getModel() {
		searchBean = new SearchBean();
		return searchBean;
	}
	
}
