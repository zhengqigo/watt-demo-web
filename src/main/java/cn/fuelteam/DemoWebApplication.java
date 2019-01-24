package cn.fuelteam;

import org.fuelteam.watt.swagger2.EnableSwagger2;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication(scanBasePackages = { "cn.fuelteam" })
@DubboComponentScan(value = "cn.fuelteam")
@EnableSwagger2
@EnableAutoConfiguration
public class DemoWebApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoWebApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
