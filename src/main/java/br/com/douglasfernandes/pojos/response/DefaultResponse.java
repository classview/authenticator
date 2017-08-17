package br.com.douglasfernandes.pojos.response;

import javax.faces.application.FacesMessage;

public class DefaultResponse {
	private boolean status = false;
	private FacesMessage mensagem;
	
	public DefaultResponse(){
		status = false;
		mensagem = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro fatal", "Erro!");
	}
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public FacesMessage getMensagem() {
		return mensagem;
	}
	public void setMensagem(FacesMessage mensagem) {
		this.mensagem = mensagem;
	}
	
	@Override
	public String toString() {
		return "DefaultResponse [status=" + status + ", mensagem=" + mensagem.getSummary() + ": " + mensagem.getDetail() + "]";
	}
}
