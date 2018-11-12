package ink.toppest.secondskill.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * 描述:
 * 订单信息
 *
 * @author HASEE
 * @create 2018-11-10 17:48
 */
@Data
@Accessors(chain = true)
public class Order {
    long order_id;
    long stock_id;
    String stock_name;
    Date order_date;    //下单日期
}