package ink.toppest.secondskill.advice;

import ink.toppest.secondskill.pojo.OrderRequest;
import ink.toppest.secondskill.util.NullBody;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;



/**
 * 描述:
 *      先根据请求号查询redis，如果查不到就说明未请求过，并在redis插入
 *      如果能查到说明已经请求过，所以抛出异常，防止重复请求.
 * @author HASEE
 * @create 2018-11-11 11:44
 */
@Aspect
@Component
@Order(1)
public class PreventRepetitionAdvice {
    @Pointcut("@annotation(ink.toppest.secondskill.advice.PreventRepetition)")
    private void check(){}

    @Value("${redis.prefix.requestNo}")
    String prefix;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private NullBody nullBody;
    @Before("check()")
    public void before(JoinPoint joinPoint) throws Exception {
        Object[] objects=joinPoint.getArgs();
        for (Object o:objects){
            if(o instanceof OrderRequest){
                String requestNo=((OrderRequest) o).getRequestNO(); //获取请求号
                Object result=(stringRedisTemplate.opsForValue().get(prefix+requestNo));
                if(null==result){
                    stringRedisTemplate.opsForValue().set(prefix+requestNo,nullBody.toString());
                    return;
                }else {
                    throw new RuntimeException("请求重复");
                }
            }
        }
    }

}