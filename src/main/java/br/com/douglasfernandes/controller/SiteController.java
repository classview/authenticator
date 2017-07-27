package br.com.douglasfernandes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.douglasfernandes.dataservices.dao.interfaces.PerfilDao;
import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.utils.Logs;

@Controller
@Transactional
public class SiteController {
	
	@Autowired
	@Qualifier("perfilDaoImpl")
	PerfilDao perfilDao;
	
	@RequestMapping(value={"/","home"})
	public String home(HttpSession session){
		
		Perfil logado = (Perfil)session.getAttribute("logado");
		if(logado != null){
			if(logado.getAdmin())
				return "admin";
			else
				return "index";
		}
		else{
			session.invalidate();
			return "redirect:login";
		}
	}
	
	@RequestMapping("login")
	public String login(HttpSession session){
		try {
			perfilDao.primeiroAcesso();
		} catch (Exception e) {
			Logs.warn("[SiteController]::login::Erro ao tentar configurar foto padrão de perfil.");
			e.printStackTrace();
			return "login";
		}
		return "login";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:login";
	}
	
}
