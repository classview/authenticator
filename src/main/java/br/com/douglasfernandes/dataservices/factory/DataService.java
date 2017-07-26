package br.com.douglasfernandes.dataservices.factory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.jsf.FacesContextUtils;

import br.com.douglasfernandes.dataservices.services.PerfilService;
import br.com.douglasfernandes.utils.Logs;

@Transactional
public class DataService {
	
	public static PerfilService getPerfilService(FacesContext facesContext){
		WebApplicationContext wac = FacesContextUtils.getWebApplicationContext(facesContext);
		return getPerfilService(wac);
	}
	
	public static PerfilService getPerfilService(ServletContext servletContext){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		return getPerfilService(wac);
	}
	
	public static PerfilService getPerfilService(WebApplicationContext webApplicationContext){
		ApplicationContext applicationContext = webApplicationContext;
		Logs.info("[DBService]::getPerfilService::app name: "+applicationContext.getDisplayName());
		
		PerfilService service = (PerfilService)applicationContext.getBean("perfilService");
		if(service != null)
			Logs.info("[DBService]::getPerfilService::service: PerfilService");
		else
			Logs.info("[DBService]::getPerfilService::service: null");
		
		return service;
	}
}
