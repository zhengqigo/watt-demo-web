package cn.fuelteam.controller;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;

@RestController
@RefreshScope
public class DubboController {

    @Value(value = "${name:unknown}")
    private String name;

    @RequestMapping(value = "/hello")
    public String dubboSayHello() {
        return "hello " + name;
    }

    @NacosConfigListener(dataId = "changeDataId1", groupId = DEFAULT_GROUP, timeout = 500)
    public void onChange(String newContent) throws Exception {
        System.out.println("onChange : " + newContent);
    }
}