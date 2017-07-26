package br.com.douglasfernandes.dataservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.douglasfernandes.dataservices.dao.PerfilDao;
import br.com.douglasfernandes.utils.Logs;

@Controller
@Transactional
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
public class PerfilService {
	
	PerfilDao perfilDao;

	public PerfilDao getPerfilAccess() {
		return perfilDao;
	}
	
	@Autowired
	public PerfilService(PerfilDao perfilDao) {
		this.perfilDao = perfilDao;
		if(perfilDao == null)
			Logs.info("[PerfilDaoService]::construtor::perfilDao nulo");
		else
			Logs.info("[PerfilDaoService]::construtor::perfilDao setado");
	}
}
