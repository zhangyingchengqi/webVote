package com.hcq.vote.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoteSubject implements Serializable{
	private static final long serialVersionUID = 4211428179919646284L;
	private static int TYPE_SINGLE =1;   //单选
	private static int TYPE_MULTIPLE=2;  //多选
	
	private long vsid;
	private String title;
	private int stype=1;
	private Integer usercount;
	private Integer optioncount;
	private List<VoteOption> options;
	private List<Long>chooseIds = new ArrayList<Long>();
	private List<String>voteoptions = new ArrayList<String>();
	
	

	public List<String> getVoteoptions() {
		return voteoptions;
	}

	public void setVoteoptions(List<String> voteoptions) {
		this.voteoptions = voteoptions;
	}

	public List<Long> getChooseIds() {
		return chooseIds;
	}
	
	public void setChooseIds(List<Long> chooseIds) {
		this.chooseIds = chooseIds;
	}
	
	public List<VoteOption> getOptions() {
		return options;
	}
	public void setOptions(List<VoteOption> options) {
		this.options = options;
	}
	public Integer getOptioncount() {
		return optioncount;
	}
	public void setOptioncount(Integer optioncount) {
		this.optioncount = optioncount;
	}
	public Integer getUsercount() {
		return usercount;
	}
	public void setUsercount(Integer usercount) {
		this.usercount = usercount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStype() {
		return stype;
	}
	public void setStype(int stype) {
		this.stype = stype;
	}
	public VoteSubject() {
	}
	public long getVsid() {
		return vsid;
	}
	public void setVsid(long vsid) {
		this.vsid = vsid;
	}
	
	
	public VoteSubject(String title, int stype) {
		super();
		this.title = title;
		this.stype = stype;
	}
	public VoteSubject(long vsid, String title, int stype, Integer usercount) {
		super();
		this.vsid = vsid;
		this.title = title;
		this.stype = stype;
		this.usercount = usercount;
	}
	@Override
	public String toString() {
		return "VoteSubject [vsid=" + vsid + ", title=" + title + ", stype=" + stype + ", usercount=" + usercount + "]";
	}
	
}
