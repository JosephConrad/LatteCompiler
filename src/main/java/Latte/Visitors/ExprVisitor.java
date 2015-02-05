package Latte.Visitors;

import Latte.Absyn.Expr;
import Latte.Env;

/**
 * Class for expressions
 
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, Env>
{
    public String visit(Latte.Absyn.EVar p, Env arg)
    {
        String argument = "["+p.ident_+"]";
        String asm = "\tmov eax, "+ argument+"\n";
        return asm;
    }


    // Literal liczbowy
    public String visit(Latte.Absyn.ELitInt p, Env env)
    {
        String asm = "\tmov " + env.register + ", " + p.integer_+"\n";
        return asm;
    }
    
    // True
    public String visit(Latte.Absyn.ELitTrue p, Env env)
    {
        String asm = '\t' + "mov " + env.register + ", 1\n" ;
        return asm;
    }
    
    // Wyrazenie False
    public String visit(Latte.Absyn.ELitFalse p, Env env)
    {
        String asm = '\t' + "mov " + env.register + ", 0\n" ;
        return asm;
    }
    
    // Dodawanie
    public String visit(Latte.Absyn.EApp p, Env arg)
    {
        String asm = "";
        for (Expr expr : p.listexpr_) {
            expr.accept(new ExprVisitor(), arg);
            asm += "\tmov edi, eax\n";
        }
        asm += "\tcall " + p.ident_ + "\n";

        return asm;
    }
    
    // Napis
    public String visit(Latte.Absyn.EString p, Env arg)
    {
        return null;
    }
    public String visit(Latte.Absyn.Neg p, Env arg)
    {
        p.expr_.accept(new ExprVisitor(), arg);
        String asm = "";
        //String asm = "\tsub eax, " + currentNumber+ " \n";
        //asm += "\tsub eax, " + currentNumber+ " \n";
        return asm;
    }

    public String visit(Latte.Absyn.Not p, Env arg)
    {

        p.expr_.accept(new ExprVisitor(), arg);

        return null;
    }

    public String visit(Latte.Absyn.EMul p, Env arg)
    {
        p.expr_1.accept(new ExprVisitor(), arg);
        p.mulop_.accept(new MulOpVisitor(), arg);
        p.expr_2.accept(new ExprVisitor(), arg);

        return null;
    }
    public String visit(Latte.Absyn.EAdd p, Env env)
    {
        env.register = "eax";
        p.expr_1.accept(new ExprVisitor(), env);//eax
        p.addop_.accept(new AddOpVisitor(), env);
        env.register = "eax";
        p.expr_2.accept(new ExprVisitor(), env);//edx
        String asm = "\tadd eax, edx\n";

        return asm;
    }
    public String visit(Latte.Absyn.ERel p, Env env)
    {   
        env.register = "eax";
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        env.register = "edx";
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += '\t'+ "cmp eax, edx\n";
        asm += p.relop_.accept(new RelOpVisitor(), env);

        return asm;
    }
    public String visit(Latte.Absyn.EAnd p, Env arg)
    {
        p.expr_1.accept(new ExprVisitor(), arg);
        p.expr_2.accept(new ExprVisitor(), arg);

        return null;
    }


    public String visit(Latte.Absyn.EOr p, Env arg)
    {
        p.expr_1.accept(new ExprVisitor(), arg);
        p.expr_2.accept(new ExprVisitor(), arg);

        return null;
    }
}
