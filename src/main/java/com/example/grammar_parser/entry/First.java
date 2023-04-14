package com.example.grammar_parser.entry;

import java.util.*;

public class First {
    private Set<Character> firstSet = new HashSet<>();
    private Character token;

    public void setFirstSet(Set<Character> firstSet) {
        this.firstSet = firstSet;
    }

    public Character getToken() {
        return token;
    }

    public void setToken(Character token) {
        this.token = token;
    }

    public First(Character token) {
        this.token = token;
    }
    public Set<Character> getFirstSet(){
        return firstSet;
    }
    public int getSetSize(){
        return firstSet.size();
    }
    public boolean addElement(Set<Character> set){
        int oldSize = getSetSize();
        firstSet.addAll(set);
        return oldSize!=getSetSize();
    }
    public boolean addElement(Character c){
        int oldSize = getSetSize();
        firstSet.add(c);
        return oldSize!=getSetSize();
    }
    @Override
    public String toString() {
        return "First{" +
                "token=" + token +
                ", firstSet=" + Arrays.toString(firstSet.toArray()) +
                '}';
    }
}
