package Latte.Visitors;

import Latte.Absyn.Item;
import Latte.Absyn.Stmt;
import Latte.Env;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */



public class StmtVisitor implements Stmt.Visitor<String, LinkedList<Env>>
{
    public String visit(Latte.Absyn.Empty p, LinkedList<Env> envs)
    {
        return "";
    }
    public String visit(Latte.Absyn.BStmt p, LinkedList<Env> envs)
    {
        String asm = p.block_.accept(new BlockVisitor(), envs);
        return asm;
    }
    
    public void checkVariableRedeclaration(String ident, Env env) {
        if (!(env.variableType.containsKey(ident)))
            throw new IllegalArgumentException("Variable " + ident + " is redeclared\n");

    }

    // Deklaracje
    public String visit(Latte.Absyn.Decl p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        envs.getLast().setCurrentType(";" + p.type_.toString());
        p.type_.accept(new TypeVisitor(), envs);
        String asm = "";
        for (Item x : p.listitem_) {
            //checkVariableRedeclaration(x.getIdent(), env);
            envs.getLast().variableType.put(x.getIdent(), p.type_.toString());
            envs.getLast().register = "rax";
            asm += x.accept(new ItemVisitor(), envs);
        }
        return asm;
    }

    public void checkTypeDefinition(String ident, Env env) {

        if (!(env.variableType.containsKey(ident)))
            throw new IllegalArgumentException("Variable " + ident + " has no type assigned\n");
        
    }
    
    
    // Przypisanie
    public String visit(Latte.Absyn.Ass p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        
        checkTypeDefinition(p.ident_, env);
        
        String asm = "";

        
        asm += p.expr_.accept(new ExprVisitor(), envs);
        asm += "\tpop rax\n";

        if (env.variableShifts.containsKey(p.ident_)) {
            asm += "\tmov [rbp-"+env.variableShifts.get(p.ident_)+"], rax\n";
        }
        if (env.argumentsShifts.containsKey(p.ident_)) {
            asm += "\tmov [rbp+"+env.variableShifts.get(p.ident_)+"], rax\n";
        }
        return asm;
    }

    // Inkrementacja
    public String visit(Latte.Absyn.Incr p, LinkedList<Env> envs)
    {
        String asm = "";
        Env env = envs.getLast();
        
        int shift = env.variableShifts.get(p.ident_);
        asm += "\tmov rax, [rbp-"+ shift +"]\n";
        asm += "\tadd rax, 1\n";
        asm += "\tmov [rbp-"+ shift +"], rax\n";
        return asm;
    }
    
    

    // Dekrementacja
    public String visit(Latte.Absyn.Decr p, LinkedList<Env> envs)
    {
        String asm = "";
        Env env = envs.getLast();

        int shift = env.variableShifts.get(p.ident_);
        asm += "\tmov rax, [rbp-"+ shift +"]\n";
        asm += "\tsub rax, 1\n";
        asm += "\tmov [rbp-"+ shift +"], rax\n";
        return asm;
    }

    // Return
    public String visit(Latte.Absyn.Ret p, LinkedList<Env> envs)
    {
        envs.getLast().register = "rax";
        String asm = p.expr_.accept(new ExprVisitor(), envs);
        asm += "\tpop rax\n";
        asm += "\tleave\n\tret\n";
        return asm;
    }

    // Void Return
    public String visit(Latte.Absyn.VRet p, LinkedList<Env> envs)
    {
        //String asm = "\tmov rax, 0\n";
        String asm = "\tleave\n\tret\n";
        return asm;
    }

    // Warunek
    public String visit(Latte.Absyn.Cond p, LinkedList<Env> envs)
    {

        Env env = envs.getLast();
        
        envs.getLast().register = "rax"; // che dostac wynik obliczenia wyr do eax
        int ifNo = envs.getLast().ifCounter++;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        String ifLabel = "\nIF_"+env.funName+"_"+ifNo;
        asm += ifLabel+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje AFTER_IF_"+env.funName+"_"+ifNo+"\n\n";
        envs.getLast().register = "rax";
        asm += p.stmt_.accept(new StmtVisitor(), envs);
        asm += "AFTER_IF_" + env.funName+"_"+ifNo + ":\n";
        return asm;
    }

    // Warunek z elsem
    public String visit(Latte.Absyn.CondElse p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        
        envs.getLast().register = "rax"; // che dostac wynik obliczenia wyr do eax
        int ifNo = envs.getLast().ifCounter++;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        String ifLabel = "\nIF_"+env.funName+"_"+ifNo;
        asm += ifLabel+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje  ELSE_"+env.funName+"_"+ ifNo +"\n\n";
        envs.getLast().register = "rax";
        asm += p.stmt_1.accept(new StmtVisitor(), envs);
        asm += "\tjmp AFTER_IF_"+env.funName+"_"+ ifNo +"\n\n";
        asm += "ELSE_"+env.funName+"_" + ifNo + ":\n";
        envs.getLast().register = "rax";
        asm += p.stmt_2.accept(new StmtVisitor(), envs);
        asm += "AFTER_IF_"+env.funName+"_" + ifNo + ":\n";

        return asm;
    }

    // Petla
    public String visit(Latte.Absyn.While p, LinkedList<Env> envs)
    {   
        Env env = envs.getLast();
        
        int whileNo = envs.getLast().whileCounter++;
        String whileLabel = "WHILE_"+env.funName+"_"+whileNo;
        String afterWhileLabel = "AFTER_WHILE_"+env.funName+"_"+whileNo;

        String asm = whileLabel + ":\n";
        asm += p.expr_.accept(new ExprVisitor(), envs);
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje " + afterWhileLabel+"\n\n";
        asm += p.stmt_.accept(new StmtVisitor(), envs);
        asm += "\tjmp "+whileLabel+"\n\n";
        asm += afterWhileLabel+ ":\n\n";

        return asm;
    }

    // Instrukcja dla wyrazenia
    public String visit(Latte.Absyn.SExp p, LinkedList<Env> envs)
    {

        String asm = p.expr_.accept(new ExprVisitor(), envs);

        return asm;
    }

}


