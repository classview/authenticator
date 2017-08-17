package br.com.douglasfernandes.utils;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	private static Properties props;
	private static String username;
	private static String password;
	
	/**
	 * Envia o mesmo e-mail para todos da lista
	 * @param emails
	 */
	public static boolean enviar(String emails, String assunto, String corpo) throws Exception
	{
		InputStream inputFromFile = EmailUtil.class.getClassLoader().getResourceAsStream("mailing.properties");
		
		props = new Properties();
		props.load(inputFromFile);
		
		username = props.getProperty("username");
		password = props.getProperty("password");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(true);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		
		String lista = emails.replace("'", "").replace("[", "").replace("]", "");
		
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(lista));
		message.setSubject(assunto);
		message.setText(corpo);
		
		Transport.send(message);
		
		return true;
	}
}
