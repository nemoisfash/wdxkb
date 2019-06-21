package cn.hxz.webapp.util;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * MailUtils.java Create on 2017年3月23日 下午5:32:45
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class MailSender {
	
	private Properties props = new Properties();
	private String username;
	private String password;
	private Session session;

    public MailSender(String host, String username, String password){ 
    	this.props.put("mail.smtp.auth", "true");
        this.props.put("mail.smtp.host", host);
        this.username = username;
        this.password = password;
    }
    
    private Session getSession(){
    	if (session == null){
	    	session = Session.getInstance(props, new Authenticator(){
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });
	    	session.setDebug(true);
    	}
    	return session;
    }
    
    public boolean valid(){
    	if (null==getSession())
    		return false;
    	return true;
    }
    
    public void send(String subject, String content, List<String> addresses){
    	
        List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (String address : addresses){
        	InternetAddress to;
			try {
				to = new InternetAddress(address);
			} catch (AddressException e) {
				continue;
			}
        	recipients.add(to);
        }
    	
        try {
            Message message = new MimeMessage(getSession());
            InternetAddress from = new InternetAddress(username,"中阿技术转移中心");
            
            message.setSubject(subject);
            message.setFrom(from);
            message.setSentDate(new Date());
            InternetAddress[] to = recipients.toArray(new InternetAddress[recipients.size()]);
			message.setRecipients(Message.RecipientType.BCC, to);

//	        // --- 发送文本邮件
//	        message.setText(content);
	        
	        // --- 发送HTML邮件
	        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
	        Multipart multipart = new MimeMultipart();
	        // 创建一个包含HTML内容的MimeBodyPart
	        BodyPart bodyPart = new MimeBodyPart();
	        // 设置HTML内容
	        bodyPart.setContent(content, "text/html; charset=utf-8");
	        multipart.addBodyPart(bodyPart);
	        // 将MiniMultipart对象设置为邮件内容
	        message.setContent(multipart);

	        Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public static void main(String[] args) {
		
		List<String> addresses = new ArrayList<String>();
//		addresses.add("张三<zhangsan@mail.net>");
//		addresses.add("李四<lisi#mail.net");
//		addresses.add("王五<wangwu@mail.net>");
		addresses.add("测试1<ckepub@qq.com>");
		
        List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (String address : addresses){
        	InternetAddress to;
			try {
				to = new InternetAddress(address);
			} catch (AddressException e) {
				System.out.println("---------------- " + address + " 格式错误");
				continue;
			}
        	recipients.add(to);
        }
		
        for (InternetAddress recipient : recipients){
			System.out.println(recipient.getAddress());
			System.out.println(recipient.getPersonal());
			System.out.println(recipient.getType());
        }
        
        MailSender sender = new MailSender("smtp.163.com", "nxcke@163.com", "fT32Gb5FrlvQWSxx");
        sender.send("测试javax.mail发送邮件", "<p style=\"color:blue\">邮件测试</p><p style=\"color:green\">email test</p>", addresses);
	}
}
