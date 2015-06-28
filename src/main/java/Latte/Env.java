package Latte;

import Latte.Absyn.*;
import Latte.Absyn.Void;
import Latte.Exceptions.TypeException;

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


    public String getCurrentFunctionIdent() {
        return currentFunctionIdent;
    }

    private String currentFunctionIdent;


    public Env() {
        this.envVar = new Stack<Map<String, Type>>();
        this.envFun = new Stack<Map<String, Fun>>();
        this.stringsMap = new HashMap<String, String>();
        addPredefinedFunctions();
    }



    private Stack<Map<String, Type>> envVar;

    private Stack<Map<String, Fun>> envFun;
    private Fun currentFunction;

    private Map<String, String> stringsMap;

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


    public static int stringsCounter = 0;




    private void addPredefinedFunctions() {
        envFun.push(new HashMap<String, Fun>());

        ListType argumentsInt= new ListType();
        argumentsInt.add(new Int());
        ListType argumentsPrintString = new ListType();
        argumentsPrintString.add(new Str());

        ListType argumentsConcatString = new ListType();
        argumentsConcatString.add(new Str());
        argumentsConcatString.add(new Str());

        this.envFun.peek().put("printInt", new Fun(new Void(), argumentsInt));
        this.envFun.peek().put("printString", new Fun(new Void(), argumentsPrintString));
        this.envFun.peek().put("readInt", new Fun(new Int(), new ListType()));
        this.envFun.peek().put("readString", new Fun(new Str(), new ListType()));
        this.envFun.peek().put("concatenateString", new Fun(new Str(), argumentsConcatString));

    }

    public void beginBlock() {
        envVar.push(new HashMap<String, Type>());
    }

    public void endBlock() {
        envVar.pop();
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

    public void addString(String str) {
        if (stringsMap.containsKey(str)) return;
        stringsMap.put(str, ".LC"+ stringsCounter++);
    }

    public String getStringAddress(String str) {
        return stringsMap.get(str);
    }

    public void addFunction(FnDef function) {
        ListType argumentList = new ListType();
        for (Arg arg: function.listarg_) {
            argumentList.add(arg.type_);
        }
        addFun(function.ident_, new Fun(function.type_, argumentList));

    }

    private void addFun(String ident, Fun function) {
        if (envFun.peek().containsKey(ident))
            return;
        envFun.peek().put(ident, function);
    }

    public void beginFunction(String functionIdent) throws TypeException {
        currentFunction = getFunctionById(functionIdent);
        currentFunctionIdent = functionIdent;
        beginBlock();
    }

    public Fun getFunctionById(String functionIdent) throws TypeException {
        Iterator<Map<String, Fun>> iter = envFun.iterator();

        while (iter.hasNext()){
            Fun fun = iter.next().get(functionIdent);
            if (!(fun == null)) {
                return fun;
            }
        }
        throw new TypeException("No function of identifier: "+functionIdent);
    }


    public Type getVariableType(String ident) throws TypeException {
        for (ListIterator<Map<String, Type>> iterator = envVar.listIterator(envVar.size());
             iterator.hasPrevious();) {
            Type type = iterator.previous().get(ident);
            if (type != null)
                return type;
        }
        throw new TypeException(currentFunctionIdent, "No variable of identifier " + ident);
    }

    public void endFunction() {
    }

    public void addVariable(String ident, Type type) throws TypeException {
        if (envVar.peek().containsKey(ident)) {
            throw new TypeException(currentFunctionIdent, "At variable " + ident + " declaration: variable " + ident + " is redeclared.");
        }
        if (type.equals(new Void())) {
            throw new TypeException("Nie można zadeklarować zmiennej typu void");
        } else if (type.equals(new Void())) {
            throw new TypeException("Nie można zadeklarować tablicy zawierającej zmienne typu void");
        }
        envVar.peek().put(ident, type);
    }

    public boolean typeEquality(Type leftHandType, Type rightHandType) {
        return leftHandType.equals(rightHandType);
    }

    public Fun getCurrentFunction() {
        return null;
    }

    public Type getCurrentFunctionType() {
        return currentFunction.type_;
    }
}
