package cn.fuelteam.controller;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@Configuration
public class NacosRefreshEventListener {

    private Logger logger = LoggerFactory.getLogger(NacosRefreshEventListener.class);

    @Autowired
    private Environment environment;

    private ContextRefresher refresh;

    private AtomicBoolean ready = new AtomicBoolean(false);

    public NacosRefreshEventListener(ContextRefresher refresh) {
        this.refresh = refresh;
    }

    @EventListener
    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    @EventListener
    public void handle(RefreshEvent refreshEvent) {
        if (!this.ready.get()) return;
        Set<String> keys = this.refresh.refresh();
        for (String key : keys) {
            logger.info("Key {} refreshed value {}", key, environment.getProperty(key));
        }
    }
}