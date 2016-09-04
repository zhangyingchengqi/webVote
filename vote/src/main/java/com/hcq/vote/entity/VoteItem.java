package com.hcq.vote.entity;

import java.io.Serializable;

public class VoteItem implements Serializable{
	private static final long serialVersionUID = -6477994104560392173L;
	private Integer viid ;   //编号
	private Integer vsid;   //主题
	private Integer voteid;    //选项
	private Integer uid;      //用户
	private Integer votecount;
	private String voteoption;
	
	
	public String getVoteoption() {
		return voteoption;
	}
	public void setVoteoption(String voteoption) {
		this.voteoption = voteoption;
	}
	public Integer getVotecount() {
		return votecount;
	}
	public void setVotecount(Integer votecount) {
		this.votecount = votecount;
	}
	public Integer getViid() {
		return viid;
	}
	public void setViid(Integer viid) {
		this.viid = viid;
	}
	public Integer getVsid() {
		return vsid;
	}
	public void setVsid(Integer vsid) {
		this.vsid = vsid;
	}
	public Integer getVoteid() {
		return voteid;
	}
	public void setVoteid(Integer voteid) {
		this.voteid = voteid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "VoteItem [viid=" + viid + ", vsid=" + vsid + ", voteid=" + voteid + ", uid=" + uid + "]";
	}
	public VoteItem(Integer viid, Integer vsid, Integer voteid, Integer uid) {
		super();
		this.viid = viid;
		this.vsid = vsid;
		this.voteid = voteid;
		this.uid = uid;
	}
	
	
	public VoteItem(Integer vsid, Integer voteid, Integer uid) {
		super();
		this.vsid = vsid;
		this.voteid = voteid;
		this.uid = uid;
	}
	
	public VoteItem() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((viid == null) ? 0 : viid.hashCode());
		result = prime * result + ((voteid == null) ? 0 : voteid.hashCode());
		result = prime * result + ((vsid == null) ? 0 : vsid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteItem other = (VoteItem) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (viid == null) {
			if (other.viid != null)
				return false;
		} else if (!viid.equals(other.viid))
			return false;
		if (voteid == null) {
			if (other.voteid != null)
				return false;
		} else if (!voteid.equals(other.voteid))
			return false;
		if (vsid == null) {
			if (other.vsid != null)
				return false;
		} else if (!vsid.equals(other.vsid))
			return false;
		return true;
	}
}
