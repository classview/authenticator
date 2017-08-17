package br.com.douglasfernandes.interceptor;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.douglasfernandes.dataservices.entities.Perfil;

/**
 * Intercepta solicitações (requests) antes de entregar ao controller
 * @author douglas.f.filho
 *
 */
public class MainInterceptor extends HandlerInterceptorAdapter implements PhaseListener{

	private String[] permissions = new String[]{
			"/resources/",
			"dynamiccontent",
			"esqueciMinhaSenha",
			"testar",
			"application",
			"/login",
			"/login.xhtml",
			"servicesDescriptor",
			"services"
	};	
	
	private boolean containsPermission(String uri){
		boolean contains = false;
		for(String p : permissions){
			if(uri.contains(p))
				contains = true;
		}
		return contains;
	}
	
	/**
	 * Auto generated default serial.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object controller)
	 {
		  try{
			  String uri = request.getRequestURI();
		      
			  Perfil logado = (Perfil)request.getSession().getAttribute("logado");
			  
			  if(containsPermission(uri)){
				  return true;
			  }
			  
			  if(logado != null){
				  return true;
			  }
			  else{
				  response.sendRedirect("login");
				  return false;
			  }
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  return false;
		  }
	 }

	@Override
	public void afterPhase(PhaseEvent event) {
		try{
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String uri = request.getRequestURI();
			
			HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			Perfil logado = (Perfil)request.getSession().getAttribute("logado");
			
			if(!containsPermission(uri)){
				if(logado == null)
					response.sendRedirect("login");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
	}
	
}
