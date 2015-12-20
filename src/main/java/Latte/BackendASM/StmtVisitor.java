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
        env.beginBlockASM();
        String asm = p.block_.accept(new BlockVisitor(), env);
        env.endBlockASM();
        return asm;
    }

    /*
     *  Declaration
     */
    public String visit(Latte.Absyn.Decl p, Env env) throws TypeException {

        p.type_.accept(new TypeVisitor(), env);

        String asm = "";
        for (Item x : p.listitem_) {
            env.addVariable(x.getIdent(), p.type_);
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
        asm += "\tpop eax\n";
        asm += "\tmov [ebp"+env.getVarStack(p.ident_)+"], eax\n";
        return asm;
    }

    /*
     *  Increment
     */
    public String visit(Latte.Absyn.Incr p, Env env) throws TypeException {

        String shift = env.getVarStack(p.ident_);

        String asm = "";
        asm += "\tmov eax, [ebp"+ shift +"]\n";
        asm += "\tadd eax, 1\n";
        asm += "\tmov [ebp"+ shift +"], eax\n";
        return asm;
    }

    /*
     *  Decrement
     */
    public String visit(Latte.Absyn.Decr p, Env env) throws TypeException {

        String shift = env.getVarStack(p.ident_);

        String asm = "";
        asm += "\tmov eax, [ebp-"+ shift +"]\n";
        asm += "\tsub eax, 1\n";
        asm += "\tmov [ebp-"+ shift +"], eax\n";
        return asm;
    }

    /*
     *  Return
     */
    public String visit(Latte.Absyn.Ret p, Env env) throws TypeException {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += "\tpop eax\n";
        asm += "\tjmp ret_" + env.getCurrentFunctionIdent() + "\n";
        return asm;
    }

    /*
     *  Void Return
     */
    public String visit(Latte.Absyn.VRet p, Env env) {
        String asm = "";
        asm += "\tmov eax, 0\n";
        asm += "\tjmp ret_" + env.getCurrentFunctionIdent() + "\n";
        return asm;
    }

    /*
     *  Condition
     */
    public String visit(Latte.Absyn.Cond p, Env env) throws TypeException {
        String suffix = env.getNextLabel();

        String asm = p.expr_.accept(new ExprVisitor(), env);

        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop eax\n";
        asm += "\tcmp eax, 0\n";
        asm += "\tje AFTER_IF_"+suffix+"\n\n";

        asm += p.stmt_.accept(new StmtVisitor(), env);

        asm += "AFTER_IF_"+ suffix+":\n";
        return asm;
    }

    /*
     *  Condition with else
     */
    public String visit(Latte.Absyn.CondElse p, Env env) throws TypeException {
        String suffix = env.getNextLabel();

        String asm = p.expr_.accept(new ExprVisitor(), env);

        asm += "\nIF_"+suffix+":\n";
        asm += "\tpop eax\n";
        asm += "\tcmp eax, 0\n";
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

        String whileLabel = env.getNextLabel();
        String afterWhileLabel = env.getNextLabel();

        String asm = whileLabel + ":\n";

        asm += p.expr_.accept(new ExprVisitor(), env);

        asm += "\tpop eax\n";
        asm += "\tcmp eax, 0\n";
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