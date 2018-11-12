package ink.toppest.secondskill.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 描述:
 *
 * @author HASEE
 * @create 2018-11-11 18:52
 */
public class JsonUtil {

    public static String ObjectToString(Object o){
        try {
            return inner.objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T stringToObject(String value,Class<T> clazz){
        try {
                return inner.objectMapper.readValue(value,clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static class inner{
         static ObjectMapper objectMapper=new ObjectMapper();
    }
}