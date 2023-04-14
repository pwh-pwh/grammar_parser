package com.example.grammar_parser.utils;

import com.example.grammar_parser.entry.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CFGUtil {
    public static void main(String[] args) {
/*        Grammar g1 = GrammarUtil.transformStrToGrammar('S', "Be");
        Grammar g2 = GrammarUtil.transformStrToGrammar('B', "Ce");
        Grammar g3 = GrammarUtil.transformStrToGrammar('B',"Af");
        Grammar g4 = GrammarUtil.transformStrToGrammar('A', "Ae");
        Grammar g5 = GrammarUtil.transformStrToGrammar('A', "e");
        Grammar g6 = GrammarUtil.transformStrToGrammar('C', "Cf");
        Grammar g7 = GrammarUtil.transformStrToGrammar('D', "f");*/
/*        Grammar g0 = GrammarUtil.transformStrToGrammar('S', "Qc|c");
        Grammar g1 = GrammarUtil.transformStrToGrammar('Q', "Rb|b");
        Grammar g2 = GrammarUtil.transformStrToGrammar('R', "Sa|a");*/
        Grammar g1 = GrammarUtil.transformStrToGrammar('S', "A|c");
        Grammar g2 = GrammarUtil.transformStrToGrammar('A', "a|bS|iB");
        Grammar g3 = GrammarUtil.transformStrToGrammar('B', "em|wo");
/*        Grammar g3 = GrammarUtil.transformStrToGrammar('B', "cC|@");
        Grammar g4 = GrammarUtil.transformStrToGrammar('C', "ef|gh");*/
//        Grammar g8 = GrammarUtil.transformStrToGrammar('B',"fd|fm|i");
        CFG cfg = new CFG('S');
        cfg.addProduction(g1);
        cfg.addProduction(g2);
        cfg.addProduction(g3);
        List<First> firsts = CFGUtil.generateFirstList(cfg);
//        firsts.forEach(System.out::println);
        System.out.println();
        List<Follow> followList = CFGUtil.generateFollowList(cfg);
//        followList.forEach(System.out::println);
        System.out.println();
        CFGUtil.separateSameLeft(cfg);
        SelectMap selectMap = new SelectMap(cfg);
        System.out.println(selectMap);
        List<Grammar> grammarList = CFGUtil.analyzeSentences(cfg, selectMap, "iem");
        grammarList.forEach(System.out::println);
/*        System.out.println(selectMap);
        System.out.println(selectMap.getRight('S', 'a'));*/
//        System.out.println(cfg.toString());

    }

    /**
     * 分析句子
     * @param cfg
     * @param selectMap
     * @param s
     * @return
     */
    public static List<Grammar> analyzeSentences(CFG cfg,SelectMap selectMap,String s){
        List<Grammar> grammarList = new ArrayList<>();
        char[] charArray = s.toCharArray();
        AtomicInteger index = new AtomicInteger(0);
        AtomicBoolean isFirst = new AtomicBoolean(true);
        AtomicReference<Character> startC = new AtomicReference<>();
        int length = charArray.length-1;
        while(true){
            if(isFirst.get()){
                String right = selectMap.getRight(cfg.getStartSymbol(), charArray[index.get()]);
                Grammar rightGrammar = selectMap.getRightGrammar(cfg.getStartSymbol(), charArray[index.get()]);
                if (right.equals("null")){
                    throw new IllegalArgumentException("文法无法解析");
                }
                grammarList.add(rightGrammar);
                if (CharUtil.isTToken(right.charAt(0))){
                    index.getAndIncrement();
                    startC.set(right.charAt(1));
                }else {
                    startC.set(right.charAt(0));
                }
                isFirst.set(false);
                continue;
            }
            if(index.get()==length){
                break;
            }
            String right = selectMap.getRight(startC.get(), charArray[index.get()]);

            Grammar rightGrammar = selectMap.getRightGrammar(startC.get(), charArray[index.get()]);
            if (right.equals("null")){
                throw new IllegalArgumentException("文法无法解析");
            }
            if (CharUtil.isTToken(right.charAt(0))){
                index.getAndIncrement();
                grammarList.add(rightGrammar);
                startC.set(right.charAt(1));
            }else {
                startC.set(right.charAt(0));
            }
        }


        return grammarList;
    }


    /**
     * 计算first集
     * @param cfg
     * @return
     */
    public static List<First> generateFirstList(CFG cfg){
        List<First> firstList = new ArrayList<>();
        cfg.getNoTerminalList().forEach(e->{
            firstList.add(new First(e));
        });
        while(true){
            AtomicInteger t1 = new AtomicInteger();
            int t2 = 0;
            cfg.getProductionList().forEach(e->{
                First target = null;
                for (First first : firstList) {
                    if (first.getToken()==e.getLeft()) {
                        target = first;
                    }
                }

                for (String s : e.getRight()) {
                    char c = s.charAt(0);
                    if (CharUtil.isTToken(c)) {
                        if (target.addElement(c)){
                            t1.getAndIncrement();
                        }
                    }else{
                        First firstByName = FirstUtil.getFirstByName(s.charAt(0), firstList);
                        if (target.addElement(firstByName.getFirstSet())){
                            t1.getAndIncrement();
                        }
                    }
                }

            });
            if (t1.get() == t2){
                break;
            }
        }

        cfg.setFirstList(firstList);
        return firstList;
    }

    /**
     * 计算follow集
     * @param cfg
     * @param firstList
     * @return
     */
    public static List<Follow> generateFollowList(CFG cfg,List<First> firstList){
        List<Follow> followList = new ArrayList<>();
        cfg.getNoTerminalList().forEach(e->{
            if (e==cfg.getStartSymbol()){
                Follow follow = new Follow(e);
                follow.addElement('$');
                followList.add(follow);
            }else{
                followList.add(new Follow(e));
            }
        });
        while(true){
            AtomicInteger t1 = new AtomicInteger(0);
            int t2 = 0;
            cfg.getProductionList().forEach(e->{
                e.getRight().forEach(it->{
                    for (int i = 0; i < it.length(); i++) {
                        if (CharUtil.isNoTToken(it.charAt(i))) {
                            Follow f1 = FollowUtil.getFollowByName(it.charAt(i),followList);
                            if(i!=it.length()-1){
                                if (CharUtil.isTToken(it.charAt(i+1))){
                                    if (f1.addElement(it.charAt(i + 1))){
                                        t1.getAndIncrement();
                                    }
                                }else{
                                    for (int j = i+1; j < it.length(); j++) {
                                        if(CharUtil.isTToken(it.charAt(j))){
                                            break;
                                        }
                                        First first = FirstUtil.getFirstByName(it.charAt(j),firstList);
                                        if (f1.addElement(FollowUtil.getFollowSet(first))){
                                            t1.getAndIncrement();
                                        }
                                        if(FollowUtil.isContainNull(first)){
                                            if(f1.addElement(FollowUtil.getFollowByName(e.getLeft(), followList).getFollowSet())){
                                                t1.getAndIncrement();
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                }
                            }else{

                                Follow f2 = FollowUtil.getFollowByName(e.getLeft(), followList);
                                if (f1.addElement(f2.getFollowSet())){
                                    t1.getAndIncrement();
                                }
                            }
                        }
                    }
                });
            });
            if(t1.get()==t2){
                break;
            }
        }
        cfg.setFollowList(followList);
        return followList;
    }

    public static List<Follow> generateFollowList(CFG cfg){
        return generateFollowList(cfg,cfg.getFirstList());
    }


    /**
     * 消除左递归
     * @param cfg
     */
    public static void removeLeftRecursion(CFG cfg){
        List<Character> noTerminalList = cfg.getNoTerminalList();
        noTerminalList.forEach(System.out::println);
        int size = noTerminalList.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                System.out.println("i:"+noTerminalList.get(i)+",j:"+noTerminalList.get(j));
                iterativeReplacement(noTerminalList.get(i),noTerminalList.get(j),cfg);
            }
            doRemoveLeftRecursion(noTerminalList.get(i),cfg);
        }
        removeRedundantRule(cfg);
    }

    /**
     * 消除指定非终结符的左递归
     * @param c
     * @param cfg
     */
    public static void doRemoveLeftRecursion(Character c,CFG cfg){
        Grammar g1 = null;
        List<Grammar> productionList = cfg.getProductionList();
        for (Grammar grammar : productionList) {
            if (grammar.getLeft()==c){
                g1 = grammar;
            }
        }
        AtomicBoolean flag = new AtomicBoolean(false);
        Set<String> right = g1.getRight();
        right.forEach(e->{
            if (e.startsWith(c.toString())){
                flag.set(true);
            }
        });
        if(!flag.get()){
            return;
        }
        Set<String> set4n = new HashSet<>();
        Set<String> set4o = new HashSet<>();
        char ntChar = CharUtil.generatorNTChar();
        right.forEach(e->{
            if (e.startsWith(c.toString())){
                set4n.add(e.substring(1)+ntChar);
            }else{
                set4o.add(e+ntChar);
            }
        });
        set4n.add("@");
//        cfg.addTerminal('@');
        Grammar gn = new Grammar(ntChar, set4n);
        g1.setRight(set4o);
        cfg.addProduction(gn);
//        productionList.add(gn);
    }

    /**
     * 替换规则
     * @param c1
     * @param c2
     * @param cfg
     */
    public static void iterativeReplacement(Character c1,Character c2,CFG cfg){
        List<Grammar> productionList = cfg.getProductionList();
        Grammar g1 = null;
        Grammar g2 = null;
        for (Grammar grammar : productionList) {
            if (grammar.getLeft()==c1){
                g1 = grammar;
            }else if(grammar.getLeft()==c2){
                g2 = grammar;
            }
        }
        Set<String> g2Right = g2.getRight();
        Set<String> newSet = new HashSet<>();
        g1.getRight().forEach(e->{
            if(e.startsWith(c2.toString())){
                String substring = e.substring(1);
                g2Right.forEach(it->{
                    newSet.add(it+substring);
                });
            }
            else{
                newSet.add(e);
            }
        });
        g1.setRight(newSet);
    }

    /**
     * 合并相同左部
     * @param cfg
     */
    public static void mergeSameLeft(CFG cfg){
        List<Grammar> productionList = cfg.getProductionList();
        Map<Character,Integer> map = new HashMap<>();
        productionList.forEach(e->{
            Integer count = map.getOrDefault(e.getLeft(), 0);
            map.put(e.getLeft(),++count);
        });
        map.forEach((k,v)->{
            if(v>=2){
                Set<String> set = new HashSet<>();
                productionList.forEach(it->{
                    if(it.getLeft()==k){
                        set.addAll(it.getRight());
                    }
                });
                productionList.removeIf(it->{
                    return it.getLeft()==k;
                });
                productionList.add(new Grammar(k,set));
            }
        });
    }

    /**
     * 分离相同的左部
     * @param cfg
     */
    public static void separateSameLeft(CFG cfg){
        List<Grammar> newList = new ArrayList<>();
        cfg.getProductionList().forEach(e->{
            if(e.getRight().size()>1){
                e.getRight().forEach(it->{
                    Set<String> set = new HashSet<>();
                    set.add(it);
                    Grammar grammar = new Grammar(e.getLeft(), set);
                    newList.add(grammar);
                });
            }else{
                newList.add(e);
            }
        });
        cfg.setProductionList(newList);
    }

    /**
     * 消除左公因子
     *
     * @param cfg
     */
    public static void removeLeftCommonFactor(CFG cfg) {
        List<Grammar> newGList = new ArrayList<>();
        List<Grammar> productionList = cfg.getProductionList();
        productionList.forEach(e -> {
            Map<Character, Integer> map = new HashMap<>();
            Set<String> right = e.getRight();
            right.forEach(it -> {
                char c = it.charAt(0);
                Integer n = map.getOrDefault(c, 0);
                map.put(c, ++n);
            });
            map.forEach((k, v) -> {
                if (v > 1) {
                    char c = CharUtil.generatorNTChar();
                    cfg.getNoTerminalList().add(c);
                    StringBuilder sb = new StringBuilder();
                    Map<String, String> map4r = new HashMap<>();
                    right.forEach(it -> {
                        if (it.contains(k.toString())) {
                            map4r.put(it, it.substring(1) + c);
                            if (it.length() == 1) {
                                sb.append("@|");
                            } else {
                                sb.append(it.substring(1) + "|");
                            }
                        }
                    });
                    map4r.forEach((key, val) -> {
                        right.removeIf(item -> item.equals(key));
                        right.add(val);
                    });
                    newGList.add(GrammarUtil.transformStrToGrammar(c, sb.toString()));
                }
            });
        });
        productionList.addAll(newGList);
    }

    /**
     * 化简文法
     *
     * @param cfg
     */
    public static void simplifiedGrammar(CFG cfg) {
        removeHarmfulRule(cfg);
        removeRedundantRule(cfg);
    }

    /**
     * 去除有害规则
     *
     * @param cfg
     */
    public static void removeHarmfulRule(CFG cfg) {
        List<Grammar> productionList = cfg.getProductionList();
        //去除形如U->U|e的产生式的一部分
        productionList.forEach(e -> {
            Character left = e.getLeft();
            e.getRight().removeIf(it -> {
                return it.equals(left + "");
            });
        });
        productionList.removeIf(e -> {
            if (e.getRight().size() == 0) {
                return true;
            }
            return false;
        });
        cfg.getNoTerminalList().removeIf(e -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            productionList.forEach(it -> {
                if (it.getLeft() == e) {
                    flag.set(true);
                }
            });
            return !flag.get();
        });
    }

    /**
     * 去除多余规则
     *
     * @param cfg
     */
    public static void removeRedundantRule(CFG cfg) {
        List<Grammar> productionList = cfg.getProductionList();
        List<Character> noTerminalList = cfg.getNoTerminalList();
        //去除不可到达终结符
        noTerminalList.forEach(e -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            if (e != cfg.getStartSymbol()) {
                productionList.forEach(it -> {
                    it.getRight().forEach(s -> {
                        if (s.contains(e.toString())) {
                            flag.set(true);
                        }
                    });

                });
                if (!flag.get()) {
//                    System.out.println("e:"+e);
                    productionList.removeIf(it -> {
                        return it.getLeft() == e;
                    });
                }
            }
        });
        tidyNoT(cfg);
        //去除不可到达
        Map<Character, Boolean> map = new HashMap<>();   //false为不可到达
        List<Character> noAbleList = new ArrayList<>();
        cfg.getNoTerminalList().forEach(e -> {
            map.put(e, false);
        });
        //去除明显的不可到达
        noTerminalList.forEach(e -> {
            productionList.forEach(it -> {
                if (it.getLeft() == e) {
                    Set<String> right = it.getRight();
                    if (right.size() == 1) {
                        String next = right.iterator().next();
                        if (GrammarUtil.countNTNum(next) == 1) {
                            if (GrammarUtil.countTNum(next) + 1 == next.length() && next.contains(e + "")) {

                            } else {
                                map.replace(e, true);
                            }
                        } else {
                            map.replace(e, true);
                        }
                    } else {
                        map.replace(e, true);
                    }
                }
            });
        });
        Iterator<Map.Entry<Character, Boolean>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, Boolean> next = iterator.next();
            if (!next.getValue()) {
                noAbleList.add(next.getKey());
            }
        }
        noAbleList.forEach(e -> {
            //去除上面过滤的
            productionList.removeIf(it -> {
                return it.getLeft() == e;
            });

            productionList.forEach(it -> {
                Set<String> right = it.getRight();
                right.removeIf(item -> {
                    return item.contains(e + "");
                });
            });

            productionList.removeIf(it -> {
                return it.getRight().size() == 0;
            });

        });
        tidyNoT(cfg);
    }

    public static void tidyNoT(CFG cfg) {
        cfg.getNoTerminalList().removeIf(e -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            cfg.getProductionList().forEach(it -> {
                if (it.getLeft() == e) {
                    flag.set(true);
                }
            });
            return !flag.get();
        });
    }
}
