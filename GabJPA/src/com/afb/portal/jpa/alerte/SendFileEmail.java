package com.afb.portal.jpa.alerte;

//File Name SendFileEmail.java

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * SendFileEmail
 * @author Owner
 *
 */
public class SendFileEmail{


	public static String SendMailProvider(String from , List<String> mailsTo, List<String> mailsToCopy, String subject, String messageCorps ){    
		return SendMail("", "", from, mailsTo, mailsToCopy, subject, messageCorps);
	}

	/**
	 * SendMail
	 * @param sourcefile
	 * @param sourcefichier
	 * @param from
	 * @param mailsTo
	 * @param subject
	 * @param messageCorps
	 */
	public static void SendMail(String sourcefichier,String from , List<String> mailsTo, List<String> mailsToCopy, String subject, String messageCorps){

		// Recipient's email ID needs to be mentioned.
		// Sender's email ID needs to be mentioned
		// String from = "francis_konchou@afrilandfirstbank.com";

		if(sourcefichier == null || sourcefichier.trim().isEmpty()){
			SendMailProvider(from, mailsTo, mailsToCopy, subject, messageCorps);
			return;
		}

		//System.out.println("----SendMail----"+sourcefichier);

		// Assuming you are sending email from localhost
		String host = "172.21.10.91";

		// Get system properties
		//Properties properties = System.getProperties();
		Properties properties = new Properties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(false);

		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			BodyPart messageBodyPart = new MimeBodyPart();

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));	

			// Set To: header field of the header.
			if(mailsTo.isEmpty() && mailsToCopy.isEmpty()) return ;
			if(mailsTo.isEmpty() && !mailsToCopy.isEmpty()){
				for(String mal: mailsToCopy){
					if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}
			}else{
				for(String mal: mailsTo){
					if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}

				if(mailsToCopy != null ){
					for(String mal: mailsToCopy){
						if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.CC,new InternetAddress(mal.trim()));
					}
				}
			}

			// Set Subject: header field
			message.setSubject(subject);

			// Fill the message
			messageBodyPart.setText(messageCorps);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			File fichier = new File(sourcefichier);
			DataSource source = new FileDataSource(fichier);			
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fichier.getName());
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart );

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");

		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}


	public static void SendMail2(String fileName,String sourcefichier,String from , List<String> mailsTo, List<String> mailsToCopy, String subject, String messageCorps){

		// Assuming you are sending email from localhost
		String host = "172.21.10.91";
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(false);

		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));	

			// Set To: header field of the header.
			if(mailsTo.isEmpty() && mailsToCopy.isEmpty()) return ;
			if(mailsTo.isEmpty() && !mailsToCopy.isEmpty()){
				for(String mal: mailsToCopy){
					if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}
			}else{
				for(String mal: mailsTo){
					if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}

				if(mailsToCopy != null ){
					for(String mal: mailsToCopy){
						if(mal != null && !mal.trim().isEmpty())message.addRecipient(Message.RecipientType.CC,new InternetAddress(mal.trim()));
					}
				}

			}

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(messageCorps);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();		
			DataSource source = new FileDataSource(new File(sourcefichier));			
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			// Send the complete message parts
			message.setContent(multipart );

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");

		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	
	public static String SendMail(String fileName,String sourcefichier,String from , List<String> mailsTo, List<String> mailsToCopy, String subject, String messageCorps){

		try{
			
			// Step1  mail.afrilandfirstbank.com   
			//System.out.println("\n 1st ===> setup Mail Server Properties.."); "172.21.10.91" "mail.afrilandfirstbank.com"
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.host","172.21.10.91");
			mailServerProperties.put("mail.smtp.port","25");
			mailServerProperties.put("mail.smtp.auth","true");
			mailServerProperties.put("mail.smtp.starttls.enable","true");
			mailServerProperties.put("mail.smtp.starttls.required","true");
			//mailServerProperties.put("mail.smtp.ssl.enabled", "true");
			//System.out.println("Mail Server Properties have been setup successfully..");
			
			/**String javaHomePath = System.getProperty("java.home");
			String keystore = javaHomePath + "\\lib\\security\\cacerts";
			String storepass= "changeit";
			String storetype= "JKS";

			String[][] props = {
			    { "javax.net.ssl.trustStore", keystore, },
			    { "javax.net.ssl.keyStore", keystore, },
			    { "javax.net.ssl.keyStorePassword", storepass, },
			    { "javax.net.ssl.trustStorePassword", storepass, },
			    { "javax.net.ssl.keyStoreType", storetype, },
			};
			for (int i = 0; i < props.length; i++) {
				mailServerProperties.put(props[i][0], props[i][1]);
			}*/
			

			// Step2
			//System.out.println("\n\n 2nd ===> get Mail Session..");
			getMailSession = Session.getDefaultInstance(mailServerProperties, new Authenticator(){});
			getMailSession.setDebug(false);
			generateMailMessage = new MimeMessage(getMailSession);
			
			//String from = "alertetpe@afrilandfirstbank.com";
			
			generateMailMessage.setFrom(new InternetAddress(from));
			
			// Cc
			// generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("francis_konchou@afrilandfirstbank.com"));
			
			if(mailsTo.isEmpty() && mailsToCopy.isEmpty()) return "000";
			if(mailsTo.isEmpty() && !mailsToCopy.isEmpty()){
				for(String mal: mailsToCopy){
					if(mal != null && !mal.trim().isEmpty())generateMailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}
			}else{
				for(String mal: mailsTo){
					if(mal != null && !mal.trim().isEmpty())generateMailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(mal.trim()));
				}

				if(mailsToCopy != null ){
					for(String mal: mailsToCopy){
						if(mal != null && !mal.trim().isEmpty())generateMailMessage.addRecipient(Message.RecipientType.CC,new InternetAddress(mal.trim()));
					}
				}

			}
		
			
			//String Subject = "MAIL TEST ";
			generateMailMessage.setSubject(subject);
			//String emailBody = "Ne pas Repondre";

			///////////////////////////////////////
			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(messageCorps);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if(!fileName.trim().isEmpty() && !sourcefichier.trim().isEmpty() ){
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(sourcefichier);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
			}
			
			//generateMailMessage.setContent(emailBody, "text/html");
			generateMailMessage.setContent(multipart);
			//System.out.println("Mail Session has been created successfully..");

			// Step3
			//System.out.println("\n\n 3rd ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");

			// Enter your correct gmail UserID and Password https://mail.afrilandfirstbank.com
			// if you have 2FA enabled then provide App Specific Password mail.afrilandfirstbank.com
			//transport.connect("mail.google.com", "xxxxxxxxxx@gmail.com", "xxxxx");
			//transport.connect("mail.afrilandfirstbank.com","alertetpe@afrilandfirstbank.com", "ChangeMe");
			transport.connect("172.21.10.91", "alertetpe@afrilandfirstbank.com", "ChangeMe");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			
		}catch(MessagingException mex){
			mex.printStackTrace();
			return mex.toString();
		}
		return "200";
		
	}
	
	
}