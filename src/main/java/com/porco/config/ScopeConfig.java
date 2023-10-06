package com.porco.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ScopeConfig {
    public ScopeConfig() {
        System.out.println("ScopeConfig");
    }
}
