package Latte;

import Latte.Absyn.*;
import Latte.Absyn.Void;
import Latte.Exceptions.TypeException;

import java.util.*;

/**
 * Created by konrad on 01/02/15.
 */
public class Env {
    private List<String> predefinedFunctions =
            Arrays.asList("readInt", "readString", "printInt", "printString", "concatenateString");
    private FnDef currentFunctionBlockGenerator;
    private Stack<Map<String, Integer>> functionParams;
    private Stack<Map<String, Type>> envVar;
    private Stack<Map<String, Fun>> envFun;
    private Map<String, String> stringsMap;
    private String currentFunctionIdent;
    private Fun currentFunction;
    private static int stringsCounter = 0;
    private int createdLocals;

    public String getNextLabel() {
        return "LABEL_"+ labelNumber++;
    }

    private int labelNumber = 1;

    private boolean plusConcat;

    public Env() {
        this.envVar = new Stack<Map<String, Type>>();
        this.envFun = new Stack<Map<String, Fun>>();
        this.stringsMap = new HashMap<String, String>();
        this.functionParams = new Stack<Map<String, Integer>>();
        addPredefinedFunctions();
    }



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

    public static <K, V>  Map<K, V> deepCopy(Map<K,V> map) {
        HashMap<K,V> newMap = new HashMap<K, V>();
        for(K key: map.keySet()) {
            newMap.put(key, map.get(key));
        }
        return newMap;
    }

    public void addString(String str) {
        if (stringsMap.containsKey(str)) return;
        stringsMap.put(str, "LC"+ stringsCounter++);
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

    public int getVarStack(String ident) throws TypeException {
        for (ListIterator<Map<String, Integer>> iterator = functionParams
                .listIterator(functionParams.size()); iterator.hasPrevious();) {
            Integer value = iterator.previous().get(ident);
            if (value != null)
                return value;
        }
        throw new TypeException("Brak zmiennej o identyfikatorze "+ident);
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

    public void beginFunctionASM(FnDef function) {
        currentFunctionBlockGenerator = function;
        createdLocals = 0;
        beginBlockASM();

    }

    public void endFunctionASM() {
        currentFunctionBlockGenerator = null;
        endBlockASM();
    }

    public void beginBlockASM() {
        functionParams.push(new HashMap<String, Integer>());
    }

    public void addFunctionParams(String ident, int shift) {
        functionParams.peek().put(ident, shift);
    }

    public void endBlockASM() {
        functionParams.pop();
    }
//
//    public String getFunctionParamShift(String ident_) {
//    }

    public String getCurrentFunctionIdent() {
        return currentFunctionIdent;
    }

    public Map<String, String> getStringsMap() {
        return stringsMap;
    }


    public boolean isPlusConcat() {
        return plusConcat;
    }

    public void setPlusConcat(boolean plusConcat) {
        this.plusConcat = plusConcat;
    }

    public void determinePlusOperation(Type expressionType) {
        setPlusConcat(false);
        if (expressionType.equals(new Str())) {
            setPlusConcat(true);
        }
    }

    public boolean isPredefinedFunction(String ident) {
        return predefinedFunctions.contains(ident);
    }

    public int getLocalVarNumber() {
        int result = -4*(currentFunctionBlockGenerator.localVars - createdLocals);
        createdLocals++;
        return result;
    }

    public void addVariableStack(String ident, int varNumber) {
        functionParams.peek().put(ident, varNumber);
    }
}
