package br.com.douglasfernandes.testes;

import br.com.douglasfernandes.pojos.request.LoginServiceRequest;

public class Testes {
	public static void main(String[] args) {
		try{
			String jsonRequest = "{\"request\":{\"nome\":\"admin\",\"senha\":\"admin123\"}}";
			LoginServiceRequest request = LoginServiceRequest.getServiceFromJson(jsonRequest);
			
			System.out.println(request.getNome());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
