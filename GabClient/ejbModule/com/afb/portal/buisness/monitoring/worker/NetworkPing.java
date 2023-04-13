/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afb.portal.buisness.monitoring.worker;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author Francis
 */
public class NetworkPing {


	public static void main(String[] args){

		try{

			System.out.println(System.getProperty("os.name"));

			InetAddress address = InetAddress.getByName("10.165.214.52");
			boolean reachable = address.isReachable(10000);
			System.out.println("Is host reachable? " + reachable);

			address = InetAddress.getByName("172.21.10.103");
			reachable = address.isReachable(10000);
			System.out.println("Is host reachable? " + reachable);


			InetAddress.getByName("10.165.214.52").isReachable(10000);
			//System.out.println(isReachableByPing("10.119.12.25"));
			//System.out.println(isReachableByPing("10.119.12.25") ? " 10.119.12.25 Host is reachable" : " 10.119.12.25 Host is NOT reachable");

			/**
			System.out.println(isReachableByPing("127.0.0.1"));
			System.out.println(isReachableByPing("127.0.0.1") ? " 127.0.0.1 Host is reachable" : "127.0.0.1 Host is NOT reachable");

			System.out.println(isReachableByPing("172.21.10.12"));
			System.out.println(isReachableByPing("172.21.10.12") ? "172.21.10.12 Host is reachable" : " 172.21.10.12 Host is NOT reachable");

			System.out.println(isReachableByPing("172.21.10.57"));
			System.out.println(isReachableByPing("172.21.10.57") ? " 172.21.10.57 Host is reachable" : " 172.21.10.57 Host is NOT reachable");

			System.out.println(isReachableByPing("172.21.20.37"));
			System.out.println(isReachableByPing("172.21.20.37") ? " 172.21.20.37 Host is reachable" : " 172.21.20.37 Host is NOT reachable");

			System.out.println(isReachableByPing("172.21.70.121"));
			System.out.println(isReachableByPing("172.21.70.121") ? " 172.21.70.121 Host is reachable" : " 172.21.70.121 Host is NOT reachable");
			 */

		}catch (Exception e) {
			// TODO: handle exception

		}



	}



	public static boolean ping(String host) {

		int returnVal = 0;

		try{
			boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

			ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows? "-n" : "-c", "1", host);
			Process proc = processBuilder.start();
			//proc.wait(20000);
			returnVal = proc.waitFor();
			//proc.wait(20000);
			//returnVal = proc.waitFor();
			//return returnVal == 0;

		}catch(IOException ex){
			return false;
		}catch(InterruptedException ee){
			return false;
		}

		return returnVal == 0;

	}


	public static boolean isReachableByPing(String host){

		//boolean ping = false;
		boolean ping = isReachablePing(host);
		try{

			//InetAddress address = InetAddress.getByName("172.21.70.121");
			//ping = address.isReachable(10000);
			if(ping == false){
				Thread.sleep(100);
				ping = isReachablePing(host);
				if(ping == false){
					Thread.sleep(100);
					ping = isReachablePing(host);
				}
				if(ping == false){
					Thread.sleep(100);
					ping = isReachablePing(host);
				}
				if(ping == false){
					Thread.sleep(100);
					ping = isReachablePing(host);
				}
				if(ping == false){
					ping = InetAddress.getByName(host).isReachable(10000);
				}
			}
		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
		return ping;		
	}

	public static boolean isReachablePing(String host) {
		try{
			String cmd = "";
			if(System.getProperty("os.name").startsWith("Windows")) {   
				// For Windows
				cmd = "ping -n 1 " + host;
			} else {
				// For Linux and OSX
				cmd = "ping -c 1 " + host;
				//cmd = "ping -c 1 " + host;
			}
			Process myProcess = Runtime.getRuntime().exec(cmd);
			myProcess.waitFor();
			//System.out.println("------Ping sur-"+host+"---"+myProcess.exitValue());
			if(myProcess.exitValue() == 0) {
				return true;
			} else {
				return false;
			}
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
	}

}
