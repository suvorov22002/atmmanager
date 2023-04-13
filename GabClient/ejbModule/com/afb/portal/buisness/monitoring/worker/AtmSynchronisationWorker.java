package com.afb.portal.buisness.monitoring.worker;

public class AtmSynchronisationWorker {

	/**
	public  void Synconisation() {

		List<Atm> Atms = new ArrayList<Atm>();

		try {

			String sql ="select tid, street,city,c1_start_bills,c2_start_bills,c3_start_bills,c4_start_bills, c_c1bills,c_c2bills,c_c3bills,c_c4bills "
				+ "from ctr_TAB a , def_tab b "
				+ "where a.pid=b.id and c1_start_bills is not null";

			openSVFEConnection();
			ResultSet rs = conSVISTA.createStatement().executeQuery(sql);
			while(rs.next()){          
				// debut
				String tid = rs.getString("tid");
				String street = rs.getString("street");
				String city = rs.getString("city");
				Atm atm = new Atm(tid, street, street, city, 2500000, 1000000);
				Atms.add(atm);
			}
			closeSVFEConnection();

			openATFFConnection();
			for(Atm val : Atms){

				sql = "insert into atmmanager values('"+val.getTid()+"','"+val.getNom()+"','"+val.getAdresse()+"','"+val.getVille()+"','"+val.getSodleMin()+"','"+val.getSodlecritique()+"')";
				conATFF.createStatement().executeUpdate(sql);
			}
			closeATFFConnection();

		}catch (Exception ex) {
			ex.printStackTrace();
		}

	}*/
	
}
