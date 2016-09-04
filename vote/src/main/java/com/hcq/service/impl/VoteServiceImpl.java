package com.hcq.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hcq.service.VoteService;
import com.hcq.utils.DBUtil;
import com.hcq.utils.LogUtil;
import com.hcq.vote.entity.VoteItem;
import com.hcq.vote.entity.VoteOption;
import com.hcq.vote.entity.VoteSubject;
import com.hcq.vote.entity.VoteUser;


public class VoteServiceImpl implements VoteService {
	private DBUtil db = new DBUtil();
	


	@SuppressWarnings({ "static-access", "rawtypes" })
	public List getUserCountPerSubject(Long id) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<Object>params = new ArrayList<Object>();
		sb.append(" select a.vsid,a.title,a.stype,a.usercount,ifnull(b.optioncount,0) as optioncount from ( ");
		sb.append(" select votesubject.vsid,votesubject.title,votesubject.stype,count(distinct(uid)) as usercount ");
		sb.append(" from voteitem ");
		sb.append(" right join votesubject ");
		sb.append(" on voteitem.vsid=votesubject.vsid ");
		sb.append(" group by voteitem.vsid,votesubject.title");
		sb.append(") a ");
		sb.append(" left join ( ");
		sb.append(" select votesubject.vsid,count(*) as optioncount ");
		sb.append(" from votesubject ");
		sb.append(" inner join voteoption ");
		sb.append(" on voteoption.vsid=votesubject.vsid ");
		sb.append(" group by votesubject.vsid ");
		sb.append(" ) b ");
		sb.append(" on a.vsid = b.vsid ");
		if(id!=null && id>0){
			sb.append(" where a.vsid=? ");
			params.add(id);
		}
		List<VoteSubject>list = null;
		try{
			list = db.find(sb.toString(), params, VoteSubject.class);
		}catch(Exception e){
			throw e;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<VoteSubject> getAllSubjects() throws Exception {
		return this.getUserCountPerSubject(null);
	}

	@SuppressWarnings("static-access")
	public List<VoteSubject> getSubjectsByTitle(String title) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<Object>params = new ArrayList<Object>();
		sb.append(" select a.vsid,a.title,a.stype,a.usercount, ifnull(b.optioncount,0) as votecount from( ");
		sb.append(" select votesubject.vsid,votesubject.title,votesubject.stype,count(distinct(uid)) as usercount ");
		sb.append(" from voteitem ");
		sb.append(" right join votesubject ");
		sb.append(" on voteitem.vsid=votesubject.vsid ");
		sb.append(" group by voteitem.vsid,votesubject.title ");
		sb.append(") a ");
		sb.append(" left join ( ");
		sb.append(" select votesubject.vsid,count(*) as optioncount ");
		sb.append(" from votesubject ");
		sb.append(" inner join voteoption ");
		sb.append(" on voteoption.vsid = votesubject.vsid ");
		sb.append(" group by votesubject.vsid ");
		sb.append(" )b ");
		sb.append("on a.vsid =b.vsid ");
		
		//条件不同
		if( title !=null && !"".equals(title)){
			sb.append(" where a.title like ?");
			params.add("%"+title+"%");
		}
		
		List<VoteSubject>list =null;
		try{
			list = db.find(sb.toString(), params, VoteSubject.class);
		}catch(Exception e){
			throw e;
		}
		LogUtil.logger.debug(sb.toString());
		System.out.println(sb.toString());
		return list;
	}

	@SuppressWarnings("static-access")
	public VoteSubject findSubjectById(long id, boolean lock) throws Exception {
		String sql = "select*from votesubject where vsid +? ";
		List<Object>params =new ArrayList<Object>();
		params.add(id);
		List<VoteSubject>list = db.find(sql, params, VoteSubject.class);
		return list!=null&&list.size()>0?list.get(0):null;
	}

	@SuppressWarnings("static-access")
	public VoteOption findOptionById(long id) throws Exception {
		String sql = "select * from voteoption where voteid =? ";
		List <Object>params = new ArrayList<Object>();
		params.add(id);
		List<VoteOption>list = db.find(sql, params, VoteOption.class);
		return list!=null&&list.size()>0?list.get(0):null;
	}

	@SuppressWarnings("static-access")
	public void saveOrUpdate(VoteSubject subject) throws SQLException, IOException {
		List<Object>params = new ArrayList<Object>();
		String sql = "";
		if(subject !=null &&subject.getVsid()>0){
			params.add(subject.getTitle());
			params.add(subject.getStype());
			params.add(subject.getVsid());
			sql ="update votesubject set title=?,stype=? where vsid=? ";
			db.doUpdate(sql, params);
		}else{
			//先查主题编号
			sql ="select max(vsid)as maxvsid from votesubject";
			Map<String, Object>map=db.doQueryOne(sql,null);
			Integer vsid =1;
			if(map.get("maxvsid")!=null){
				vsid=(Integer)map.get("maxvsid")+1;
			}
			List<String>sqls=new ArrayList<String>();
			List<List<Object>>paramlist=new ArrayList<List<Object>>();
			sql ="insert into votesubject(title,stype) values(?,?) ";
			sqls.add(sql);
			params.add(subject.getTitle());
			params.add(subject.getStype());
			paramlist.add(params);
			int order=0;
			for(String option:subject.getVoteoptions()){
				order++;
				sql="insert into voteoption(voteoption,voteorder,vsid)value(?,?,?)";
				sqls.add(sql);
				
				List<Object>p = new ArrayList<Object>();
				p.add(option);
				p.add(order);
				p.add(vsid);
				
				paramlist.add(p);
			}
			db.doUpdate(sqls, paramlist);
		}
	}

	@SuppressWarnings("static-access")
	public void deleteSubject(long id) throws SQLException {
		List<String>sqls =new ArrayList<String>();
		List<List<Object>>params = new ArrayList<List<Object>>();
		sqls.add(" delete from voteitem where vsid =? ");
		List<Object> p =new ArrayList<Object>() ;
		p.add(id);
		params.add(p);
		
		sqls.add(" delete from voteoption where vsid =?");
		p = new ArrayList<Object>();
		p.add(id);
		params.add(p);
		
		sqls.add(" delete from votesubject where vsid=? ");
		p = new ArrayList<Object>();
		p.add(id);
		params.add(p);
		
		db.doUpdate(sqls, params);
	}

	@SuppressWarnings({ "static-access" })
	public VoteUser findUserById(String uname) throws Exception {
		String sql = "select *from voteuser where uname= ? ";
		List<Object>params = new ArrayList<Object>();
		params.add(uname);
		List<VoteUser>list = db.find(sql, params, VoteUser.class);
		
		return list!=null && list.size()>0? list.get(0):null;
	}

	@SuppressWarnings("static-access")
	public void saveOrUpdate(VoteItem item) throws SQLException {
		List<Object>params = new ArrayList<Object>();
		String sql = "";
		if(item !=null && item.getViid()!=null){
			params.add(item.getVoteid());
			params.add(item.getVsid());
			params.add(item.getUid());
			params.add(item.getViid());
			sql ="update voteitem set voteid=?,vsid=?,uid=? where uid=?";
		}else{
			sql ="insert into voteitem(voteid,vsid,uid) values(?,?,?) ";
			params.add(item.getVoteid());
			params.add(item.getVsid());
			params.add(item.getUid());
		}
		db.doUpdate(sql, params);
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	public List statVoteCountPerOptionOfSubject(long entityId) throws Exception {
		StringBuffer sql = new StringBuffer("select a.voteid,a.voteoption,ifnull(b.votecount,0) as votecount from ");
		sql.append(" (select vsid,voteid,voteoption from voteoption where vsid="+entityId+")a ");
		sql.append(" left join ");
		sql.append(" (select vsid,voteid,count(voteid) as votecount from voteitem ");
		sql.append(" where vsid="+entityId);
		sql.append(" group by voteid ");
		sql.append(") b ");
		sql.append(" on a.voteid=b.voteid ");
		
		return db.find(sql.toString(), null, VoteItem.class);
	}

	@SuppressWarnings("static-access")
	public void saveOrUpdate(VoteUser user) throws SQLException {
		List<Object>params = new ArrayList<Object>();
		String sql ="";
		
		if(user!=null && user.getUid()!=null){
			params.add(user.getUname());
			params.add(user.getPwd());
			params.add(user.getUid());
			sql ="update voteuser set uname=?,pwd=? where uid=? ";
		}else{
			sql ="insert into voteuser(uname,pwd) values(?,?) ";
			params.add(user.getUname());
			params.add(user.getPwd());
		}
		db.doUpdate(sql, params);
	}

	@SuppressWarnings("static-access")
	public List<VoteOption> findAllOption(Long vsid) throws Exception {
		String sql ="select *from voteoption where vsid="+vsid;
		LogUtil.logger.debug(sql);
		List<VoteOption>optionlist = db.find(sql,null, VoteOption.class);
		return optionlist;
	}

	public boolean isUserVote(Integer uid, long vsid) throws SQLException, IOException {
		String sql ="select count(*) as num from voteitem where vsid="+vsid+" and uid="+uid;
		Map<String, Object>map = db.doQueryOne(sql, null);
		if(map!=null&&Long.parseLong(map.get("num").toString())>=1){
			return true;
		}else{
		return false;
	}
  }

	public void saveVoteItem(long vsid, List<Long> votelist, Integer uid) throws SQLException {
		List<String>sqls = new ArrayList<String>();
		List<List<Object>>params = new ArrayList<List<Object>>();
		
		for(Long voteid:votelist){
			String sql="insert into voteitem(voteid,vsid,uid)values(?,?,?)";
			List<Object>p = new ArrayList<Object>();
			p.add(voteid);
			p.add(vsid);
			p.add(uid);
			
			sqls.add(sql);
			params.add(p);
		}
		db.doUpdate(sqls, params);
	}
}
