package com.example.grammar_parser.utils;

import com.example.grammar_parser.entry.CFG;
import com.example.grammar_parser.entry.First;
import com.example.grammar_parser.entry.Follow;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FollowUtil {
    public static Follow getFollowByName(Character name, List<Follow> followList){
        for (Follow follow : followList) {
            if (follow.getToken()==name){
                return follow;
            }
        }
        throw new IllegalArgumentException("找不到该名字的follow");
    }
    public static Follow getFollowByName(Character name, CFG cfg){
        return getFollowByName(name,cfg.getFollowList());
    }
    public static Set<Character> getFollowSet(First first){
        return first.getFirstSet().stream().filter(e->e!='@').collect(Collectors.toSet());
    }
    public static Set<Character> getNoNullSet(First first){
        return first.getFirstSet().stream().filter(e->e!='@').collect(Collectors.toSet());
    }
    public static boolean isContainNull(First first){
        return first.getFirstSet().contains('@');
    }
}
