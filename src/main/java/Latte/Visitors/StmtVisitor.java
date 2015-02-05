package Latte.Visitors;

import Latte.Absyn.Item;
import Latte.Absyn.Stmt;
import Latte.Env;

/**
 * Created by konrad on 05/02/15.
 */



public class StmtVisitor implements Stmt.Visitor<String, Env>
{
    public String visit(Latte.Absyn.Empty p, Env arg)
    {
        return null;
    }
    public String visit(Latte.Absyn.BStmt p, Env arg)
    {
        String asm = p.block_.accept(new BlockVisitor(), arg);
        return asm;
    }

    // Deklaracje
    public String visit(Latte.Absyn.Decl p, Env env)
    {
        env.setCurrentType(";" + p.type_.toString());
        p.type_.accept(new TypeVisitor<String, Env>(), env);
        String asm = "";
        for (Item x : p.listitem_) {
            env.register = "eax";
            asm += x.accept(new ItemVisitor(), env);
        }
        return asm;
    }

    // Przypisanie
    public String visit(Latte.Absyn.Ass p, Env arg)
    {
        String asm = p.expr_.accept(new ExprVisitor(), arg);//eax
        //asm += "\tmov eax, "+currentNumber+"\n";
        asm+="\tmov ["+p.ident_+"], eax\n";
        //currentNumber = 0;

        return asm;
    }

    // Inkrementacja
    public String visit(Latte.Absyn.Incr p, Env arg)
    {
        //p.ident_;
        return null;
    }

    // Dekrementacja
    public String visit(Latte.Absyn.Decr p, Env arg)
    {
        //p.ident_;
        return null;
    }

    // Return
    public String visit(Latte.Absyn.Ret p, Env env)
    {
        env.register = "eax";
        String asm = p.expr_.accept(new ExprVisitor(), env);
        return asm;
    }

    // Void Return
    public String visit(Latte.Absyn.VRet p, Env arg)
    {
        return null;
    }

    // Warunek
    public String visit(Latte.Absyn.Cond p, Env arg)
    {
        String asm = p.expr_.accept(new ExprVisitor(), arg); //eax
        asm += " AFTER_IF\n\n";

        asm += p.stmt_.accept(new StmtVisitor(), arg);
        asm += "AFTER_IF:\n";
        return asm;
    }

    // Warunek z elsem
    public String visit(Latte.Absyn.CondElse p, Env env)
    {
        env.register = "eax"; // che dostac wynik obliczenia wyr do eax
        int ifNo = env.ifCounter++;
        String asm = p.expr_.accept(new ExprVisitor(), env) + " ELSE_"+ ifNo +"\n\n";
        
        env.register = "eax";
        asm += p.stmt_1.accept(new StmtVisitor(), env);
        asm += "\tjmp AFTER_IF_"+ ifNo +"\n\n";
        asm += "ELSE_" + ifNo + ":\n\n";
        env.register = "eax";
        asm += p.stmt_2.accept(new StmtVisitor(), env);
        asm += "AFTER_IF_" + ifNo + ":\n";

        return asm;
    }

    // Petla
    public String visit(Latte.Absyn.While p, Env arg)
    {
        String asm = p.expr_.accept(new ExprVisitor(), arg);
        asm += p.stmt_.accept(new StmtVisitor(), arg);

        return asm;
    }

    // Instrukcja dla wyrazenia
    public String visit(Latte.Absyn.SExp p, Env arg)
    {

        String asm = p.expr_.accept(new ExprVisitor(), arg);

        return asm;
    }

}


