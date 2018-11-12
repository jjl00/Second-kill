package ink.toppest.secondskill;

import ink.toppest.secondskill.advice.CurrentLimiting;
import ink.toppest.secondskill.advice.PreventRepetition;
import ink.toppest.secondskill.pojo.OrderRequest;
import ink.toppest.secondskill.pojo.Stock;
import ink.toppest.secondskill.service.PayService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@SpringBootApplication
@MapperScan(basePackages = "ink.toppest.secondskill.mapper")
@RestController
//@EnableAspectJAutoProxy(proxyTargetClass = true) //开启cglib代理
@EnableAspectJAutoProxy
public class SecondsKillApplication {
    @Autowired
    PayService payService;

    public static void main(String[] args) {
        SpringApplication.run(SecondsKillApplication.class, args);
    }

    @GetMapping("/")
//  @CurrentLimiting("1000")
//  @PostMapping("/")
//  @PreventRepetition
    public String test(){
        payService.pay2(new Stock().setStock_id(1));
        return "ok";
    }
}
