package com.afb.portal.buisness.monitoring.worker;


import java.io.File;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class CSftp
{

	private String strHost;
	private String strUser;
	private String strPasswd;
	private Session session;
	private String strPortSSHZone;
	private ChannelSftp cannal;
	private static boolean annuler;


	public CSftp(String strLeHost, String strLeUser, String strLePasswd){
		this.strHost = strLeHost;
		this.strUser = strLeUser;
		this.strPasswd = strLePasswd;
		annuler = false;
	}

	/**
	 * 
	 * @param lfile
	 * @param user
	 * @param host
	 * @param rfile
	 * @param password
	 * @throws Exception
	 */
	public static Boolean send(String lfile, String user, String host, String rfile, String password)  throws Exception {

		try{

			JSch jsch = new JSch();
			Session  session = jsch.getSession(user,host,22);
			UserInfo ui = new MyUserInfo(password);
			session.setUserInfo(ui);
			session.connect();


			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp cannal = ((ChannelSftp)channel);

			cannal.put(lfile, rfile, null, 0);
			File remoteFileName = new File(lfile);
			cannal.mkdir(rfile);
			cannal.cd(rfile);
			cannal.rm(remoteFileName.getName());

			channel.disconnect();
			session.disconnect();

		}catch (Exception e){
			e.printStackTrace();
			return Boolean.FALSE;
		}

		return Boolean.TRUE;

	}

	public boolean openSFTP(){

		try{
			JSch jsch = new JSch();
			this.session = jsch.getSession(this.strUser, this.strHost, Integer.parseInt(strPortSSHZone));
			UserInfo ui = new MyUserInfo(this.strPasswd);
			this.session.setUserInfo(ui);
			this.session.connect();
			Channel channel = this.session.openChannel("sftp");
			channel.connect();
			this.cannal = ((ChannelSftp)channel);
			return true;
		}catch (Exception e){
			e.printStackTrace(System.out);
		}

		return false;

	}

	public boolean closeSFTP(){

		try{

			this.session.disconnect();
			return true;
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

	public boolean envoieSFTP(String strLocalPath, String strRemotePath)
	{
		try
		{
			annuler = false;
			this.cannal.put(strLocalPath, strRemotePath, null, 0);
			if (annuler)
			{
				File remoteFileName = new File(strLocalPath);
				this.cannal.cd(strRemotePath);
				this.cannal.rm(remoteFileName.getName());
				return false;
			}
			return true;
		}
		catch (SftpException e)
		{
			return false;
		}
		catch (Exception exc)
		{
		}
		return false;
	}

	public static class MyUserInfo implements UserInfo
	{
		String strPasswd;

		public MyUserInfo(String strLePasswd)
		{
			this.strPasswd = strLePasswd;
		}

		public String getPassword()
		{
			return this.strPasswd;
		}

		public boolean promptYesNo(String str)
		{
			return true;
		}

		public String getPassphrase()
		{
			return null;
		}

		public boolean promptPassphrase(String strMessage)
		{
			return true;
		}

		public boolean promptPassword(String strMessage)
		{
			return true;
		}

		public void showMessage(String strMessage) {}
	}


}

