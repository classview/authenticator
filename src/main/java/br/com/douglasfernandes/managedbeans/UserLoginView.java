package br.com.douglasfernandes.managedbeans;

import java.util.Calendar;

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
import br.com.douglasfernandes.pojos.response.DefaultResponse;
import br.com.douglasfernandes.utils.EmailUtil;
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
	
	private Perfil perfil;
	
	public Perfil getPerfil(){
		return this.perfil;
	}
	public void setPerfil(Perfil perfil){
		this.perfil = perfil;
	}
	
	public void mostrarMensagem(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aguarde","Um e-mail será enviado com os passos pra sua nova senha."));
	}
	
	public void esqueciMinhaSenha(){
		try{
			String email = this.perfil.getNome();
			Logs.info("[UserLoginView]::esqueciMinhaSenha::E-mail de recuperacao: "+email);
			Perfil perfil = perfilService.pegarPorEmail(email);
			
			if(perfil != null){
				Calendar momento1 = Calendar.getInstance();
				Calendar momento2 = Calendar.getInstance();
				
				long m1 = momento1.getTimeInMillis() * 2;
				long m2 = momento2.getTimeInMillis();
				
				long senha = m1 + m2;
				
				if(EmailUtil.enviar(email, "ClassView Login", "Você solicitou sua senha de acesso: "+senha)){
					perfil.setSenha(""+senha);
					perfilService.atualizarPerfil(perfil);
					
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso","Uma nova senha de recuperação foi enviada ao seu e-mail."));
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro","Verifique se seu e-mail foi digitado corretamente."));
				}
			}
			else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro","Verifique se seu e-mail foi digitado corretamente."));
			}
			
		}
		catch(Exception e){
			Logs.info("[UserLoginView]::esqueciMinhaSenha::Erro fatal ao tentar enviar senha por e-mail. Exception");
        	e.printStackTrace();
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Erro","Erro no servidor."));
		}
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
    	Logs.info("[UserLoginView]::init::chamada de inicializacao.");
    	perfilService.init();
    	perfil = new Perfil();
    }
}
