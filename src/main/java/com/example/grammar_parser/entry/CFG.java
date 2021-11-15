package com.example.grammar_parser.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CFG {
    private List<Character> noTerminalList = new ArrayList<>();
    private List<Character> terminalList = new ArrayList<>();
    private List<Grammar> productionList = new ArrayList<>();
    private Character startSymbol;

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
    public boolean isUppercase(char c){
        return c>='a'&&c<='z';
    }
    public boolean isLowercase(char c){
        return c>='A'&&c<='Z';
    }
    public void addNoTerminal(Character character){
        if (!noTerminalList.contains(character)&&isUppercase(character)) {
            noTerminalList.add(character);
        }
    }
    public void addTerminal(Character character){
        if (!terminalList.contains(character)&&isLowercase(character)) {
            terminalList.add(character);
        }
    }


}
