package cn.smxy.zhouxuelian9;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.smxy.zhouxuelian9.mapper")
public class Zhouxuelian9Application {

    public static void main(String[] args) {
        SpringApplication.run(Zhouxuelian9Application.class, args);
    }

}
