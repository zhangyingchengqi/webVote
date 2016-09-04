package com.hcq.web.test;

import java.sql.SQLException;
import java.util.List;
import com.hcq.service.VoteService;
import com.hcq.service.impl.VoteServiceImpl;
import com.hcq.vote.entity.VoteItem;
import com.hcq.vote.entity.VoteOption;
import com.hcq.vote.entity.VoteSubject;
import com.hcq.vote.entity.VoteUser;

import junit.framework.TestCase;

public class TestVoteService extends TestCase {
	
	public void testGetUserCountPerSubject() throws Exception{
		VoteService vService = new VoteServiceImpl();
		List list = vService.getUserCountPerSubject(null);
		System.out.println(list);
	}
	
	
	public void testgetSubjectsByTitle() throws Exception{
		VoteService vService = new VoteServiceImpl();
		List list = vService.getSubjectsByTitle("选出你心目中最好的网络聊天工具");
		System.out.println(list);
	}
	
	public void testfindSubjectById() throws Exception{
		VoteService vService = new VoteServiceImpl();
		VoteSubject list = vService.findSubjectById(3, false);
		System.out.println(list);
	}
	
	public void testfindOptionById() throws Exception{
		VoteService vService = new VoteServiceImpl();
		VoteOption list = vService.findOptionById(1);
		System.out.println(list);
	}
	
	
	public void testsaveOrUpdate() throws Exception{
		VoteService vService = new VoteServiceImpl();
		vService.saveOrUpdate(new VoteSubject("我的故乡在哪？",1));
	}
	
	public void testdeleteSubject() throws Exception{
		VoteService vService = new VoteServiceImpl();
		vService.deleteSubject(8);
	}
	
	public void testfindUserById() throws Exception{
		VoteService vService = new VoteServiceImpl();
		VoteUser user = vService.findUserById("1");
		System.out.println(user);
	}
	
	public void testsaveOrUpdate2 () throws SQLException{
		VoteService vService = new VoteServiceImpl();
		vService.saveOrUpdate(new VoteItem(1,1,1));
	}
	
	public void teststatVoteCountPerOptionOfSubject () throws Exception {
		VoteService vService = new VoteServiceImpl();
		List list =vService.statVoteCountPerOptionOfSubject(3l);
		System.out.println(list);
	}
	
	public void testsaveOrUpdate3 () throws Exception {
		VoteService vService = new VoteServiceImpl();
		
		vService.saveOrUpdate(new VoteUser("hcq","a"));
	}
	
	public void testfindAllOption () throws Exception {
		VoteService vService = new VoteServiceImpl();
		
		List<VoteOption>optionlist = vService.findAllOption(1l);
		System.out.println(optionlist);
	}
}
