package br.com.douglasfernandes.managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import br.com.douglasfernandes.dataservices.dao.PerfilDao;
import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.factory.DataService;
import br.com.douglasfernandes.pojos.DefaultResponse;
import br.com.douglasfernandes.utils.Logs;

/**
 * Apresenta e trata tela de login
 * @author douglas.f.filho
 *
 */
@ManagedBean
@SessionScoped
public class UserLoginView {
	private Perfil perfil = new Perfil();
	
	public Perfil getPerfil(){
		return this.perfil;
	}
   
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        
        try{
        	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	HttpSession session = request.getSession();
        	
        	PerfilDao perfilDao = DataService.getPerfilService(FacesContext.getCurrentInstance()).getPerfilAccess();
        	
            DefaultResponse response = perfilDao.logar(perfil, session);
             
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
}
