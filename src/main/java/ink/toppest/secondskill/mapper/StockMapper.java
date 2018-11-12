package ink.toppest.secondskill.mapper;


import ink.toppest.secondskill.pojo.Stock;
import org.apache.ibatis.annotations.*;

/**
 * 描述:
 * 库存数据访问
 *
 * @author HASEE
 * @create 2018-11-10 19:02
 */
public interface StockMapper {
    //使用乐观锁实现
    @Update("update tb_stock set sale=sale+1 , version=version+1 where id=#{id} and version=#{version}")
    int sale(@Param("id") long id,@Param("version") long version);

    @Select("select * from tb_stock where id=#{id}")
//    @ResultMap("stock_map")
    @Results(id="stock_map",value = {
            @Result(property = "stock_id", column = "id"),
            @Result(property = "stock_name", column = "name"),
            @Result(property = "sum", column = "count"),
            @Result(property = "sold", column = "sale"),
            @Result(property = "version", column = "version"),
    }
    )
    Stock getById(long id);
}