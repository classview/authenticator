package br.com.douglasfernandes.pojos.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.com.douglasfernandes.utils.JSONUtil;
import br.com.douglasfernandes.utils.Logs;

@XStreamAlias("response")
public class LoginServiceResponse{
	
	@XStreamAlias("result")
	private String result;
	
	@XStreamAlias("success")
	private boolean success;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@Override
	public String toString() {
		String response = "{}";
		try {
			response = JSONUtil.convertToJson(this);
		} catch (Exception e) {
			Logs.warn("[LoginServiceResponse]::toString::Erro ao tentar converter objeto para json. Exception:");
			e.printStackTrace();
		}
		return response;
	}
	
	public static LoginServiceResponse getServiceFromJson(String json){
		LoginServiceResponse response = new LoginServiceResponse();
		response.setResult("");
		response.setSuccess(false);
		
		try {
			response = JSONUtil.convertFromJson(json, LoginServiceResponse.class);
		} catch (Exception e) {
			Logs.warn("[LoginServiceResponse]::getServiceFromJson::Erro ao tentar converter json para onjeto. Exception:");
			e.printStackTrace();
		}
		
		return response;
	}
	
}
