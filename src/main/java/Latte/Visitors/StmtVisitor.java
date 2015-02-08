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
        return null;
    }
    public String visit(Latte.Absyn.BStmt p, LinkedList<Env> envs)
    {
        String asm = p.block_.accept(new BlockVisitor(), envs);
        return asm;
    }

    // Deklaracje
    public String visit(Latte.Absyn.Decl p, LinkedList<Env> envs)
    {
        envs.getLast().setCurrentType(";" + p.type_.toString());
        p.type_.accept(new TypeVisitor(), envs);
        String asm = "";
        for (Item x : p.listitem_) {
            envs.getLast().register = "rax";
            asm += x.accept(new ItemVisitor(), envs);
        }
        return asm;
    }

    // Przypisanie
    public String visit(Latte.Absyn.Ass p, LinkedList<Env> envs)
    {
        String asm = p.expr_.accept(new ExprVisitor(), envs);//eax
        asm += "\tpop rax\n";
        //asm += "\tmov eax, "+currentNumber+"\n";
        asm+="\tmov ["+p.ident_+"], rax\n";
        //currentNumber = 0;

        return asm;
    }

    // Inkrementacja
    public String visit(Latte.Absyn.Incr p, LinkedList<Env> envs)
    {
        //p.ident_;
        return null;
    }

    // Dekrementacja
    public String visit(Latte.Absyn.Decr p, LinkedList<Env> envs)
    {
        //p.ident_;
        return null;
    }

    // Return
    public String visit(Latte.Absyn.Ret p, LinkedList<Env> envs)
    {
        envs.getLast().register = "rax";
        String asm = p.expr_.accept(new ExprVisitor(), envs);
        return asm;
    }

    // Void Return
    public String visit(Latte.Absyn.VRet p, LinkedList<Env> envs)
    {
        return "\tmov rax, 0\n";
    }

    // Warunek
    public String visit(Latte.Absyn.Cond p, LinkedList<Env> envs)
    {
        envs.getLast().register = "rax"; // che dostac wynik obliczenia wyr do eax
        int ifNo = envs.getLast().ifCounter++;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        String ifLabel = "\nIF_"+ifNo;
        asm += ifLabel+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje AFTER_IF_"+ ifNo +"\n\n";
        envs.getLast().register = "rax";
        asm += p.stmt_.accept(new StmtVisitor(), envs);
        asm += "AFTER_IF_" + ifNo + ":\n";
        return asm;
    }

    // Warunek z elsem
    public String visit(Latte.Absyn.CondElse p, LinkedList<Env> envs)
    {
        envs.getLast().register = "rax"; // che dostac wynik obliczenia wyr do eax
        int ifNo = envs.getLast().ifCounter++;

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        String ifLabel = "\nIF_"+ifNo;
        asm += ifLabel+":\n";
        asm += "\tpop rax\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje  ELSE_"+ ifNo +"\n\n";
        envs.getLast().register = "rax";
        asm += p.stmt_1.accept(new StmtVisitor(), envs);
        asm += "\tjmp AFTER_IF_"+ ifNo +"\n\n";
        asm += "ELSE_" + ifNo + ":\n";
        envs.getLast().register = "rax";
        asm += p.stmt_2.accept(new StmtVisitor(), envs);
        asm += "AFTER_IF_" + ifNo + ":\n";

        return asm;
    }

    // Petla
    public String visit(Latte.Absyn.While p, LinkedList<Env> envs)
    {   
        int whileNo = envs.getLast().whileCounter++;
        String whileLabel = "WHILE_"+whileNo;
        String afterWhileLabel = "AFTER_WHILE_"+whileNo;

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


