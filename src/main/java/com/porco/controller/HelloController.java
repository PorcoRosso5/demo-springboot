package com.porco.controller;

import com.porco.config.MqConfig;
import com.porco.config.ScopeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private MqConfig.DemoConfig zmDemoConfig;
    @Autowired
    private MqConfig.DemoConfig demoConfig;
    @Autowired
    private ScopeConfig scopeConfig;

    @GetMapping
    public Object hello() {
        System.out.println(zmDemoConfig);
        System.out.println(demoConfig);
        System.out.println(scopeConfig);
        return demoConfig + " - " + zmDemoConfig;
    }

}
