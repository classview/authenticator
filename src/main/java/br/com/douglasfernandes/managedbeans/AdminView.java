package br.com.douglasfernandes.managedbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import br.com.douglasfernandes.dataservices.dao.interfaces.PerfilDao;
import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.utils.Logs;

/**
 * Mostra as informações na tela de administração de perfis
 * @author douglas.f.filho
 *
 */
@Controller
@Transactional
@ManagedBean
@SessionScoped
public class AdminView {
	
	@Autowired
	@Qualifier("perfilDaoImpl")
	PerfilDao perfilDao;
	private List<Perfil> perfis;
	
	public void getMsgListaPronta(){
		String mensagem = ""+perfis.size();
		if(perfis.size() < 2)
			mensagem += " perfil listado.";
		else
			mensagem += " perfis listados.";
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso",mensagem));
	}
	
	public List<Perfil> getListaPerfis(){
		Logs.info("[AdminView]::getListaPerfis::Lista de perfis obtida do objeto ja instanciado.");
    	
		return this.perfis;
	}
	
	public StreamedContent getImagem(){
		FacesContext context = FacesContext.getCurrentInstance();
		String perfilId = context.getExternalContext().getRequestParameterMap().get("perfilId");
		if(perfilId != null && !perfilId.equals("")){
			Logs.info("[AdminView]::getImagem::Id de perfil obtido da request = "+perfilId);
			long id = Long.parseLong(perfilId);
			Perfil perfil = perfilDao.pegarPorId(id);
			return perfil.getFotoAsStream();
		}
		else{
			return new DefaultStreamedContent();
		}
	}
	
	@PostConstruct
	public void init(){
		Logs.info("[AdminView]::init::Iniciando a pagina ao acessar a mesma.");
		try{
			Logs.info("[AdminView]::init::Obtendo lista de perfis...");
			perfis = perfilDao.listar();
			Logs.info("[AdminView]::init::Lista de perfis obtida e instancia no objeto de lista privado.");
		}
		catch(Exception e){
			Logs.warn("[AdminView]::acessar::Erro ao tentar exibir tela de perfis.Exception");
			e.printStackTrace();
		}
	}
	
}
