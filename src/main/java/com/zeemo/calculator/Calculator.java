package com.zeemo.calculator;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    public int sum(int a, int b){
        return a+b;
    }

    public int subtract(int a, int b){
        return a-b;
    }
}
