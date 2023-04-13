/**
 * 
 */
package com.afb.portal.presentation.tools.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filtre d'initialisation des Parametres Utilisateurs apres connexion
 * @author Francis K
 * @version 1.0
 */
public class NavigatorCheckingFilter implements Filter {
	
	/**
	 * Etat permettant de savoir que le navigateur a deja ete controle
	 */
	public static final String NAVIGATOR_ALREADY_CHECKED = "NAVIGATOR_ALREADY_CHECKED";

	/**
	 * Le Logger
	 */
	private static Log logger = LogFactory.getLog(NavigatorCheckingFilter.class);
	
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
		
		// Si l'Utilisateur a deja ete charge
		if(session.getAttribute(NAVIGATOR_ALREADY_CHECKED) != null) {
			
			// On filtre
			chain.doFilter(servletRequest, servletResponse);
			
			// On sort
			return;
		}
		
		// Obtention de l'agent de la requete
		String user_agent = request.getHeader("User-Agent");
		
		// On affiche
		logger.info("User-Agent: " + user_agent);
		
		// Si le Navigateur n'est pas FireFox
		if(!user_agent.matches(".*Firefox/.*") && !user_agent.matches(".*FireFox/.*")) {
			
			// Un Log
			logger.info("Redirection vers une page de téléchargement de FireFox");
			
			// On redirige ver une Page de telechargement de FireFox
						
		}
		
		// On positionne l'etat de chargement
		session.setAttribute(NAVIGATOR_ALREADY_CHECKED, NAVIGATOR_ALREADY_CHECKED);
		
		// On filtre
		chain.doFilter(servletRequest, servletResponse);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {}
}
