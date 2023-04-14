package com.example.grammar_parser.entry;

import com.example.grammar_parser.utils.CharUtil;
import com.example.grammar_parser.utils.GrammarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CFG {
    private List<Character> noTerminalList = new ArrayList<>();
    private List<Character> terminalList = new ArrayList<>();
    private List<Grammar> productionList = new ArrayList<>();
    private Character startSymbol;
    private List<First> firstList;
    private List<Follow> followList;


    public List<First> getFirstList() {
        return firstList;
    }

    public void setFirstList(List<First> firstList) {
        this.firstList = firstList;
    }

    public List<Follow> getFollowList() {
        return followList;
    }

    public void setFollowList(List<Follow> followList) {
        this.followList = followList;
    }

    public List<Character> getNoTerminalList() {
        return noTerminalList;
    }

    public void setNoTerminalList(List<Character> noTerminalList) {
        this.noTerminalList = noTerminalList;
    }

    public List<Character> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<Character> terminalList) {
        this.terminalList = terminalList;
    }

    public List<Grammar> getProductionList() {
        return productionList;
    }

    public void setProductionList(List<Grammar> productionList) {
        this.productionList = productionList;
    }

    public Character getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(Character startSymbol) {
        this.startSymbol = startSymbol;
    }

    public CFG(Character startSymbol) {
        this.startSymbol = startSymbol;
    }
    public void addProduction(Grammar grammar){
        productionList.add(grammar);
        addNoTerminal(grammar.getLeft());
        Set<String> set = grammar.getRight();
        set.forEach(str->{
            char[] chars = str.toCharArray();
            for (char aChar : chars) {
                addTerminal(aChar);
                addNoTerminal(aChar);
            }
        });

    }
    public void addNoTerminal(Character character){
        if (!noTerminalList.contains(character)&& CharUtil.isNoTToken(character)) {
            noTerminalList.add(character);
        }
    }
    public void addTerminal(Character character){
        if (!terminalList.contains(character)&&CharUtil.isTToken(character)) {
            terminalList.add(character);
        }
    }

    @Override
    public String toString() {
        return "CFG{\n" +
                "noTerminalList=" + Arrays.toString(noTerminalList.toArray()) +
                ", \nterminalList=" + Arrays.toString(terminalList.toArray()) +
                ", \nproductionList=" +Arrays.toString(productionList.toArray()) +
                ", \nstartSymbol=" + startSymbol +
                "\n}";
    }
}
