package cn.fuelteam;

import org.fuelteam.watt.swagger2.EnableSwagger2;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

@SpringBootApplication(scanBasePackages = { "cn.fuelteam" })
@DubboComponentScan(value = "cn.fuelteam")
@EnableSwagger2
@EnableAutoConfiguration
@NacosPropertySource(dataId = "demowebconfig", groupId = "demowebgroup", 
        first = true, autoRefreshed = true, 
        before = SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, after = SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)
public class DemoWebApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoWebApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
