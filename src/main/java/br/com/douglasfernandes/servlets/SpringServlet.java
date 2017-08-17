package br.com.douglasfernandes.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

@WebServlet(name = "springmvc",
			urlPatterns = {"/"},
			initParams = {
					@WebInitParam(name = "contextConfigLocation",value = "/WEB-INF/applicationContext.xml")
			},
			loadOnStartup = 1)
public class SpringServlet extends DispatcherServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		super.service(request, response);
	}
	
}
