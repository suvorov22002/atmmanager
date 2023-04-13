package com.afb.portal.jpa.alerte;

import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
	    //System.setProperty("javax.net.ssl.trustStore","C:\\jboss-4.2.1\\server\\portal\\data\\mail.afrilandfirstbank.com.ks");
		//System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		
		//System.setProperty("javax.net.ssl.trustStore","");
		//System.setProperty("javax.net.ssl.trustStorePassword", "");
				
		// TODO Auto-generated method stub
		String from = "francis_konchou@afrilandfirstbank.com";
		List<String> mailsTo = new ArrayList<String>();
		mailsTo.add("francis_konchou@afrilandfirstbank.com");
		mailsTo.add("kfarmel24@gmail.com");
		mailsTo.add("kfarmel@yahoo.fr");
		String subject = "Test";
		String messageCorps = "Mail is OK";
				
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		System.setProperty("javax.net.ssl.trustStore","C:\\jboss-4.2.1\\server\\portal\\data\\keystore.ks");
		System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
		
		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		System.setProperty("javax.net.ssl.trustStore","C:\\jboss-4.2.1\\server\\portal\\data\\keystore.ks");
		System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
		
		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		// TODO Auto-generated method stub
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);

		
	}

}
