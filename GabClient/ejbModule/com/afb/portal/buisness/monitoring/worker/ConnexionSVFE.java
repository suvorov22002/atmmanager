package com.afb.portal.buisness.monitoring.worker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

/**
 * 
 * @author Owner
 * @version 1.0
 */
public class ConnexionSVFE {

	// Objet Connexion à Delta
	private  Connection conSVISTA;
	
	// Objet Connexion a ATFF
	public  Statement statement;
	
	/**
	 * 
	 * @param system
	 * @throws Exception
	 */
	public void openSVFEConnection(DataSystem system) throws Exception {

		Class.forName(system.getProviderClassName());
		conSVISTA = DriverManager.getConnection(system.getDbConnectionString(), system.getDbUserName(), system.getDbPassword());
		statement = conSVISTA.createStatement();
	}

	/**
	 * closeSVFEConnection
	 * @throws Exception
	 */
	public void closeSVFEConnection() throws Exception {
		if(statement != null) statement.close();
		if(conSVISTA != null) conSVISTA.close();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet Execute(String sql) throws SQLException{
		return statement.executeQuery(sql);
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public int ExecuteUpdate(String sql) throws SQLException{
		return statement.executeUpdate(sql);
	}
	
	/**
	 * getTime
	 */
	public static Boolean  checkTime(){
		String txtheure = new SimpleDateFormat("HH:MM:ss").format(new Date());
		if((Integer.valueOf(txtheure.substring(0,2)) < 18)  ){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}
	
}
