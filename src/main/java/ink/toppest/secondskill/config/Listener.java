package ink.toppest.secondskill.config;

/**
 * 描述:
 *      kafka消费,开启三个线程，每个线程处理一个分区(主题为order)
 *      每个线程取出库存信息，并更新数据库.
 * @author HASEE
 * @create 2018-11-11 19:18
 */
import ink.toppest.secondskill.mapper.OrderMapper;
import ink.toppest.secondskill.mapper.StockMapper;
import ink.toppest.secondskill.pojo.Order;
import ink.toppest.secondskill.pojo.Stock;
import ink.toppest.secondskill.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Slf4j
public class Listener {
    @Autowired
    StockMapper stockMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @KafkaListener(topicPartitions = @TopicPartition(topic = "order",partitions = "0"))
    public void listen1(ConsumerRecord<?, ?> record){
        Stock stock = JsonUtil.stringToObject(record.value().toString(),Stock.class);
        doOrder(stock);
    }


    @KafkaListener(topicPartitions = @TopicPartition(topic = "order",partitions = "1"))
    public void listen2(ConsumerRecord<?, ?> record){
        Stock stock = JsonUtil.stringToObject(record.value().toString(),Stock.class);
        doOrder(stock);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "order",partitions = "2"))
    public void listen3(ConsumerRecord<?, ?> record){
        Stock stock = JsonUtil.stringToObject(record.value().toString(),Stock.class);
        doOrder(stock);
    }

    private void doOrder(Stock stock){
        long stock_id=stock.getStock_id();
        stock=stockMapper.getById(stock_id);
        int result=stockMapper.sale(stock_id,stock.getVersion());
        if(0==result){
            throw new RuntimeException("更新库存失败");
        }
        orderMapper.insert(new Order().setStock_id(stock_id)
                .setOrder_date(new Date(System.currentTimeMillis()))
                .setStock_name(stock.getStock_name())
        );
    }
}