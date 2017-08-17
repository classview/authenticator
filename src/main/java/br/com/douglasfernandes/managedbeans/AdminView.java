package br.com.douglasfernandes.managedbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.services.interfaces.PerfilService;
import br.com.douglasfernandes.pojos.response.DefaultResponse;
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
	
	private Perfil cadastrar;
	public Perfil getCadastrar(){
		return cadastrar;
	}
	
	private UploadedFile fotoCadastrar;
	public void setFotoCadastrar(UploadedFile fotoCadastrar){
		this.fotoCadastrar = fotoCadastrar;
	}
	public UploadedFile getfotoCadastrar(){
		return this.fotoCadastrar;
	}
	
	private UploadedFile fotoAtualizar;
	public void setFotoAtualizar(UploadedFile fotoAtualizar){
		this.fotoAtualizar = fotoAtualizar;
	}
	public UploadedFile getFotoAtualizar(){
		return this.fotoAtualizar;
	}
	
	private Perfil atualizar;
	public Perfil getAtualizar(){
		return this.atualizar;
	}
	public void setAtualizar(Perfil atualizar){
		this.atualizar = atualizar;
	}
	public void prepararAtualizacao(){
		FacesContext context = FacesContext.getCurrentInstance();
		String perfilId = context.getExternalContext().getRequestParameterMap().get("perfilId");
		if(perfilId != null && !perfilId.equals("")){
			long id = Long.parseLong(perfilId);
			atualizar = perfilService.pegarPorId(id);
			Logs.info("[AdminView]::prepararAtualizacao::Perfil a ser atualizado: "+atualizar.toString());
		}
	}
	public void atualizarPerfil(){
		Logs.info("[AdminView]::atualizarPerfil::Perfil a ser atualizado: "+atualizar.toString());
		if(fotoAtualizar != null && fotoAtualizar.getSize() > 5){
			Logs.info("[AdminView]::atualizarPerfil::Tamanho da nova foto = "+fotoAtualizar.getSize() + " bytes");
			atualizar.setFoto(fotoAtualizar.getContents());
		}
		
		DefaultResponse response = perfilService.atualizarPerfil(atualizar);
		
		FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
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
	
	public void cadastrarPerfil(){
		Logs.info("[AdminView]::cadastrarPerfil::Perfil a ser cadastrado: "+cadastrar.toString());
		if(fotoCadastrar != null && fotoCadastrar.getSize() > 5){
			Logs.info("[AdminView]::cadastrarPerfil::Tamanho da nova foto = "+fotoCadastrar.getSize());
			cadastrar.setFoto(fotoCadastrar.getContents());
		}
		
		DefaultResponse response = perfilService.adicionarPerfil(cadastrar);
		
		FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
	}
	
	public boolean podeAtualizarAdm(){
		int admins = 0;
		for(Perfil p : getListaPerfis()){
			if(p.isAdmin() && p.isAtivo())
				admins++;
		}
		if(admins > 1)
			return true;
		else
			return false;
	}
	
	public void atualizarStatusDePerfil(){
		FacesContext context = FacesContext.getCurrentInstance();
		String perfilId = context.getExternalContext().getRequestParameterMap().get("perfilId");
		if(perfilId != null && !perfilId.equals("")){
			long id = Long.parseLong(perfilId);
			DefaultResponse response = perfilService.atualizarStatusDoPerfil(id);
			FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
		}
	}
	
	@RequestMapping("mostrarFotoDePerfil")
	public void mostrarFoto(long id, HttpServletResponse response){
		try{
			Perfil perfil = perfilService.pegarPorId(id);
			byte[] foto = perfil.getFoto();
			
			if(foto != null)
			{
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				response.getOutputStream().write(foto);
				response.getOutputStream().close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void removerPerfil(){
		FacesContext context = FacesContext.getCurrentInstance();
		String perfilId = context.getExternalContext().getRequestParameterMap().get("perfilId");
		if(perfilId != null && !perfilId.equals("")){
			long id = Long.parseLong(perfilId);
			
			DefaultResponse response = perfilService.removerPerfil(id);
			
			FacesContext.getCurrentInstance().addMessage(null, response.getMensagem());
		}
		else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Id de perfil não encontrado."));
		}
	}
	
	@PostConstruct
	public void init(){
		Logs.info("[AdminView]::init::Iniciando a pagina ao acessar a mesma.");
		try{
			getListaPerfis();
			cadastrar = new Perfil();
			atualizar = new Perfil();
		}
		catch(Exception e){
			Logs.warn("[AdminView]::acessar::Erro ao tentar exibir tela de perfis.Exception");
			e.printStackTrace();
		}
	}
	
}
