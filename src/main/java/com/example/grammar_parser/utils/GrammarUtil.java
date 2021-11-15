package com.example.grammar_parser.utils;

import com.example.grammar_parser.entry.Grammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GrammarUtil {
    public static Grammar transformStrToGrammar(Character left,String right) {
        String[] strings = right.split("\\|");
        Set<String> set = new HashSet<>(Arrays.asList(strings));
        return new Grammar(left,set);
    }

    public static void main(String[] args) {
        Grammar grammar = GrammarUtil.transformStrToGrammar('f', "aa|aa|b|cc|b");
        grammar.getRight().forEach(System.out::println);
    }
}
