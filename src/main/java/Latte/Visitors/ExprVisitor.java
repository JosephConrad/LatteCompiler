package Latte.Visitors;

import Latte.Absyn.Expr;
import Latte.Env;

/**
 * Class for expressions
 
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, Env>
{   
    
    public String oneArg(){
        return "\n\tpop rax\n";
    }
    
    public String twoArgs(){
        String asm = "\n\tpop rbx\n";
        asm += "\tpop rax\n";
        return asm;
    }
    
    public String visit(Latte.Absyn.EVar p, Env env)
    {
        String argument = "["+p.ident_+"]";
        //String asm = oneArg();
        String asm = "\tmov rax, "+ argument+"\n";
        asm += "\tpush rax\n";
        return asm;
    }


    // Literal liczbowy
    public String visit(Latte.Absyn.ELitInt p, Env env)
    {
        String asm = "\tmov rax, " + p.integer_+"\n";
        asm += "\tpush rax\n";
        return asm;
    }
    
    // True
    public String visit(Latte.Absyn.ELitTrue p, Env env)
    {
        String asm = '\t' + "mov rax, 1\n" ;
        asm += "\tpush rax\n";
        return asm;
    }
    
    // Wyrazenie False
    public String visit(Latte.Absyn.ELitFalse p, Env env)
    {
        String asm = '\t' + "mov rax, 0\n" ;
        asm += "\tpush rax\n";
        return asm;
    }
    
    // Wywolanie funkcji
    public String visit(Latte.Absyn.EApp p, Env env)
    {
        String asm = "";
        for (Expr expr : p.listexpr_) {
            env.register = "rax";
            asm += expr.accept(new ExprVisitor(), env);
            asm += "\tpop rax\n";
            asm += "\tmov rdi, rax\n";
            //asm += "\tpush eax\n";
        }
        asm += "\tcall " + p.ident_ + "\n";
        asm += "\tpush rax\n";

        return asm;
    }
    
    // Napis - zwraca identyfikator do wypisania
    public String visit(Latte.Absyn.EString p, Env env)
    {
        int stringNo = env.strings.size();
        String stringID = "STR_"+stringNo;
        env.strings.put(stringID, p.string_);
        String asm = "\tmov rax, (" + stringID + ")\n";
        asm += "\tpush rax\n";
        env.addIsString = true;
        return asm;
    }
   
    public String visit(Latte.Absyn.Neg p, Env env)
    {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\tneg rax\n";
        asm += "\tpush rax\n";
        return asm;
    }

    public String visit(Latte.Absyn.Not p, Env arg)
    {

        String asm = p.expr_.accept(new ExprVisitor(), arg);
        asm += oneArg();
        asm += "\txor rax, 1\n";
        asm += "\tpush rax\n";
        return asm;
    }

    public String visit(Latte.Absyn.EMul p, Env env)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += twoArgs();
        asm += p.mulop_.accept(new MulOpVisitor(), env);
        asm += "\tpush rax\n";
        return asm;
    }
    public String visit(Latte.Absyn.EAdd p, Env env)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += twoArgs();
        
        asm += p.addop_.accept(new AddOpVisitor(), env);
        env.addIsString = false;
        asm += "\tpush rax\n";
        return asm;
    }
    public String visit(Latte.Absyn.ERel p, Env env)
    {   
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);
        
        int no = env.jmpExpCounter++;
        asm += twoArgs();

        asm += '\t'+ "cmp rax, rbx\n";
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
    public String visit(Latte.Absyn.EAnd p, Env env)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), env);

        int no = env.andExpCounter++;
        String label = "AFTER_AND_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 0\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);
       
        asm += twoArgs();
        asm += "\tand rax, rbx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }


    public String visit(Latte.Absyn.EOr p, Env env)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        int no = env.orExpCounter++;
        String label = "AFTER_OR_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 1\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);

        asm += twoArgs();
        asm += "\tor rax, rbx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }
}
