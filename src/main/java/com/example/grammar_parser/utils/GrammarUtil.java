package com.example.grammar_parser.utils;

import com.example.grammar_parser.entry.Grammar;

import java.util.*;

public class GrammarUtil {
    public static Grammar transformStrToGrammar(Character left,String right) {
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList(right.split("\\|")));
        stringList.removeIf(e-> e.length() == 0);
        Set<String> set = new HashSet<>(stringList);
        return new Grammar(left,set);
    }
    public static int countNTNum(String str){
        int count = 0;
        for (char c : str.toCharArray()) {
            if(CharUtil.isNoTToken(c)){
                count++;
            }
        }
        return count;
    }

    public static int countTNum(String str){
        int count = 0;
        for (char c : str.toCharArray()) {
            if(CharUtil.isTToken(c)){
                count++;
            }
        }
        return count;
    }
    public static void main(String[] args) {
/*        Grammar grammar = GrammarUtil.transformStrToGrammar('f', "|aa|aa|b|cc|b|");
        System.out.println(grammar.toString());*/
    }
}
