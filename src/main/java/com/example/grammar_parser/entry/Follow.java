package com.example.grammar_parser.entry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Follow {
    private Set<Character> followSet = new HashSet<>();
    private Character token;

    public Follow(Character token) {
        this.token = token;
    }

    public Set<Character> getFollowSet() {
        return followSet;
    }

    public void setFollowSet(Set<Character> followSet) {
        this.followSet = followSet;
    }

    public Character getToken() {
        return token;
    }

    public void setToken(Character token) {
        this.token = token;
    }

    public int getSetSize(){
        return followSet.size();
    }
    public boolean addElement(Set<Character> set){
        int oldSize = getSetSize();
        followSet.addAll(set);
        return oldSize!=getSetSize();
    }
    public boolean addElement(Character c){
        int oldSize = getSetSize();
        followSet.add(c);
        return oldSize!=getSetSize();
    }

    @Override
    public String toString() {
        return "Follow{" +
                "token=" + token +
                ", followSet=" + Arrays.toString(followSet.toArray()) +
                '}';
    }
}
