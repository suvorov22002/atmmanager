package com.afb.portal.buisness.monitoring.resident;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * ShellExecuter
 * @author Owner
 * @version 1.0
 */
public class ShellExecuter {

	private String user ="trans"; // username for remote host
	private String pwd ="trans123"; // password of the remote host
	private String host = "172.21.60.200"; // remote host address
	private int port = 22;
	
	private static String bashsvfe = "PATH=$PATH:/home/smartfe/bin; export PATH;unset USERNAME;" +
	"export ORACLE_BASE=/oracle;export ORACLE_HOME=/oracle/11.2.0;export ORACLE_SID=svfe;" +
	"export ORACLE_TERM=vt100; export NLS_LANG=American_America.RU8PC866;" +
	"LD_LIBRARY_PATH=$ORACLE_HOME/lib:/lib64:/lib:/usr/lib64:/usr/lib;" +
	"LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib:/usr/local/ssl/lib;" +
	"export LD_LIBRARY_PATH ; export SV_HOME=$HOME ;export DB_LOGIN=svista/svista1@svfe;" +
	"export PATH=$ORACLE_HOME/bin:/opt/automake/bin:$SV_HOME/bin:$PATH;" +
	". $SV_HOME/etc/sv_profile;" +
	"alias cle='(cd ~/output/outfiles; rm *; close_out all;)';" +
	"export SV_PROJECT_CODE=AFF;" +
	"export SVN=svn+ssh://svn@svn/SVFE;" +
	"alias cle='(cd ~/output/outfiles; rm *; close_out all;)';" +
	"alias cdl='cd $SV_HOME/output/logfiles';" +
	"alias cdo='cd $SV_HOME/output/outfiles';" +
	"alias cdi='cd $SV_HOME/input/autoload';" +
	"alias dunzip='~/dunzip.sh'";
	
	
	private static String bashDelta ="PATH=/usr/bin:/etc:/usr/sbin:/usr/ucb:$HOME/bin:/usr/bin/X11:/sbin:.;" +
			"export PATH; export DATABASE=afri; export OS=AIX ; set -o vi; export AFFICH_CMDE=N;" +
			"export TERM=vt220; export PROFILE=/app/delta/v10/profile; cd $PROFILE; . profile_trans ; . /home/informix/enviro.sh;" +
			"cd /echange/requetes";
	

	/**
	 * ShellExecuter
	 * @param user
	 * @param pwd
	 * @param host
	 * @param port
	 */
	public ShellExecuter(String user, String pwd, String host, int port) {
		super();
		this.user = user;
		this.pwd = pwd;
		this.host = host;
		this.port = port;
	}

	private JSch jsch;
	private Session session;
	private ChannelExec channelExec;

	/**
	 * init
	 */
	public void init(){
		try{
			jsch = new JSch();
			/**System.out.println("-user-"+user);
			System.out.println("-host-"+host);
			System.out.println("-port-"+port);
			System.out.println("-pwd-"+pwd);*/
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(pwd);
			session.connect();
			channelExec = (ChannelExec)session.openChannel("exec");
			//System.out.println("-OK-");
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
	}

	/**
	 * execute
	 * @param script
	 * @return
	 */
	public List<String> execute(String script){
		List<String> result = new ArrayList<String>();
		try{
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(script);
			channelExec.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null){
				result.add(line);
			}
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
		return result;
	}
	
	
	/**
	 * execute
	 * @param script
	 * @return
	 */
	public List<String> executeDelta(String script){
		script = bashDelta+";cd $HOME"+";"+script;
		System.out.println("script: " + script);
		List<String> result = new ArrayList<String>();
		try{
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(script);
			channelExec.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			System.out.println("reader: " + reader);
			System.out.println("line: " + reader.readLine());
			while ((line = reader.readLine()) != null){
				result.add(line);
			}
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
		return result;
	}
	
	
	/**
	 * execute
	 * @param script
	 * @return
	 */
	public List<String> executesvfe(String script){
		script = bashsvfe+";"+script;
		List<String> result = new ArrayList<String>();
		try{
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(script);
			channelExec.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null){
				result.add(line);
			}
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
		return result;
	}

	/**
	 * Exit
	 */
	public void exit(){
		try{
			channelExec.disconnect();
			session.disconnect();
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
	}

}
