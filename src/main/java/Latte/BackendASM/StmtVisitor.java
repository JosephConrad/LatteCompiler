package Latte.BackendASM;

import Latte.Absyn.Item;
import Latte.Absyn.Stmt;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 05/02/15.
 */

public class StmtVisitor implements Stmt.Visitor<String, Env>
{
    public String visit(Latte.Absyn.Empty p, Env env) {
        return "";
    }

    public String visit(Latte.Absyn.BStmt p, Env env) throws TypeException {
        return p.block_.accept(new BlockVisitor(), env);
    }

    /*
     *  Declaration
     */
    public String visit(Latte.Absyn.Decl p, Env env) throws TypeException {

        p.type_.accept(new TypeVisitor(), env);

        String asm = "";
        for (Item x : p.listitem_) {
            env.variableType.put(x.getIdent(), p.type_.toString());
            asm += x.accept(new ItemVisitor(), env);
        }
        return asm;
    }

    /*
     *  Assignment
     */
    public String visit(Latte.Absyn.Ass p, Env env) throws TypeException {

        String asm = "";

        asm += p.expr_.accept(new ExprVisitor(), env);
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
    public String visit(Latte.Absyn.Incr p, Env env) {

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
    public String visit(Latte.Absyn.Decr p, Env env) {

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
    public String visit(Latte.Absyn.Ret p, Env env) throws TypeException {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += "\tpop rax\n";
        asm += "\tleave\n\tret\n";
        return asm;
    }

    /*
     *  Void Return
     */
    public String visit(Latte.Absyn.VRet p, Env env) {
        return "\tmov rax, 0\n\tleave\n\tret\n";
    }

    /*
     *  Condition
     */
    public String visit(Latte.Absyn.Cond p, Env env) throws TypeException {
        int ifNo = env.ifCounter++;
        String suffix = env.funName.toUpperCase()+"_"+ifNo;

        String asm = p.expr_.accept(new ExprVisitor(), env);

        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje AFTER_IF_"+suffix+"\n\n";

        asm += p.stmt_.accept(new StmtVisitor(), env);

        asm += "AFTER_IF_"+ suffix+":\n";
        return asm;
    }

    /*
     *  Condition with else
     */
    public String visit(Latte.Absyn.CondElse p, Env env) throws TypeException {

        int ifNo = env.ifCounter++;
        String suffix = env.funName.toUpperCase()+"_"+ifNo;

        String asm = p.expr_.accept(new ExprVisitor(), env);

        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje  ELSE_"+suffix+"\n\n";

        asm += p.stmt_1.accept(new StmtVisitor(), env);

        asm += "\tjmp AFTER_IF_"+suffix+"\n\n";
        asm += "ELSE_"+suffix+ ":\n";

        asm += p.stmt_2.accept(new StmtVisitor(), env);

        asm += "AFTER_IF_"+suffix+ ":\n";
        return asm;
    }

    /*
     *  Loop
     */
    public String visit(Latte.Absyn.While p, Env env) throws TypeException {

        int whileNo = env.whileCounter++;

        String whileLabel = "WHILE_"+env.funName.toUpperCase()+"_"+whileNo;
        String afterWhileLabel = "AFTER_WHILE_"+env.funName.toUpperCase()+"_"+whileNo;

        String asm = whileLabel + ":\n";

        asm += p.expr_.accept(new ExprVisitor(), env);

        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje " + afterWhileLabel+"\n\n";

        asm += p.stmt_.accept(new StmtVisitor(), env);

        asm += "\tjmp "+whileLabel+"\n\n";
        asm += afterWhileLabel+ ":\n\n";
        return asm;
    }

    /*
     *  Expression instruction
     */
    public String visit(Latte.Absyn.SExp p, Env env) throws TypeException {
        return p.expr_.accept(new ExprVisitor(), env);
    }
}