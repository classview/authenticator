package br.com.douglasfernandes.dataservices.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.douglasfernandes.dataservices.dao.interfaces.PerfilDao;
import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.utils.Logs;

/**
 * Esta classe identifica como proceder com atualizações no banco de dados na tablela de Perfil
 * @author douglas.f.filho
 *
 */
@Repository
public class PerfilDaoImpl implements PerfilDao{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public void adicionar(Perfil perfil) throws Exception{
		manager.persist(perfil);
	}

	@Override
	public void atualizar(Perfil perfil) throws Exception{
		manager.merge(perfil);
	}

	@Override
	public void remover(Perfil perfil) throws Exception{
		manager.remove(perfil);
	}

	@Override
	public Perfil pegarPorId(long id) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.id = :id");
			query.setParameter("id", id);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDaoImpl]::pegarPorId:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDaoImpl]::pegarPorId:::Não existe perfil com este ID. [" + id + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDaoImpl]::pegarPorId:::Erro ao tentar pegar perfil por ID. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDaoImpl]::pegarPorId:::Erro ao tentar pegar perfil por ID. Excecao:");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Perfil pegarPorNome(String nome) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.nome = :nome");
			query.setParameter("nome", nome);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDaoImpl]::pegarPorNome:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDaoImpl]::pegarPorNome:::Não existe perfil com este NOME. [" + nome + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDaoImpl]::pegarPorNome:::Erro ao tentar pegar perfil por Nome. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDaoImpl]::pegarPorNome:::Erro ao tentar pegar perfil por NOME. Excecao:");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Perfil pegarPorEmail(String email) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.email = :email");
			query.setParameter("email", email);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDaoImpl]::pegarPorEmail:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDaoImpl]::pegarPorEmail:::Não existe perfil com este EMAIL. [" + email + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDaoImpl]::pegarPorEmail:::Erro ao tentar pegar perfil por Email. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDaoImpl]::pegarPorEmail:::Erro ao tentar pegar perfil por EMAIL. Excecao:");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Perfil> listar() throws Exception{
		Query query = manager.createQuery("select p from Perfil as p");
		@SuppressWarnings("unchecked")
		List<Perfil> perfis = query.getResultList();
		if(perfis != null && perfis.size() > 0){
			Logs.info("[PerfilDaoImpl]::listar:::Perfis encontrado: " + perfis.size());
			return perfis;
		}
		else{
			Logs.warn("[PerfilDaoImpl]::listar:::Nenhum perfil listado.");
			return null;
		}
	}
	
}