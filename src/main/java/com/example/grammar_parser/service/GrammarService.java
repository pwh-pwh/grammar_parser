package com.example.grammar_parser.service;

import com.example.grammar_parser.entry.CFG;
import com.example.grammar_parser.entry.Grammar;
import com.example.grammar_parser.entry.SelectMap;
import com.example.grammar_parser.utils.CFGUtil;
import com.example.grammar_parser.utils.GrammarUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarService {
    private CFG cfg;
    private Map<String,Object> resultMap;

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }


    public CFG getCfg() {
        return cfg;
    }

    public void setCfg(CFG cfg) {
        this.cfg = cfg;
    }


    public CFG startParser(List<String> grammarTextList,String text) {
        resultMap = new HashMap<>();
        cfg = new CFG(grammarTextList.get(0).charAt(0));
        grammarTextList.forEach(e->{
            String[] split = e.split("->");
            Grammar grammar = GrammarUtil.transformStrToGrammar(split[0].charAt(0), split[1]);
            cfg.addProduction(grammar);
        });
        CFGUtil.simplifiedGrammar(cfg);
        resultMap.put("simResult",cfg.toString());
        CFGUtil.removeLeftCommonFactor(cfg);
        resultMap.put("rlfResult",cfg.toString());
        CFGUtil.removeLeftRecursion(cfg);
        resultMap.put("lrResult",cfg.toString());
        CFGUtil.generateFirstList(cfg);
        resultMap.put("firstResult",cfg.getFirstList());
        CFGUtil.generateFollowList(cfg);
        resultMap.put("followResult",cfg.getFollowList());
        CFGUtil.separateSameLeft(cfg);
        SelectMap selectMap = new SelectMap(cfg);
        resultMap.put("selectMapResult",selectMap);
        if(text!=null&&text.length()>0){
            List<Grammar> grammarList = CFGUtil.analyzeSentences(cfg, selectMap, text);
            resultMap.put("analyzeResult",grammarList);
        }
        return cfg;
    }
}
