package ink.toppest.secondskill.service;

import ink.toppest.secondskill.mapper.OrderMapper;
import ink.toppest.secondskill.mapper.StockMapper;
import ink.toppest.secondskill.pojo.Order;
import ink.toppest.secondskill.pojo.OrderRequest;
import ink.toppest.secondskill.pojo.Stock;
import ink.toppest.secondskill.util.JsonUtil;
import ink.toppest.secondskill.util.ScriptUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

/**
 * 描述:
 * 下单:
 *       pay1()为传统方式
 *       pay2()使用缓存以及消息队列可以提高访问速度
 *
 * @author HASEE
 * @create 2018-11-10 19:08
 */
@Service
public class PayService {
    @Autowired
    StockMapper stockMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Value("${redis.prefix.stockCount}")
    String stockCount;
    @Value("${redis.prefix.stockSold}")
    String stockSold;
    @Value("${kafka.topic}")
    String topic;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    /**
     *  下单分为三步:
     *               1.查看库存是否剩余
     *               2.库存sale字段增加
     *               3.生成订单，写入数据库
     **/
    public void pay1(OrderRequest orderRequest){
        long stock_id=orderRequest.getStock_id();
        Stock stock=stockMapper.getById(stock_id);
        if(stock.getSold()==stock.getSum()){  //库存已空
            throw new RuntimeException("库存已空");
        }
        int result=stockMapper.sale(stock_id,stock.getVersion());
        if(0==result){
            throw new RuntimeException("更新库存失败");
        }
        orderMapper.insert(new Order().setStock_id(stock_id)
                .setOrder_date(new Date(System.currentTimeMillis()))
                .setStock_name(stock.getStock_name())
        );
    }
    /**
     * @Author HASEE
     * @Description
     *      1.将库存信息存进redis,查询之后,redis中sale+1直接返回,
     *
     *      2.使用fakfa发送消息，更新数据库消息(更新库存以及保存订单信息).
     *
     **/
    public void pay2(Stock stock){
        long stock_id=stock.getStock_id();
        Boolean result1=ScriptUtil.readAndExecute("order.lua",stringRedisTemplate,Arrays.asList(stockCount+stock_id,stockSold+stock_id));
        if(!result1){
            throw new RuntimeException("库存不足");
        }
        kafkaTemplate.send(topic,stock_id+"",JsonUtil.ObjectToString(stock));
    }

}