package Latte.Visitors;

import Latte.Absyn.Expr;
import Latte.Env;

/**
 * Class for expressions
 
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, Env>
{
    public String visit(Latte.Absyn.EVar p, Env env)
    {
        String argument = "["+p.ident_+"]";
        String asm = "\tmov "+ env.register + ", "+ argument+"\n";
        return asm;
    }


    // Literal liczbowy
    public String visit(Latte.Absyn.ELitInt p, Env env)
    {
        String asm = "\tmov " + env.register + ", " + p.integer_+"\n";
        env.neg = p.integer_;
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
    
    // Wywolanie funkcji
    public String visit(Latte.Absyn.EApp p, Env env)
    {
        String asm = "";
        for (Expr expr : p.listexpr_) {
            env.register = "rax";
            asm += expr.accept(new ExprVisitor(), env);
            asm += "\tmov rdi, rax\n";
            //asm += "\tpush eax\n";
        }
        asm += "\tcall " + p.ident_ + "\n";

        return asm;
    }
    
    // Napis - zwraca identyfikator do wypisania
    public String visit(Latte.Absyn.EString p, Env env)
    {
        int stringNo = env.strings.size();
        String stringID = "STR_"+stringNo;
        env.strings.put(stringID, p.string_);
        String asm = "\tmov rax, (" + stringID + ")\n";
        return asm;
    }
   
    public String visit(Latte.Absyn.Neg p, Env env)
    {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += "\tneg rax\n";
        asm += "\tpush rax\n";
        return asm;
    }

    public String visit(Latte.Absyn.Not p, Env arg)
    {

        p.expr_.accept(new ExprVisitor(), arg);

        return null;
    }

    public String visit(Latte.Absyn.EMul p, Env env)
    {
        env.register = "rax";
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        env.register = "rbx";
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += "\tmov rdx, 0\n";
        asm += p.mulop_.accept(new MulOpVisitor(), env);
        

        return asm;
    }
    public String visit(Latte.Absyn.EAdd p, Env env)
    {
        env.register = "rax";
        String asm = p.expr_1.accept(new ExprVisitor(), env);//eax
        env.register = "rdx";
        asm += p.expr_2.accept(new ExprVisitor(), env);//edx
        asm += p.addop_.accept(new AddOpVisitor(), env) + " rax, rdx\n";
        return asm;
    }
    public String visit(Latte.Absyn.ERel p, Env env)
    {   
        env.register = "rax";
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        env.register = "rdx";
        asm += p.expr_2.accept(new ExprVisitor(), env);
        
        
        int no = env.jmpExpCounter++;
        
        asm += '\t'+ "cmp rax, rdx\n";
        asm += p.relop_.accept(new RelOpVisitor(), env) + " REL_TRUE_"+no + "\n";
        asm += "\tjmp REL_FALSE_"+no + "\n";
        
        asm += "\nREL_TRUE_"+no+":\n";
        asm += "\tmov rax, 1\n";
        asm += "\tjmp REL_FINISH_"+no+"\n";

        asm += "\nREL_FALSE_"+no+":\n";
        asm += "\tmov rax, 0\n";
        asm += "\nREL_FINISH_"+no+":\n";
        
        asm += "\tpush rax\n";
        

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
