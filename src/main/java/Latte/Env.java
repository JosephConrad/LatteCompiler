package Latte;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konrad on 01/02/15.
 * Default contructor initate completely new environment
 */
public class Env {
    List<String> predefinedFunctions = Arrays.asList("readInt", "readString", "error", "printInt", "printString", "concatenateString");
    private Map<String, String> variableType = new HashMap<String, String>();
    private Map<String, Integer> variableValues  = new HashMap<String, Integer>();
    private Map<String, String> variablesDefault = new HashMap<String, String>();
    private String currentType;
    public int rbp = 4;
    public int ifCounter = 1;
    public String register = "";
    public int neg;
    public int whileCounter = 1;
    public Map<String, String> strings = new HashMap<String, String>();
    public int jmpExpCounter = 1;
    public boolean addIsString = false;
    public int andExpCounter = 1;
    public int orExpCounter = 1;


    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public void addVariable(String name, String value, int type) {
        variableType.put(name, value);

        variableValues.put(name, type);
    }
    
    public Integer getRbpPosition(String str) {
        return variableValues.get(str);
    }
   



    @Override
    public String toString() {
        return "Env{" +
                "predefinedFunctions=" + predefinedFunctions +
                ", variableType=" + variableType +
                ", variableValues=" + variableValues +
                ", variablesDefault=" + variablesDefault +
                '}';
    }

    public void addVariable(String name, Integer type) {
        variableType.put(name, "null");
        variableValues.put(name, type);
    }

}
