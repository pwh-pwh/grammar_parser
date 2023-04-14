package com.example.grammar_parser.utils;

import com.example.grammar_parser.entry.CFG;
import com.example.grammar_parser.entry.First;

import java.util.List;
import java.util.Set;

public class FirstUtil {
    public static First getFirstByName(Character name,List<First> firstList){
        for (First first : firstList) {
            if (first.getToken()==name){
                return first;
            }
        }
        throw new IllegalArgumentException("找不到该名字的first");
    }
    public static First getFirstByName(Character name, CFG cfg){
        return getFirstByName(name,cfg.getFirstList());
    }

}
