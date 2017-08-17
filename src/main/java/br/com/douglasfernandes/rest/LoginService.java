package br.com.douglasfernandes.rest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.douglasfernandes.dataservices.entities.Perfil;
import br.com.douglasfernandes.dataservices.services.interfaces.PerfilService;
import br.com.douglasfernandes.pojos.request.LoginServiceRequest;
import br.com.douglasfernandes.pojos.response.DefaultResponse;
import br.com.douglasfernandes.pojos.response.LoginServiceResponse;
import br.com.douglasfernandes.utils.Logs;

@Controller
@RequestMapping("services/loginService")
@Transactional
public class LoginService {
	@Autowired
	@Qualifier("perfilServiceImpl")
	PerfilService perfilService;
	
	@RequestMapping(value={"status",""})
	public void status(HttpServletResponse response){
		try{
			boolean isOk = perfilService.init();
			String status = "fail";
			if(isOk)
				status = "ok";
				
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().append("{\"status\":\"" + status + "\"}");
			response.getWriter().close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping("login")
	public void login(String loginServiceRequest, HttpServletResponse servletResponse, HttpSession session){
		try{
			Logs.info("[LoginService]::login::loginServiceRequest: "+loginServiceRequest);
			LoginServiceRequest request = LoginServiceRequest.getServiceFromJson(loginServiceRequest);
			
			Perfil perfil = new Perfil();
			perfil.setNome(request.getNome());
			perfil.setSenha(request.getSenha());
			
			DefaultResponse serviceResponse = perfilService.logarPerfil(perfil, session);
			LoginServiceResponse response = new LoginServiceResponse();
			response.setResult(serviceResponse.getMensagem().getSummary());
			response.setSuccess(serviceResponse.getStatus());
			
			servletResponse.setContentType("application/json");
			servletResponse.setCharacterEncoding("utf-8");
			servletResponse.getWriter().append(response.toString());
			servletResponse.getWriter().close();
		}catch(Exception e){
			Logs.warn("[LoginService]::login::Erro ao tentar fazer login.Exception");
			e.printStackTrace();
		}
	}
	
}
