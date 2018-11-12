package ink.toppest.secondskill.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 描述:
 *下单请求
 * @author HASEE
 * @create 2018-11-10 18:54
 */
@Data
@Accessors(chain = true)
public class OrderRequest {
    String requestNO;  //请求号
    long stock_id;
    Date requestDate;
}