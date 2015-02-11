package Latte;

import java.util.*;

/**
 * Created by konrad on 01/02/15. 
 */
public class Env {
    public List<String> predefinedFunctions = 
            Arrays.asList("readInt", "readString", "printInt", "printString", "concatenateString");
    public Map<String, String> variableType = new HashMap<String, String>();
    public Map<String, Integer> varDeclarationEnv = new HashMap<String, Integer>();
    public Map<String, Integer> variableShifts = new HashMap<String, Integer>(); // Name, Shift
    public Map<String, Integer> argumentsShifts = new HashMap<String, Integer>(); // Name, Shift
    public int rbp = 4;
    public int ifCounter = 1;
    public int neg;
    public int whileCounter = 1;
    public int jmpExpCounter = 1;
    public int andExpCounter = 1;
    public int orExpCounter = 1;
    public int localVarShift = 8;
    public int ileArgumentow = 0;
    public static int ileZmiennych = 0;
    public String funName = "";
    public boolean addIsString = false;
    
    public static Map<String, String> strings = new HashMap<String, String>();
    public static Map<String, Boolean> functionsReturnAchievibility = new HashMap<String, Boolean>();
    public static Map<String, String> functionsReturnType = new HashMap<String, String>();
    public static Map<String, Integer> functionsArgumentsNumber = new HashMap<String, Integer>();

    public static Env copyEnv(Env last, String funName) {
        Env env = new Env(funName);
        env.ileArgumentow = last.ileArgumentow;
        env.jmpExpCounter = last.jmpExpCounter;
        env.andExpCounter = last.andExpCounter;
        env.whileCounter = last.whileCounter;
        env.orExpCounter = last.orExpCounter;
        env.localVarShift = last.localVarShift;
        env.argumentsShifts = last.argumentsShifts;
        env.addIsString = last.addIsString;
        
        env.strings = deepCopy(last.strings);
        env.variableShifts = deepCopy(last.variableShifts);
        env.argumentsShifts = deepCopy(last.argumentsShifts);
        env.variableType = deepCopy(last.variableType);
        env.varDeclarationEnv = deepCopy(last.varDeclarationEnv);
        return env;
    }

    public Env(String ident_) {
        this.funName = ident_;
    }
    
    public static <K, V>  Map<K, V> deepCopy(Map<K,V> map) {
        HashMap<K,V> newMap = new HashMap<K, V>();
        for(K key: map.keySet()) {
            newMap.put(key, map.get(key));
        }
        return newMap;
    }
}
