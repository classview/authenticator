package br.com.douglasfernandes.dataservices.dao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.pojos.DefaultResponse;
import br.com.douglasfernandes.utils.Logs;

/**
 * Esta classe identifica como proceder com atualizações no banco de dados na tablela de Perfil
 * @author douglas.f.filho
 *
 */
@Repository
@Transactional
public class PerfilDao{

	@PersistenceContext
	private EntityManager manager;
	
	public DefaultResponse logar(Perfil perfil, HttpSession session) {
		DefaultResponse response = new DefaultResponse();
		try{
			if(perfil != null){
				String nomeOuEmail = perfil.getNome();
				Perfil encontrado = pegarPorNome(nomeOuEmail);
				if(encontrado == null)
					encontrado = pegarPorEmail(nomeOuEmail);
				
				if(encontrado != null){
					String senha = perfil.getSenha();
					if(senha.equals(encontrado.getSenha())){
						session.setAttribute("logado", encontrado);
						Logs.info("[PerfilDao]::logar::Usuario logado");
						response.setStatus(true);
						response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem Vindo!", "Seja bem vindo, "+perfil.getNome()));
						return response;
					}
					else{
						Logs.warn("[PerfilDao]::logar::Usuario ou senha incorretos: "+perfil.toString());
						response.setStatus(false);
						response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro de login!", "Usuário e/ou senha incorretos."));
						return response;
					}
				}
				else{
					Logs.warn("[PerfilDao]::logar::Usuario ou senha incorretos: "+perfil.toString());
					response.setStatus(false);
					response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro de login!", "Usuário e/ou senha incorretos."));
					return response;
				}
			}
			else{
				Logs.warn("[PerfilDao]::logar::objeto perfil nulo.");
				response.setStatus(false);
				response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de login!", "Usuário e/ou senha nulos."));
				return response;
			}
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::logar::Falha fatal ao tentar logar perfil. Excecao:");
			e.printStackTrace();
			response.setStatus(false);
			response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de login!", "Usuário e/ou senha nulos."));
			return response;
		}
	}

	public DefaultResponse adicionar(Perfil perfil) {
		DefaultResponse response = new DefaultResponse();
		try{
			if(perfil != null){
				String nome = perfil.getNome();
				String email = perfil.getEmail();
				String senha = perfil.getSenha();
				if(!"".equals(nome) && !"".equals(email) && !"".equals(senha)){
					if(jaTemComEsteNome(nome)){
						Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar adicionar perfil: ja tem cadastrado.");
						response.setStatus(false);
						response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Já existe um perfil com este nome."));
					}
					else{
						if(jaTemComEsteEmail(email)){
							Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar adicionar perfil: ja tem cadastrado.");
							response.setStatus(false);
							response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Já existe um perfil com este email."));
						}
						else{
							manager.persist(perfil);
							Logs.warn("[PerfilDao]::adicionar:::Perfil [" + perfil.getNome() + "] cadastrado com exito.");
							response.setStatus(true);
							response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso","Perfil cadastrado!"));
						}
					}
				}
				else{
					Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar adicionar perfil. NULO");
					response.setStatus(false);
					response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Perfil com nome ou senha inválidos."));
				}
			}
			else{
				Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar adicionar perfil. NULO");
				response.setStatus(false);
				response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Perfil com nome ou senha inválidos."));
			}
			
			return response;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar adicionar perfil. Excecao:");
			e.printStackTrace();
			return response;
		}
	}

	public DefaultResponse atualizar(Perfil perfil) {
		DefaultResponse response = new DefaultResponse();
		try{
			if(perfil != null){
				long id = perfil.getId();
				String nome = perfil.getNome();
				String email = perfil.getEmail();
				String senha = perfil.getSenha();
				if(!"".equals(nome) && !"".equals(email) && !"".equals(senha)){
					Perfil conflito = pegarPorNome(nome);
					if(conflito != null && conflito.getId() != id){
						Logs.warn("[PerfilDao]::atualizar:::Erro ao tentar atualizar perfil: ja tem cadastrado.");
						response.setStatus(false);
						response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Já existe um perfil com este nome."));
					}
					else{
						conflito = pegarPorEmail(email);
						if(conflito != null && conflito.getId() != id){
							Logs.warn("[PerfilDao]::atualizar:::Erro ao tentar atualizar perfil: ja tem cadastrado.");
							response.setStatus(false);
							response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Já existe um perfil com este email."));
						}
						else{
							manager.merge(perfil);
							Logs.warn("[PerfilDao]::atualizar:::Perfil [" + perfil.getNome() + "] atualizado com exito.");
							response.setStatus(true);
							response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso","Perfil atualizado!"));
						}
					}
				}
				else{
					Logs.warn("[PerfilDao]::atualizar:::Erro ao tentar atualizar perfil. nome e/ou senha nulos");
					response.setStatus(false);
					response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Perfil com nome ou senha inválidos."));
				}
			}
			else{
				Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar atualizar perfil. nulo");
				response.setStatus(false);
				response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Perfil com nome ou senha inválidos."));
			}
			
			return response;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::adicionar:::Erro ao tentar atualizar perfil. Excecao:");
			e.printStackTrace();
			return response;
		}
	}

	public DefaultResponse remover(long id) {
		DefaultResponse response = new DefaultResponse();
		
		try{
			Perfil perfil = pegarPorId(id);
			manager.remove(perfil);
			response.setStatus(true);
			response.setMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucesso","Perfil removido com sucesso."));
			
			Logs.info("[PerfilDao]::remover::perfil removido.");
			
			return response;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::remover::Erro ao tentar remover perfil. Excecao:");
			e.printStackTrace();
			return response;
		}
	}

	public Perfil pegarPorId(long id) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.id = :id");
			query.setParameter("id", id);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDao]::pegarPorId:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDao]::pegarPorId:::Não existe perfil com este ID. [" + id + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDao]::pegarPorId:::Erro ao tentar pegar perfil por ID. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::pegarPorId:::Erro ao tentar pegar perfil por ID. Excecao:");
			e.printStackTrace();
			return null;
		}
	}

	public Perfil pegarPorNome(String nome) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.nome = :nome");
			query.setParameter("nome", nome);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDao]::pegarPorNome:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDao]::pegarPorNome:::Não existe perfil com este NOME. [" + nome + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDao]::pegarPorNome:::Erro ao tentar pegar perfil por Nome. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::pegarPorNome:::Erro ao tentar pegar perfil por NOME. Excecao:");
			e.printStackTrace();
			return null;
		}
	}
	
	public Perfil pegarPorEmail(String email) {
		try{
			Query query = manager.createQuery("select p from Perfil as p where p.email = :email");
			query.setParameter("email", email);
			Perfil encontrado = (Perfil)query.getSingleResult();
			if(encontrado != null){
				Logs.info("[PerfilDao]::pegarPorEmail:::Perfil encontrado: " + encontrado.toString());
				return encontrado;
			}
			else{
				Logs.warn("[PerfilDao]::pegarPorEmail:::Não existe perfil com este EMAIL. [" + email + "]");
				return null;
			}
		}
		catch(NoResultException e){
			Logs.warn("[PerfilDao]::pegarPorEmail:::Erro ao tentar pegar perfil por Email. NULO");
			return null;
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::pegarPorEmail:::Erro ao tentar pegar perfil por EMAIL. Excecao:");
			e.printStackTrace();
			return null;
		}
	}

	public List<Perfil> listar() {
		try{
			Query query = manager.createQuery("select p from Perfil as p");
			@SuppressWarnings("unchecked")
			List<Perfil> perfis = query.getResultList();
			if(perfis != null && perfis.size() > 0){
				Logs.info("[PerfilDao]::listar:::Perfis encontrado: " + perfis.size());
				return perfis;
			}
			else{
				Logs.warn("[PerfilDao]::listar:::Nenhum perfil listado.");
				return null;
			}
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::listar:::Erro ao tentar listar perfis. Excecao:");
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean jaTemComEsteNome(String nome){
		try{
			Perfil encontrado = pegarPorNome(nome);
			if(encontrado != null){
				Logs.info("[PerfilDao]::jaTemComEsteNome:::Ja existe perfil cadastrado com este nome [" + nome + "]");
				return true;
			}
			else{
				Logs.info("[PerfilDao]::jaTemComEsteNome:::Nao existe perfil cadastrado com este nome [" + nome + "]");
				return false;
			}
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::jaTemComEsteNome:::Erro ao tentar verificar se ja existe perfil com este nome [" + nome + "]. Excecao:");
			e.printStackTrace();
			return true;
		}
	}
	
	private boolean jaTemComEsteEmail(String email){
		try{
			Perfil encontrado = pegarPorEmail(email);
			if(encontrado != null){
				Logs.info("[PerfilDao]::jaTemComEsteEmail:::Ja existe perfil cadastrado com este email [" + email + "]");
				return true;
			}
			else{
				Logs.info("[PerfilDao]::jaTemComEsteEmail:::Nao existe perfil cadastrado com este email [" + email + "]");
				return false;
			}
		}
		catch(Exception e){
			Logs.warn("[PerfilDao]::jaTemComEsteEmail:::Erro ao tentar verificar se ja existe perfil com este email [" + email + "]. Excecao:");
			e.printStackTrace();
			return true;
		}
	}
	
	public void primeiroAcesso() throws Exception{
		String nome = "admin";
		String email = "admin@exemplo.com.br";
		
		Perfil perfil = new Perfil();
		perfil.setNome(nome);
		perfil.setSenha("admin123");
		perfil.setEmail(email);
		perfil.setDefaultFoto();
		perfil.setAdmin(true);
		
		adicionar(perfil);
	}
}