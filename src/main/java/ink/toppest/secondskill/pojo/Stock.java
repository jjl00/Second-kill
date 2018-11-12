package ink.toppest.secondskill.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述:
 * 库存信息
 *
 * @author HASEE
 * @create 2018-11-10 17:45
 */
@Data
@Accessors(chain = true)
public class Stock {
    long stock_id;
    String stock_name;   //商品名称
    long sum;      //库存总量
    long sold;     //已售总量
    long version;  //版本号，用于乐观锁更新数据库
}