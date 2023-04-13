/**
 * 
 */
package com.afb.portal.presentation.tools.filters;

import java.io.File;
import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Filtre d'initialisation des Parametres Utilisateurs apres connexion
 * @author Francis K
 * @version 1.0
 */
public class UserInitializerFilter implements Filter {

	/**
	 * Etat permettant de savoir que l'Utilisateur a deja ete charge
	 */
	public static final String USER_DATA_ALREADY_LOADED = "USER_DATA_ALREADY_LOADED";


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		// On caste la requete
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		// La Session
		HttpSession session = request.getSession(true);
		
		// Adresse IP du Client
		String clientIPAdress = request.getRemoteAddr();

		Long uid = request.getParameter("uid") == null ? null : Long.valueOf(request.getParameter("uid")) ;

		// Si l'id de l'utilisateur n'est pas null
		if(uid != null) {
			try{
				if ( ViewHelper.facadeManager == null ) ViewHelper.facadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );
			}catch(Exception e){}
			// Si le service Facade du portail est demarre
			if ( ViewHelper.facadeManager != null ) {
				// Recherche de l'utilisateur connecte
				User user = (User) ViewHelper.facadeManager.findByProperty(User.class, "id", uid);
				// Si l'utilisateur a ete retrouve
				if( user != null ) {
					// Lecture de l'adresse ip du poste utilisateur
					user.setIpAddress(clientIPAdress);
					// On Positionne l'Utilisateur
					session.setAttribute(PortalHelper.CONNECTED_USER_SESSION_NAME, user );	
				}		
			}
		}
				
		// Chemin de base des Ressources
		ViewHelper.ROOT_DATAS_DIR = getRootDataDirectory();
				
		// On positionne l'etat de chargement
		session.setAttribute(USER_DATA_ALREADY_LOADED, USER_DATA_ALREADY_LOADED);

		// On filtre
		chain.doFilter(servletRequest, servletResponse);


	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {}

	/**
	 * Methode d'obtention du Repertoire racine de stockage des donnees de l'application
	 * @return Repertoire racine de stockage des donnees de l'application
	 */
	public String getRootDataDirectory() {

		// On recalcule
		String rootDataDirectory = System.getProperty("jboss.server.data.dir", ".") + File.separator + "PortalDatas";

		// On retourne
		return rootDataDirectory;
	}
	
	
}
