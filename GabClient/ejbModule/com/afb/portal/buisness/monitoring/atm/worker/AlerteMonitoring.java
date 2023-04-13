package com.afb.portal.buisness.monitoring.atm.worker;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IAlerteMonitoring;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;
import com.afb.portal.webservcice.entities.SMSWeb;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;

/**
 * @author Owner
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
public class AlerteMonitoring implements IAlerteMonitoring{

	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao;

	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager;


	public static final String moduleCode = "GABM_001";


	public static final String moduleName ="MonitoringATM";


	private String from = "alerteGab@afrilandfirstbank.com";


	public static final Integer rupture = 100000;
	
	private DataSystem system = null;
	
	
	public Integer cpte = 0;
	
	private TimerTask task;
	private Timer timer;

    /**
     * creerTimer
     */
	public void creerTimer(){

		try{
									
			// boucle
			int sec = 60;
			int min = 20;
			
			if(timer != null)timer.cancel();
			if(task != null)task.cancel();
				
			task = new TimerTask(){
				@Override
				public void run(){
					try {
						process();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),5) , min*sec*1000);

		}catch(Exception e){
			e.printStackTrace();
			creerTimer();
		}
		
	}
	
		
	/**
	 * process
	 */
	public void process(){

		try{
						
			//System.out.println("----Alerte Monitoring----ATM----");
			// Controle de l heure
			if(Boolean.FALSE.equals(repportManager.checkTimeSolde())) return;
			
			GabParameter parameter = repportManager.findParameter("MAILS_FROM");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					from = parameter.getValue(); 
				}
			}

			if(system == null )system = repportManager.findDataSystem("SMVISTA");
			String contenu ="";
			String sql = "";
			ResultSet rs ;
			List<String> mailsTo = new ArrayList<String>();
			List<String> mailsToCC = new ArrayList<String>();
			ConnexionSVFE svfe = new ConnexionSVFE();

			svfe.openSVFEConnection(system);
			List<Atm> Atms = repportManager.findActiveAtm(Boolean.FALSE);
			//System.out.println("---------AlerteMonitoring-----------"+Atms.size());

			for(Atm value :Atms){

				// consultion du solde
				/**
				sql ="select tid, street,city,c1_start_bills,c2_start_bills,c3_start_bills,c4_start_bills, c_c1bills,c_c2bills,c_c3bills,c_c4bills,c_c1brej,c_c2brej,c_c3brej,c_c4brej "
					+ "from ctr_TAB a , def_tab b "
					+ "where a.pid=b.id and tid='"+value.getTid()+"' and c1_start_bills is not null";
				*/
				
				sql ="select ATM_PID,ATM_TID,INITIAL1,INITIAL2,INITIAL3,INITIAL4,CURRENT_BILLS1,CURRENT_BILLS2,CURRENT_BILLS3,CURRENT_BILLS4,DVAL1,DVAL2,DVAL3,DVAL4 "
					+ "from ATM_CASH "
					+ "where ATM_TID='"+value.getTid()+"' ";

				// execution
				rs = svfe.Execute(sql);

				if(rs.next()){

					// debut
					Integer c1_start_bills = rs.getInt("INITIAL1");
					Integer c2_start_bills = rs.getInt("INITIAL2");
					Integer c3_start_bills = rs.getInt("INITIAL3");
					Integer c4_start_bills = rs.getInt("INITIAL4");

					// reste
					Integer c_c1bills = rs.getInt("CURRENT_BILLS1");
					Integer c_c2bills = rs.getInt("CURRENT_BILLS2");
					Integer c_c3bills = rs.getInt("CURRENT_BILLS3");
					Integer c_c4bills = rs.getInt("CURRENT_BILLS4");

					// Rejet 
					Integer n1 = rs.getInt("DVAL1");
					Integer n2 = rs.getInt("DVAL2");
					Integer n3 = rs.getInt("DVAL3");
					Integer n4 = rs.getInt("DVAL4");

					// calcul
					Integer SoldeInitial =  (c1_start_bills*n1)+(c2_start_bills*n2)+(c3_start_bills*n3)+(c4_start_bills*n4);
					Integer SoldeRestant = (c_c1bills*n1)+(c_c2bills*n2)+(c_c3bills*n3)+(c_c4bills*n4);

					// libelle message
					String libelle = "";

					if(SoldeRestant < 0) SoldeRestant = 0;

					// set Value
					if(SoldeRestant <= rupture  && SoldeInitial > 0  ){		

						contenu ="Bonjour "+"\n"+"\n"
						+ "Nous Vous informons que le Gab :"+value.getNom()+"  "
						+ " est en rupture de fonds "+"\n"
						+ "Veillez prendre des dispositions urgentes pour approvisionner le GAb"+"\n"+"\n"
						+ "Cordialement..";

						// Archivage
						TypeIncident typInci = repportManager.findTypeIncident(Incident.ruptureFonds);
						Incident inci = repportManager.findIncident(value, typInci);
						
						if(inci == null){
							inci = new Incident();
							inci.setAtm(value);
							inci.setTypeIncident(typInci);
							dao.save(inci);
							libelle ="Rupture de fonds dans le "+value.getNom();
							
							// Alerte
							mailsTo = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
									dao.save(sms);
								}
								if(user.getEmail() != null && !user.getEmail().trim().isEmpty()  ){
									mailsTo.add(user.getEmail());
								}							
							}

							mailsToCC = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
									dao.save(sms);
								}
								if(user.getEmail() != null && !user.getEmail().trim().isEmpty() ){
									mailsToCC.add(user.getEmail());
								}	
							}

							// MAIL
							SendFileEmail.SendMailProvider(from, mailsTo,mailsToCC, libelle, contenu);
							
						}else{
							
							inci.setNomber(inci.getNomber() + 1);
							cpte = inci.getNomber() * 25;
							cpte = cpte%125;
							if(cpte==0){
								// Alerte
								cpte = cpte / 25;
								inci.setNomber(inci.getNomber() + 1);
								dao.update(inci);
								libelle =" "+inci.getNomber().toString()+"/"+inci.getNomber().toString()+": Rupture de fonds dans le "+value.getNom();
								mailsTo = new ArrayList<String>();
								for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
									// SMS 
									if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
										SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
										dao.save(sms);
									}
									if(user.getEmail() != null && !user.getEmail().trim().isEmpty()  ){
										mailsTo.add(user.getEmail());
									}							
								}

								mailsToCC = new ArrayList<String>();
								for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
									// SMS 
									if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
										SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
										dao.save(sms);
									}
									if(user.getEmail() != null && !user.getEmail().trim().isEmpty()  ){
										mailsToCC.add(user.getEmail());
									}	
								}

								// MAIL
								SendFileEmail.SendMailProvider(from, mailsTo,mailsToCC, libelle, contenu);
							}else{
								dao.update(inci);
							}
						}

					}else if(SoldeRestant <= value.getSodlecritique() && SoldeInitial > 0  && SoldeRestant > rupture){

						// Archivage
						TypeIncident typInci = repportManager.findTypeIncident(Incident.SoldeCritique);
						Incident inci = repportManager.findIncident(value, typInci);
												
						if(inci == null){

							inci = new Incident();
							inci.setAtm(value);
							inci.setTypeIncident(typInci);
							dao.save(inci);
							libelle ="Solde Critique sur le "+value.getNom();

							// Alerte
							mailsTo = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000") ){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
									dao.save(sms);
								}
								if(user.getEmail() != null && !user.getEmail().trim().isEmpty() ){
									mailsTo.add(user.getEmail());
								}							
							}

							mailsToCC = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
								if(StringUtils.isNotBlank(user.getPhone())){
									// SMS 
									if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
										SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
										dao.save(sms);
									}
								}
								if(StringUtils.isNotBlank(user.getEmail())){
									if(user.getEmail() != null && !user.getEmail().trim().isEmpty() && !user.getPhone().startsWith("000") ){
										mailsToCC.add(user.getEmail());
									}
								}
									
							}

							contenu ="Bonjour "+"\n"+"\n"
							+ " Nous Vous informons que le Gab :"+value.getNom()+""
							+ " a atteins son solde Critique qui est de'"+value.getSodlecritique()+"' le Montant disponible dans le Gab est de : "+SoldeRestant+"  \n"
							+ " Veillez prendre des dispositions  pour approvisionner le GAb"+"\n"+"\n"
							+ " Cordialement";

							// MAIL
							SendFileEmail.SendMailProvider(from, mailsTo,mailsToCC, libelle, contenu);

						}

					}else if(SoldeRestant <= value.getSodleMin() && SoldeInitial > 0 &&  SoldeRestant > value.getSodlecritique()){

						// Archivage
						TypeIncident typInci = repportManager.findTypeIncident(Incident.SoldeMin);
						Incident inci = repportManager.findIncident(value, typInci);
												
						if(inci == null){

							inci = new Incident();
							inci.setAtm(value);
							inci.setTypeIncident(typInci);
							dao.save(inci);
							libelle ="Solde Minimum sur le "+value.getNom();

							// Alerte
							mailsTo = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000") ){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
									dao.save(sms);
								}
								if(user.getEmail() != null && !user.getEmail().trim().isEmpty()){
									mailsTo.add(user.getEmail());
								}							
							}

							mailsToCC = new ArrayList<String>();
							for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelle, user.getPhone());
									dao.save(sms);
								}
								if(user.getEmail() != null && !user.getEmail().trim().isEmpty() ){
									mailsToCC.add(user.getEmail());
								}	
							}

							contenu ="Bonjour "+"\n"+"\n"
							+ "Nous Vous informons que le Gab :"+value.getNom()+""
							+ " a atteins son solde Minimum qui est de'"+value.getSodleMin()+"' le Montant disponible dans le Gab est de : "+SoldeRestant+"  \n"
							+ "Veillez prendre des dispositions pour approvisionner le GAb"+"\n"+"\n"
							+ "Cordialement";

							// MAIL
							SendFileEmail.SendMailProvider(from, mailsTo,mailsToCC, libelle, contenu);

						}

					}
				}
			}

			svfe.closeSVFEConnection();

		}catch (Exception ex){
			//SendEmail.SendMailAdministrator("Erreur :"+ex.toString());
			ex.printStackTrace();
		}

	}

}
