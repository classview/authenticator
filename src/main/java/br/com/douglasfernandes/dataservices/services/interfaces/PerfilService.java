package br.com.douglasfernandes.dataservices.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.primefaces.model.StreamedContent;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.pojos.DefaultResponse;

public interface PerfilService {
	public DefaultResponse logarPerfil(Perfil perfil, HttpSession session);
	public DefaultResponse adicionarPerfil(Perfil perfil);
	public DefaultResponse atualizarPerfil(Perfil perfil);
	public DefaultResponse removerPerfil(long id);
	public List<Perfil> listarPerfis();
	
	public StreamedContent getFotoDePerfil(long id);
	
	public void init();
}
