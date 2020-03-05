package com.taotao.commom.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendJMail {
	
		public static boolean sendMail(String email, String emailMsg) {
		
		String from = "18716033528@163.com"; 				// �ʼ������˵��ʼ���ַ
		String to = email; 										// �ʼ������˵��ʼ���ַ
		final String username = "18716033528@163.com";  	
		final String password = "ww2154";   				

		Properties props = System.getProperties();

		props.setProperty("mail.smtp.host", "smtp.163.com");
	//	props.put("mail.smtp.auth", false);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		Session session = Session.getInstance(props);
		session.setDebug(true);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setSubject("缓存失败");
			message.setContent((emailMsg),"text/html;charset=utf-8");
			Transport transport=session.getTransport();
			transport.connect("smtp.163.com",25, username, password);
			transport.sendMessage(message,new Address[]{new InternetAddress(to)});
			transport.close();
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}	
}
