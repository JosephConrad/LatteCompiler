package Latte.BackendASM;

import Latte.Absyn.Item;
import Latte.Absyn.Stmt;
import Latte.Env;
import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */

public class StmtVisitor implements Stmt.Visitor<String, LinkedList<Env>>
{
    public String visit(Latte.Absyn.Empty p, LinkedList<Env> envs) {
        return "";
    }
    
    public String visit(Latte.Absyn.BStmt p, LinkedList<Env> envs) {
        return p.block_.accept(new BlockVisitor(), envs);
    }
    
    /*
     *  Declaration
     */
    public String visit(Latte.Absyn.Decl p, LinkedList<Env> envs) { 
        
        p.type_.accept(new TypeVisitor(), envs);
        
        String asm = "";
        for (Item x : p.listitem_) {
            envs.getLast().variableType.put(x.getIdent(), p.type_.toString());
            asm += x.accept(new ItemVisitor(), envs);
        }
        return asm;
    } 
    
    /*
     *  Assignment
     */
    public String visit(Latte.Absyn.Ass p, LinkedList<Env> envs) {
        Env env = envs.getLast(); 

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

    /*
     *  Increment
     */
    public String visit(Latte.Absyn.Incr p, LinkedList<Env> envs) {
        Env env = envs.getLast();
        
        int shift = env.variableShifts.get(p.ident_);
        
        String asm = "";
        asm += "\tmov rax, [rbp-"+ shift +"]\n";
        asm += "\tadd rax, 1\n";
        asm += "\tmov [rbp-"+ shift +"], rax\n";
        return asm;
    }
    
    /*
     *  Decrement
     */
    public String visit(Latte.Absyn.Decr p, LinkedList<Env> envs) {
        Env env = envs.getLast();

        int shift = env.variableShifts.get(p.ident_);
        
        String asm = "";
        asm += "\tmov rax, [rbp-"+ shift +"]\n";
        asm += "\tsub rax, 1\n";
        asm += "\tmov [rbp-"+ shift +"], rax\n";
        return asm;
    }

    /*
     *  Return
     */
    public String visit(Latte.Absyn.Ret p, LinkedList<Env> envs) {
        String asm = p.expr_.accept(new ExprVisitor(), envs);
        asm += "\tpop rax\n";
        asm += "\tleave\n\tret\n";
        return asm;
    }

    /*
     *  Void Return
     */
    public String visit(Latte.Absyn.VRet p, LinkedList<Env> envs) {
        return "\tmov rax, 0\n\tleave\n\tret\n";
    }

    /*
     *  Condition
     */
    public String visit(Latte.Absyn.Cond p, LinkedList<Env> envs) {
        Env env = envs.getLast();
         
        int ifNo = envs.getLast().ifCounter++;
        String suffix = env.funName.toUpperCase()+"_"+ifNo;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        
        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje AFTER_IF_"+suffix+"\n\n";
        
        asm += p.stmt_.accept(new StmtVisitor(), envs);
        
        asm += "AFTER_IF_"+ suffix+":\n";
        return asm;
    }

    /*
     *  Condition with else
     */
    public String visit(Latte.Absyn.CondElse p, LinkedList<Env> envs) {
        Env env = envs.getLast(); 
        
        int ifNo = env.ifCounter++;
        String suffix = env.funName.toUpperCase()+"_"+ifNo;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        
        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje  ELSE_"+suffix+"\n\n";
        
        asm += p.stmt_1.accept(new StmtVisitor(), envs);
        
        asm += "\tjmp AFTER_IF_"+suffix+"\n\n";
        asm += "ELSE_"+suffix+ ":\n";
        
        asm += p.stmt_2.accept(new StmtVisitor(), envs);
        
        asm += "AFTER_IF_"+suffix+ ":\n";
        return asm;
    }

    /*
     *  Loop
     */
    public String visit(Latte.Absyn.While p, LinkedList<Env> envs) {
        Env env = envs.getLast();
        
        int whileNo = envs.getLast().whileCounter++;
        
        String whileLabel = "WHILE_"+env.funName.toUpperCase()+"_"+whileNo;
        String afterWhileLabel = "AFTER_WHILE_"+env.funName.toUpperCase()+"_"+whileNo;

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

    /*
     *  Expression instruction
     */
    public String visit(Latte.Absyn.SExp p, LinkedList<Env> envs) {
        return p.expr_.accept(new ExprVisitor(), envs);
    }
}