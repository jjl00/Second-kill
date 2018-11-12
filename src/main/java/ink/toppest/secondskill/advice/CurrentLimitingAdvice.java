package ink.toppest.secondskill.advice;


import ink.toppest.secondskill.util.ScriptUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;



/**
 * 描述:
 *
 * @author HASEE
 * @create 2018-11-10 20:33
 */
@Aspect
@Component
@Order(5)
public class CurrentLimitingAdvice {
    public static RedisScript<Long> luaScript;
    static {                    //加载lua脚本
        try {
            String script=ScriptUtil.read("limit.lua");
            luaScript = new DefaultRedisScript<>(script, Long.class);
            } catch (Exception e) {
            // do nothing
        }
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Pointcut("@annotation(ink.toppest.secondskill.advice.CurrentLimiting)")
    private void limit(){}

    @Before("limit()")
    public void before(JoinPoint joinPoint) throws Exception {
        String limit=null;
        Method targetMethod =((MethodSignature)joinPoint.getSignature()).getMethod();
        //遍历找出@CurrentLimiting的value值,即限流数
        for(Annotation annotation:targetMethod.getDeclaredAnnotations()){
            if(annotation instanceof ink.toppest.secondskill.advice.CurrentLimiting){
                limit=((CurrentLimiting) annotation).value();
            }
        }
        //同一分钟出现的请求使用同一个键,达到每分钟限流的效果
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        if(!ScriptUtil.execute(stringRedisTemplate,luaScript,Collections.singletonList(key),limit)){
            throw new RuntimeException("限流!!");       //超过限流数，抛出异常
        }
    }
}