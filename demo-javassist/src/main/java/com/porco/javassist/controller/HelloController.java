package com.porco.javassist.controller;

import com.porco.javassist.domain.Result;
import com.porco.javassist.mapper.RecordMapper;
import com.porco.javassist.service.ExcelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class HelloController {

    @Resource
    private ExcelService excelService;

    @Resource
    private RecordMapper mapper;

    @PostMapping("/login")
    public Result upload() throws Exception {
        Map<String, String> token = new HashMap<>();
        token.put("token", "user-token");
        return Result.of(token);
    }

    @GetMapping("/user")
    public Result download(@RequestHeader("Authorization") String token) throws Exception {
        if (token.contains("Bearer user-token")) {
            return Result.ok();
        }
        return Result.fail(token);
    }


}
