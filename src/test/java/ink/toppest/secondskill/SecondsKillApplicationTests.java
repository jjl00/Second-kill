package ink.toppest.secondskill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.toppest.secondskill.mapper.OrderMapper;
import ink.toppest.secondskill.mapper.StockMapper;
import ink.toppest.secondskill.pojo.Order;
import ink.toppest.secondskill.pojo.OrderRequest;
import ink.toppest.secondskill.pojo.Stock;
import ink.toppest.secondskill.service.PayService;
import ink.toppest.secondskill.util.JsonUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecondsKillApplicationTests {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    PayService payService;

    @Autowired
    private RedisTemplate stringRedisTemplate;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @Test
    public void contextLoads() {
      payService.pay2(new Stock().setStock_id(1));
        for(int i=0;i<30;i++){
//            kafkaTemplate.send("order","test"+i+"",JsonUtil.ObjectToString(new Stock().setStock_id(1).setStock_name("电脑")));
        }
    }

}
