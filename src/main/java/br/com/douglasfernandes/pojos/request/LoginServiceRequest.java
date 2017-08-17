package br.com.douglasfernandes.pojos.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import br.com.douglasfernandes.utils.JSONUtil;

@XStreamAlias("request")
public class LoginServiceRequest {
	@XStreamAlias("nome")
	private String nome;
	@XStreamAlias("senha")
	private String senha;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public static LoginServiceRequest getServiceFromJson(String json) throws Exception{
		return JSONUtil.convertFromJson(json, LoginServiceRequest.class);
	}
	
	public String toJson() throws Exception{
		return JSONUtil.convertToJson(this);
	}
	
	@Override
	public String toString(){
		return "LoginServiceRequest [nome=" + nome + ", senha=*****]";
	}
}
