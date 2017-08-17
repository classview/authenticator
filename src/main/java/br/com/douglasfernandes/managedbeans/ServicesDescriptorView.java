package br.com.douglasfernandes.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.douglasfernandes.pojos.response.ServiceDescriptor;

@ManagedBean
@SessionScoped
public class ServicesDescriptorView {
	private List<ServiceDescriptor> services;
	
	public List<ServiceDescriptor> getServices(){
		return services;
	}

	@PostConstruct
	public void init(){
		services = new ArrayList<ServiceDescriptor>();
		
		ServiceDescriptor loginService = new ServiceDescriptor("LoginService","services/loginService");
		
		services.add(loginService);
	}
	
}
