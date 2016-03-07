package duson.java.utils.mail;

public class SendMailThread extends Thread{

	//邮件内容
	private String mailContent;
	
	private String toAddress;
	
	private MailType mailType;
	
	private String mailTitle;
	
	private String mailServerHost = "mailServerHost";
	
	private String mailAccount    = "mailAccount";
	
	private String mailPassword   = "mailPassword";
	
	public SendMailThread(String mailTitle , String toAddress , String mailContent , MailType mailType){
		  this.toAddress = toAddress;
		  this.mailContent = mailContent;
		  this.mailType = mailType;
		  this.mailTitle = mailTitle;
	}
	
	public void run(){
		MailSenderInfo mailInfo = new MailSenderInfo();    
	      mailInfo.setMailServerHost(mailServerHost);    
	      mailInfo.setMailServerPort("25");    
	      mailInfo.setValidate(true);    
	      mailInfo.setUserName(mailAccount);    
	      mailInfo.setPassword(mailPassword);//您的邮箱密码    
	      mailInfo.setFromAddress(mailAccount);    
	      mailInfo.setToAddress(toAddress);    
	      mailInfo.setSubject(mailTitle);
	      
	      mailInfo.setContent(mailContent.toString());    
	         //这个类主要来发送邮件   
	      SimpleMailSender sms = new SimpleMailSender();   
	      if(mailType == MailType.TEXT){
	    	  sms.sendTextMail(mailInfo);//发送文体格式    
	      }else{
	    	  sms.sendHtmlMail(mailInfo);//发送html格式     
	      }
	}
	
}
