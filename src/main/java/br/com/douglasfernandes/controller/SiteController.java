package br.com.douglasfernandes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.douglasfernandes.dataservices.dao.PerfilDao;
import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.factory.DataService;
import br.com.douglasfernandes.utils.Logs;

@Controller
public class SiteController {
	
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
		PerfilDao perfilDao = DataService.getPerfilService(session.getServletContext()).getPerfilAccess();
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
