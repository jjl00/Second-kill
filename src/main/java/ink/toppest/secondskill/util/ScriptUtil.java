package ink.toppest.secondskill.util;



import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 描述:
 *      读取或执行lua脚本
 * @author HASEE
 * @create 2018-11-11 11:19
 */

public class ScriptUtil {

    //读取配置文件成字符串
    public static String read(String path){
        StringBuilder sb = new StringBuilder();
        InputStream stream = ScriptUtil.class.getClassLoader().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        try {
            String str ;
            while((str = br.readLine()) != null) {
                sb.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
        return sb.toString();
    }
    //执行lua脚本
    public static boolean execute(StringRedisTemplate stringRedisTemplate, RedisScript<Long> luaScript, List<String> keys, String...args){
        return stringRedisTemplate.execute(luaScript,keys,args)==1;
    }
    //读取并执行
    public static boolean readAndExecute(String path,StringRedisTemplate stringRedisTemplate, List<String> keys, String...args){
        RedisScript<Long> luaScript = new DefaultRedisScript<>(read(path), Long.class);
        return execute(stringRedisTemplate,luaScript,keys,args);
    }

}