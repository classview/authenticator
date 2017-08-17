package br.com.douglasfernandes.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("services")
public class ServicesDescriptorService {

	@RequestMapping("")
	public String servicesDescriptor(){
		return "servicesDescriptor";
	}
}
