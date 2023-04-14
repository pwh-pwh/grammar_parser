package com.example.grammar_parser.utils;

public class CharUtil {
    public static int id = 192;
    public static boolean isTToken(char c){
        return !isNoTToken(c);
    }
    public static boolean isNoTToken(char c){
        return (c>='A'&&c<='Z')||(c>=192&&c<=255);
    }
    public static char generatorNTChar(){
        return (char) id++;
    }

    public static void main(String[] args) {
        System.out.println(CharUtil.isTToken('@'));
        System.out.println((int)'@');
    }
}
