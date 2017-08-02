package br.com.douglasfernandes.managedbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.services.interfaces.PerfilService;
import br.com.douglasfernandes.pojos.DefaultResponse;
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
	@Qualifier("perfilServiceImpl")
	PerfilService perfilService;
	
	private Perfil cadastrar = new Perfil();
	public Perfil getCadastrar(){
		return cadastrar;
	}
	
	private UploadedFile file;
	public UploadedFile getFile() {
        return file;
    }
	public void setFile(UploadedFile file) {
        this.file = file;
        Logs.info("[AdminView]::setFile::Arquivo subido ao servidor. Size: "+file.getSize());
        cadastrar.setFoto(file.getContents());
    }
	
	private static List<Perfil> perfis;
	public List<Perfil> getListaPerfis(){
		Logs.info("[AdminView]::init::Obtendo lista de perfis...");
		perfis = perfilService.listarPerfis();
		return perfis;
	}
	public void getMsgListaPronta(){
		String mensagem = ""+perfis.size();
		if(perfis.size() < 2)
			mensagem += " perfil listado.";
		else
			mensagem += " perfis listados.";
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso",mensagem));
	}
	
	public StreamedContent getImagem(){
		FacesContext context = FacesContext.getCurrentInstance();
		String perfilId = context.getExternalContext().getRequestParameterMap().get("perfilId");
		if(perfilId != null && !perfilId.equals("")){
			Logs.info("[AdminView]::getImagem::Id de perfil obtido da request = "+perfilId);
			long id = Long.parseLong(perfilId);
			return perfilService.getFotoDePerfil(id);
		}
		else{
			return new DefaultStreamedContent();
		}
	}
	
	public void cadastrarPerfil(){
		Logs.info("[AdminView]::cadastrarPerfil::Perfil a ser cadastrado: "+cadastrar.toString());
		DefaultResponse response = perfilService.adicionarPerfil(cadastrar);
		
		cadastrar = new Perfil();
		
		FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
	}
	
	@PostConstruct
	public void init(){
		Logs.info("[AdminView]::init::Iniciando a pagina ao acessar a mesma.");
		try{
			getListaPerfis();
		}
		catch(Exception e){
			Logs.warn("[AdminView]::acessar::Erro ao tentar exibir tela de perfis.Exception");
			e.printStackTrace();
		}
	}
	
}
