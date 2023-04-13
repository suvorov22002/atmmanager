package com.afb.portal.buisness.monitoring.worker;

/**
 * 
 * @author Owner
 * @version 1.0
 */
public class AccessDirectory {

	/**
	 * 
	 * @param ip
	 * @param login
	 * @param password
	 */
	public static void clear(){
		try{
			String script = "NET USE * /DELETE";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});			    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param ip
	 * @param login
	 * @param password
	 */
	public static void procces(String ip,String login , String password){
		try{

			String script = "NET USE * /DELETE /YES";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});

			if(login.isEmpty())script = "net use * \\\\"+ip.trim()+"\\c$ /USER:administrateur password /PERSISTENT:NO";
			else script = "net use * \\\\"+ip.trim()+"\\c$ /USER:"+login.trim()+" "+password+" /PERSISTENT:NO";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
