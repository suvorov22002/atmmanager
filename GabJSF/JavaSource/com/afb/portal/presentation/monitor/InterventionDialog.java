package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.hibernate.criterion.Restrictions;
import org.richfaces.model.selection.Selection;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.Intervention;
import com.afb.portal.jpa.gab.parameter.Equipement;
import com.afb.portal.jpa.gab.parameter.TypeIntervention;
import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IViewComponent;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * InterventionDialog
 * @author Owner
 * @version 1.0
 */
public class InterventionDialog extends AbstractDialog{


	/**
	 * Objet courant de la fenetre
	 */
	private Intervention currentObject = null;

	/**
	 * Nom de l employee
	 */
	private String txtName;

	/**
	 * Map d employee
	 */
	private Map<String, Atm> mapAtm = new HashMap<String, Atm>();


	private Selection selection;

	
	private String type;

	/**
	 * Items
	 */
	private List<SelectItem> items = new ArrayList<SelectItem>(); 
	
	/**
	 * 
	 */
	private List<EquipementItem> equipements = new ArrayList<EquipementItem>(); 

	/**
	 * 
	 */
	private Map<String, TypeIntervention> mapTypeInt = new HashMap<String, TypeIntervention>();

	
	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent	Composant Parent
	 */
	public InterventionDialog(DialogFormMode mode, Intervention currentObject, IViewComponent parent) {

		// Appel Parent
		super();

		// Initialisation des Parametres
		this.mode = mode;
		this.currentObject = currentObject;
		this.parent = parent;

		// Initialisation des Composants
		initComponents();

	}

	/**
	 * @return the selection
	 */
	public Selection getSelection() {
		return selection;
	}


	/**
	 * @param selection the selection to set
	 */
	public void setSelection(Selection selection) {
		this.selection = selection;
	}


	/**
	 * @return the txtName
	 */
	public String getTxtName() {
		return txtName;
	}

	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the items
	 */
	public List<SelectItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<SelectItem> items) {
		this.items = items;
	}

	/**
	 * @return the equipements
	 */
	public List<EquipementItem> getEquipements() {
		return equipements;
	}

	/**
	 * @param equipements the equipements to set
	 */
	public void setEquipements(List<EquipementItem> equipements) {
		this.equipements = equipements;
	}

	/**
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(Intervention currentObject) {
		this.currentObject = currentObject;
	}

	/**
	 * @param txtName the txtName to set
	 */
	public void setTxtName(String txtName) {
		this.txtName = txtName;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#checkBuildedCurrentObject()
	 */
	@Override
	public boolean checkBuildedCurrentObject() {
		
		if(txtName == null || txtName.trim().isEmpty()){
			return false;
		}else{
			if(!mapAtm.containsKey(txtName)){
				return false;
			}else{
				this.currentObject.setAtm(mapAtm.get(txtName));
			}
		}
		
		if(type == null || type.trim().isEmpty()){
			return false;
		}else{
			if(!mapTypeInt.containsKey(type)){
				return false;
			}else{
				this.currentObject.setType(mapTypeInt.get(type));
			}
		}
		
		List<Equipement> eqs = new ArrayList<Equipement>();
		for(EquipementItem item : equipements){
			if(Boolean.TRUE.equals(item.getActive())) eqs.add(item.getType());
		}
		if(!eqs.isEmpty()) this.currentObject.setEquipements(ConverterUtil.convertCollectionToSet(eqs));
		
		// On retourne true
		return true;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/InterventionDialog.xhtml";

	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {

		// On retourne le Titre
		return "InterventionListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractDialog#validate()
	 */
	@Override
	protected void validate(){

		// Si on est en mode Enregistrement
		if(mode.equals(DialogFormMode.CREATE)){
			// Enregistrement
			this.currentObject = ViewHelper.dao.save(currentObject);

		}else if(mode.equals(DialogFormMode.UPDATE)){
			// Enregistrement
			this.currentObject = ViewHelper.dao.update(currentObject);
		}
		
	}

	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		mapAtm.clear();
		mapTypeInt.clear();
		equipements.clear();
		items.clear();
	}

	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub

		try{
			
			mapTypeInt = new HashMap<String, TypeIntervention>();
			equipements = new ArrayList<EquipementItem>(); 
			items = new ArrayList<SelectItem>(); 
			mapAtm = new HashMap<String, Atm>();

			items = new ArrayList<SelectItem>();
			mapTypeInt = new HashMap<String, TypeIntervention>();
			for(TypeIntervention b : ViewHelper.dao.filter(TypeIntervention.class,null,null,null,null,0,-1)){
				items.add(new SelectItem(b.getId().toString(), b.getLibelle()));
				mapTypeInt.put(b.getId().toString(),b);
			}
						
			equipements = new ArrayList<EquipementItem>();
			if(DialogFormMode.UPDATE.equals(this.mode)){
				
				List<Equipement> eqs = ConverterUtil.convertCollectionToList(currentObject.getEquipements());
				for(Equipement eq : eqs){
					EquipementItem val = new EquipementItem(Boolean.TRUE,eq);
					equipements.add(val);
				}
				
				if(currentObject.getAtm() != null )txtName = currentObject.getAtm().getNom();
			}
			
			for(Equipement b : ViewHelper.dao.filter(Equipement.class,null,null,null,null,0,-1)){
				Boolean trouv = Boolean.FALSE;		
				for(EquipementItem item : equipements){
					if(item.getType().equals(b)) trouv = Boolean.TRUE;	
				}
				if(!Boolean.FALSE.equals(trouv)){
					EquipementItem val = new EquipementItem(b);
					equipements.add(val);
				}
				
			}
			
		}catch(Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);

		}

	}

	/**
	 * mthode de seelction du subjection box 
	 */	
	public List<Atm> chargeAtm(Object suggest) {

		mapAtm = new HashMap<String, Atm>();
		// Carractere saisie dans le subjetion box 
		String pref = (String)suggest;

		// Variable qui doit contenir la lsite des employe en fonction du carractere saisie
		ArrayList<Atm> result = new ArrayList<Atm>();

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("status",Boolean.TRUE));
		List<Atm> all = ViewHelper.dao.filter(Atm.class,null,rc,null,null,0,-1);

		// Recupere la liste des employees
		Iterator<Atm> iterator = all.iterator();

		while (iterator.hasNext()) {

			// Position sur le premier element de la liste
			Atm elem = ((Atm) iterator.next());

			// filtre en faonction des carrastesre sasies
			if ((elem.getNom() != null && elem.getNom().toLowerCase().indexOf(pref.toLowerCase()) == 0) || "".equals(pref))
			{
				// 	Ajout de de l employe selectionner dans la liste
				result.add(elem);
				mapAtm.put(elem.getNom().trim(),elem);
			}
		}

		// retourne le resultat
		return result;
	}


	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IDialog#getCurrentObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Intervention getCurrentObject() {
		// TODO Auto-generated method stub
		return this.currentObject;
	}

}
