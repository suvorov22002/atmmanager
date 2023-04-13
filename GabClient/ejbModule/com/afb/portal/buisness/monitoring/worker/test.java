package com.afb.portal.buisness.monitoring.worker;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(NetworkPing.isReachableByPing("10.119.12.25"));
		System.out.println(NetworkPing.isReachableByPing("10.119.12.25") ? " 10.119.12.25 Host is reachable" : " 10.119.12.25 Host is NOT reachable");
		
	}

}
