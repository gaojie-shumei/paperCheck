package com.csuweb.PaperCheck.web.util;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.core.bean.XsPaper;

public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
		
	  /**
     * 转换成json对象
     *
     * @param user
     * @return
     */
	public static String generateJson(Object object) throws Exception{
        String json = "";
        json= mapper.writeValueAsString(object);
        return json;
    }
	
	
	public static String generateJson(Sentence sentence) throws Exception{
        String json = "";
        json= mapper.writeValueAsString(sentence);
        return json;
    }
    
	public static Object generateObject(String json,Class<?> valueType) throws Exception{
        mapper.configure(Feature.AUTO_CLOSE_JSON_CONTENT, Boolean.TRUE);
	    Object object = mapper.readValue(json, valueType);
	    return object;
    }


	public static Object generateObject(String json, TypeReference<List<XsPaper>> typeReference) throws JsonParseException, JsonMappingException, IOException {
	    Object object = mapper.readValue(json, typeReference);
	    return object;
	}
    
}
