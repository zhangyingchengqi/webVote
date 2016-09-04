package com.hcq.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.hcq.vote.entity.VoteItem;
import com.hcq.vote.entity.VoteOption;
import com.hcq.vote.entity.VoteSubject;
import com.hcq.vote.entity.VoteUser;

public interface VoteService {
	
	
	
	@SuppressWarnings("rawtypes")
	public List getUserCountPerSubject(Long id) throws Exception;
	
	/**获取所有的投票主题
	 * @return
	 * @throws Exception 
	 */
	public List<VoteSubject>getAllSubjects() throws Exception;
	
	/**根据主题的标题查找主题  1，到数据库去查   2 使用lucene
	 * @param title
	 * @return
	 * @throws Exception 
	 */
	public List<VoteSubject>getSubjectsByTitle(String title) throws Exception;
	
	/**
	 * 
	 *根据主题的编号查主题的内容 
	 * @param id
	 * @param lock:     select *from xxx  where id =xxx for update
	 * @return
	 * @throws Exception 
	 */
	public VoteSubject findSubjectById(long id,boolean lock) throws Exception;
	
	/**根据id查选项
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public VoteOption findOptionById(long id) throws Exception;
	
	/**更新或是保存主题
	 * @param subject     subject 中有id   则是更新操作   没有id，则是添加操作
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void saveOrUpdate(VoteSubject subject) throws SQLException, IOException;
	
	/**根据id
	 * 删除主题（先删除投票，再删除选项，最后才能删除主题
	 * @param id
	 * @throws SQLException 
	 */
	public void deleteSubject(long id) throws SQLException;
	
	/**根据id查找用户
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public VoteUser findUserById(String uname) throws Exception;
	
	/**保存或更新投票项
	 * @param item
	 * @throws SQLException 
	 */
	public void saveOrUpdate(VoteItem item) throws SQLException;
	
	/**某个主题的每个投票数
	 * @param entityId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public List statVoteCountPerOptionOfSubject(long entityId) throws Exception;
	
	/**保存或是更新用户
	 * @param user
	 * @throws SQLException 
	 */
	public void saveOrUpdate(VoteUser user) throws SQLException;

	public List<VoteOption> findAllOption(Long vsid) throws Exception;

	public boolean isUserVote(Integer uid, long vsid) throws SQLException, IOException;

	public void saveVoteItem(long vsid, List<Long> chooseIds, Integer uid) throws SQLException;
}
