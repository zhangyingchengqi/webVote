package com.hcq.utils;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * @author HCQ
 *
 */
public class Mail {
	private MimeMessage mimeMessage;//发送邮件需要配置邮件服务器属性信息邮件对象
	private Session session;   //邮件会话对象
	private Properties props;    //读取项目的配置文件  系统属性
	private String username;   //发件人真实姓名
	private String password;    //密码
	private Multipart mp;   //邮件正文部分
	
	private Mail(String smtp){
		setSmtpHost(smtp);  //系统属性
		createMimeMessage();    //会话邮件对象
		
	}

	
	/**
	 * @param smtp   将smtp服务器的主机名加入到系统配置信息中
	 */
	private void setSmtpHost(String hostName) {
		if(props==null){
			props=System.getProperties();   //存放邮件服务器的信息
		}
		props.put("mail.smtp.host", hostName);//设置smtp主机
	}
	
	/**
	 * @return    获取会话对象，创建邮件对象是否成功
	 */
	private boolean createMimeMessage() {
		try{
			session=Session.getDefaultInstance(props,null);
		}catch(Exception e){
			return false;
		}
		try{
			mimeMessage=new MimeMessage(session);//创建mime邮件对象
			mp= new MimeMultipart();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * @param need
	 * 定义smtp是否需要验证
	 */
	private void setNeedAuth(boolean need){
		if(props==null){
			props=System.getProperties();
		}if(need){
			props.put("mail.smtp.auth", "true");
		}else{
			props.put("mail.smtp.auth","false");
		}
	}
	
	//发送用户设置
	private void setNamePass(String username,String password){
		this.username=username;
		this.password=password;
	}
	
	//定义邮件主题
	private boolean setSubject(String subject){
		try{
			mimeMessage.setSubject(subject);
			return true;
		}catch(Exception E){
			System.out.println("定义邮件主题发生错误");
			return false;
		}
	}
	
	//定义邮件正文
	private boolean setBody(String content){
		try{
			BodyPart bp=new MimeBodyPart();
			bp.setContent(""+content,"text/html;charset=GBK");
			mp.addBodyPart(bp);
			return true;
		}catch(Exception e){
			System.out.println("定义邮件正文时发生错误");
			return false;
		}
	}
	
	//设置发信人
	private boolean setFrom(String from){
		try{
			mimeMessage.setFrom(new InternetAddress(from));  //发信人
			return true;
		}catch(Exception e){
			return false;
		}
	}
	//定义收信人
	private boolean setTo(String to){
		if(to==null){
			return false;
		}try{
			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));;
			return true;
		}catch(Exception E){
			return false;
		}
	}
	
	//发送邮件模块
	private boolean sendOut(){
		try{
			mimeMessage.setContent(mp);
			mimeMessage.saveChanges();
			Session mailSession=Session.getInstance(props, null);
			Transport transport=mailSession.getTransport("smtp");
			transport.connect((String)props.get("mail.smtp.host"), username,password);
			transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		}catch(Exception e){
			System.out.println("发送失败咯！");
			return false;
		}
	}
	
	//使用sendout发送
	
	public static boolean sendAndCc(String smtp,String from,String to,String copyto,
			String subject,String content,String username,String password){
		Mail theMail=new Mail(smtp);
		theMail.setNeedAuth(true);
		if(!theMail.setSubject(subject)){
			return false;
		}
		if(!theMail.setBody(content)){
			return false;
		}
		if(!theMail.setTo(to)){
			return false;
		}
		if(!theMail.setFrom(from)){
			return false;
		}
		theMail.setNamePass(username, password);
		if(!theMail.sendOut()){
			return false;
		}
		return true;
	}
}