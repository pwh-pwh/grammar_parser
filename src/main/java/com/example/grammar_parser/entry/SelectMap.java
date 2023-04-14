package com.example.grammar_parser.entry;

import com.example.grammar_parser.utils.CharUtil;
import com.example.grammar_parser.utils.FirstUtil;
import com.example.grammar_parser.utils.FollowUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SelectMap {
    private Map<Grammar, Set<Character>> selectMap = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        selectMap.forEach((k,v)->{
            sb.append('{'+k.toString()+'\n'+Arrays.toString(v.toArray())+"}\n");
        });
        return "SelectMap{" +
                "selectMap=" + sb.toString() +
                '}';
    }

    /**
     * 建立select表
     * @param cfg
     */
    public SelectMap(CFG cfg) {
        List<First> firstList = cfg.getFirstList();
        List<Follow> followList = cfg.getFollowList();
        cfg.getProductionList().forEach(e->{
            String next = e.getRight().iterator().next();
            Set<Character> set = new HashSet<>();
            if (CharUtil.isTToken(next.charAt(0))) {
                if(next.charAt(0)!='@'){
                    set.add(next.charAt(0));
                }
                else{
                    Set<Character> followSet = FollowUtil.getFollowByName(e.getLeft(), followList).getFollowSet();
                    set.addAll(followSet);
                }
            }else {
                First firstByName = FirstUtil.getFirstByName(next.charAt(0), firstList);
                Set<Character> firstSet = FollowUtil.getNoNullSet(firstByName);
                set.addAll(firstSet);
                if (FollowUtil.isContainNull(firstByName)) {
                    set.addAll(FollowUtil.getFollowByName(e.getLeft(), followList).getFollowSet());
                }
            }
            selectMap.put(e,set);
        });
    }

    public Grammar getRightGrammar(Character left,Character token){
        AtomicReference<Grammar> g1 = new AtomicReference<>();
        selectMap.forEach((k,v)->{
            if (k.getLeft()==left) {
                if (v.contains(token)){
                    g1.set(k);
                }
            }
        });
        if (g1.get()!=null){
            return g1.get();
        }else{
            return null;
        }
    }

    public String getRight(Character left,Character token){
        AtomicReference<Grammar> g1 = new AtomicReference<>();
        selectMap.forEach((k,v)->{
            if (k.getLeft()==left) {
                if (v.contains(token)){
                    g1.set(k);
                }
            }
        });
        if (g1.get()!=null){
            return g1.get().getRight().iterator().next().toString();
        }else{
            return "null";
        }
    }
}
