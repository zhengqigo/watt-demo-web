package cn.fuelteam.controller;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class NacosRefreshEventListener {

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
        if (this.ready.get()) {
            System.out.println("RefreshEvent received " + refreshEvent.getEventDesc());
            Set<String> keys = this.refresh.refresh();
            System.out.println("RefreshEvent keys changed: " + keys);
        }
    }
}