package ink.toppest.secondskill.mapper;

import ink.toppest.secondskill.pojo.Order;
import org.apache.ibatis.annotations.Insert;



/**
 * 描述:
 * 订单数据访问
 * @author HASEE
 * @create 2018-11-10 18:31
 */
//@Mapper
public interface OrderMapper {
    @Insert("insert into tb_order(sid,name,create_time) values(#{stock_id},#{stock_name},#{order_date})")
    void insert(Order order);
}