package br.com.douglasfernandes.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class JSONUtil {

	public static String convertToJson(Object obj) throws Exception{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(obj.getClass());
		xstream.setMode(XStream.NO_REFERENCES);
		String json = xstream.toXML(obj);
		
		return json;
	}
	
	public static <T> T convertFromJson(String json, Class<T> clazz) throws Exception{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(clazz);
		xstream.setMode(XStream.NO_REFERENCES);
		XStream.setupDefaultSecurity(xstream);
		xstream.addPermission(AnyTypePermission.ANY);
		
		@SuppressWarnings("unchecked")
		T obj = (T)xstream.fromXML(json);
		
		
		return obj;
	}
	
}
