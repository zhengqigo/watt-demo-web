package cn.fuelteam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;

@RestController
public class DubboController {

    @NacosValue(value = "${name:unknown}", autoRefreshed = true)
    private String name;

    @RequestMapping(value = "/hello")
    public String dubboSayHello() {
        return "hello " + name;
    }

    @NacosConfigListener(dataId = "DEMO_WEB", groupId = "DEV_GROUP", timeout = 500)
    public void onChange(String newContent) throws Exception {
        System.out.println("onChange : " + newContent);
    }
}