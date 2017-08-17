package br.com.douglasfernandes.dataservices.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.pojos.response.DefaultResponse;

public interface PerfilService {
	public DefaultResponse logarPerfil(Perfil perfil, HttpSession session);
	public DefaultResponse adicionarPerfil(Perfil perfil);
	public Perfil pegarPorId(long id);
	public Perfil pegarPorEmail(String email);
	public DefaultResponse atualizarPerfil(Perfil perfil);
	public DefaultResponse atualizarStatusDoPerfil(long id);
	public DefaultResponse removerPerfil(long id);
	public List<Perfil> listarPerfis();
	
	public boolean init();
}
