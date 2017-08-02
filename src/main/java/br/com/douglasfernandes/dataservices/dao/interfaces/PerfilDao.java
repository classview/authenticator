package br.com.douglasfernandes.dataservices.dao.interfaces;

import java.util.List;

import br.com.douglasfernandes.dataservices.entities.Perfil;

public interface PerfilDao {
	public void adicionar(Perfil perfil) throws Exception;
	public void atualizar(Perfil perfil) throws Exception;
	public void remover(Perfil perfil) throws Exception;
	public Perfil pegarPorId(long id);
	public Perfil pegarPorNome(String nome);
	public Perfil pegarPorEmail(String email);
	public List<Perfil> listar() throws Exception;
}
