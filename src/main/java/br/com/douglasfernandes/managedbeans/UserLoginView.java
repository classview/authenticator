package br.com.douglasfernandes.managedbeans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.services.interfaces.PerfilService;
import br.com.douglasfernandes.pojos.DefaultResponse;
import br.com.douglasfernandes.utils.Logs;

/**
 * Apresenta e trata tela de login
 * @author douglas.f.filho
 *
 */
@Controller
@Transactional
@ManagedBean
@SessionScoped
public class UserLoginView {
	
	@Autowired
	@Qualifier("perfilServiceImpl")
	PerfilService perfilService;
	
	private Perfil perfil = new Perfil();
	
	public Perfil getPerfil(){
		return this.perfil;
	}
   
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        
        try{
        	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	HttpSession session = request.getSession();
        	
        	DefaultResponse response = perfilService.logarPerfil(perfil, session);
             
            FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
            context.addCallbackParam("loggedIn", response.getStatus());
            context.addCallbackParam("userRedirect", "home");
        }
        catch(Exception e){
        	Logs.info("[UserLoginView]::login::Erro fatal ao tentar logar. Exception");
        	e.printStackTrace();
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Erro","Erro no servidor."));
        	context.addCallbackParam("loggedIn", false);
        }
    }
    
    @PostConstruct
	public void init(){
    	Logs.info("[UserLoginView]::init::chamada ao dao necessaria para realizar autowire.");
    	perfilService.init();
    }
}
