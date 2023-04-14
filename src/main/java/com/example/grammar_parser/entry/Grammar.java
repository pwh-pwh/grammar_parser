package com.example.grammar_parser.entry;

import java.util.Arrays;
import java.util.Set;

/**
 * 文法
 */
public class Grammar {
    private Character left;
    private Set<String> right;



    public Grammar(Character left, Set<String> right) {
        this.left = left;
        this.right = right;
    }

    public Character getLeft() {
        return left;
    }

    public void setLeft(Character left) {
        this.left = left;
    }

    public Set<String> getRight() {
        return right;
    }

    public void setRight(Set<String> right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "left=" + left +
                ", right=" + Arrays.toString(right.toArray()) +
                '}';
    }
}
