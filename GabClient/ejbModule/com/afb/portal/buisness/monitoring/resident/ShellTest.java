package com.afb.portal.buisness.monitoring.resident;

import java.util.List;

public class ShellTest {


	// ls -ltr  cd bin;tpstat   -	tcpdevice_reload -d 650 or 655   ; -	tcpdevice_reload -d 680
	static String txt ="tpstat";

	public static void main(String[] args) {


		main();
		
		
	}


	/**
	 * @param args String[] args
	 */
	public static void main() {
		// TODO Auto-generated method stub

		// svbo 
		//
		//ShellExecuter shell = new ShellExecuter("smartfe","smartfe1","172.21.254.31", 22);
		ShellExecuter shell = new ShellExecuter("trans","trans123","172.21.60.200", 22);
		shell.init();
		System.out.println("--"+"status_online");
		List<String> values = shell.executeDelta("./status_online");
		for(String v : values){
			System.out.println(v);
		}
		shell.exit();
		
		shell.init();
		System.out.println("--"+"start_online");
		values = shell.executeDelta("./start_online");
		for(String v:values){
			System.out.println(v);
		}
		shell.exit();
		
		try {
			Thread.sleep(5*60*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		shell = new ShellExecuter("smartfe","smartfe1","172.21.254.31", 22);
		shell.init();
		System.out.println("--"+"tcpdevice_reload -d 6000 ");
		values = shell.executesvfe("tcpdevice_reload -d 6000 ");
		for(String v:values){
			System.out.println(v);
		}
		shell.exit();
		
		shell = new ShellExecuter("trans","trans123","172.21.60.200", 22);
		shell.init();
		System.out.println("--"+"status_online");
		values = shell.executeDelta("./status_online");
		for(String v:values){
			System.out.println(v);
		}
		shell.exit();
		
		
		
		
		/**
		shell.init();
		System.out.println("--"+"tpstat");
		values = shell.executesvfe("tpstat");
		for(String v:values){
			System.out.println(v);
		}
		shell.exit();*/

	}

	
}
