package com.zeemo.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    @Autowired
    public Calculator calculator;

    @RequestMapping("sum")
    public ResponseEntity<String> sum(@RequestParam("a") int a, @RequestParam("b") int b){
        int sum = a+b;
        return ResponseEntity.ok().body(String.valueOf(sum));
    }

    @RequestMapping("subtract")
    public ResponseEntity<String> subtract(@RequestParam("a") int a, @RequestParam("b") int b){
        int subtraction = a-b;
        return ResponseEntity.ok().body(String.valueOf(subtraction));
    }
}
