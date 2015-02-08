package Latte.Visitors;

import Latte.Absyn.Expr;
import Latte.Env;

import java.util.LinkedList;

/**
 * Class for expressions
 
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, LinkedList<Env>>
{   
    
    public String oneArg(){
        return "\n\tpop rax\n";
    }

    public String twoArgs(){
        String asm = "\n\tpop rbx\n";
        asm += "\tpop rax\n";
        return asm;
    }
    
    public String visit(Latte.Absyn.EVar p, LinkedList<Env> envs)
    {
        
        Env env = envs.getLast();

        String argument = "[rbp-"+env.variableShifts.get(p.ident_)+"]";
        //String asm = oneArg();
        String asm = "\tmov rax, "+ argument+"\n";
        asm += "\tpush rax\n";
        return asm;
    }


    // Literal liczbowy
    public String visit(Latte.Absyn.ELitInt p, LinkedList<Env> env)
    {
        String asm = "\tmov rax, " + p.integer_+"\n";
        asm += "\tpush rax\n";
        return asm;
    }
    
    // True
    public String visit(Latte.Absyn.ELitTrue p, LinkedList<Env> env)
    {
        String asm = '\t' + "mov rax, 1\n" ;
        asm += "\tpush rax\n";
        return asm;
    }
    
    // Wyrazenie False
    public String visit(Latte.Absyn.ELitFalse p, LinkedList<Env> env)
    {
        String asm = '\t' + "mov rax, 0\n" ;
        asm += "\tpush rax\n";
        return asm;
    }
    
    // Wywolanie funkcji
    public String visit(Latte.Absyn.EApp p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        
        String asm = "";
        if (env.predefinedFunctions.contains(p.ident_)){
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), envs);
                asm += "\tpop rax\n";
                asm += "\tmov rdi, rax\n";
                asm += "\tcall " + p.ident_ + "\n";
            }
        } else {
            int shift = (p.listexpr_.size())*8;
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), envs);
                asm += "\tcall " + p.ident_ + "\n";
                asm += "\tadd rsp, " + shift + "\n";
                //asm += "\tpop rax\n";
                //asm += "\tmov [rsp+"+ shift +"], rax\n";
                //shift -= 8;
            }
        }

        asm += "\tpush rax\n";

        return asm;
    }
    
    // Napis - zwraca identyfikator do wypisania
    public String visit(Latte.Absyn.EString p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        int stringNo = envs.getLast().strings.size();
        String stringID = "STR_"+env.funName + "_" + stringNo;
        Env.strings.put(stringID, p.string_);
        String asm = "\tmov rax, (" + stringID + ")\n";
        asm += "\tpush rax\n";
        envs.getLast().addIsString = true;
        return asm;
    }
   
    public String visit(Latte.Absyn.Neg p, LinkedList<Env> env)
    {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\tneg rax\n";
        asm += "\tpush rax\n";
        return asm;
    }

    public String visit(Latte.Absyn.Not p, LinkedList<Env> envs)
    {

        String asm = p.expr_.accept(new ExprVisitor(), envs);
        asm += oneArg();
        asm += "\txor rax, 1\n";
        asm += "\tpush rax\n";
        return asm;
    }

    public String visit(Latte.Absyn.EMul p, LinkedList<Env> envs)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);
        asm += twoArgs();
        asm += p.mulop_.accept(new MulOpVisitor(), envs);
        asm += "\tpush rax\n";
        return asm;
    }
    public String visit(Latte.Absyn.EAdd p, LinkedList<Env> envs)
    {
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);
        asm += twoArgs();
        
        asm += p.addop_.accept(new AddOpVisitor(), envs);
        envs.getLast().addIsString = false;
        asm += "\tpush rax\n";
        return asm;
    }
    public String visit(Latte.Absyn.ERel p, LinkedList<Env> envs)
    {   
        Env env = envs.getLast();
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);
        
        String no = env.funName+"_"+envs.getLast().jmpExpCounter++;
        asm += twoArgs();

        asm += '\t'+ "cmp rax, rbx\n";
        asm += p.relop_.accept(new RelOpVisitor(), envs) + " REL_TRUE_"+no + "\n";
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
    public String visit(Latte.Absyn.EAnd p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        
        String asm = p.expr_1.accept(new ExprVisitor(), envs);

        String no = env.funName+"_"+envs.getLast().andExpCounter++;
        String label = "AFTER_AND_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 0\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), envs);
       
        asm += twoArgs();
        asm += "\tand rax, rbx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }


    public String visit(Latte.Absyn.EOr p, LinkedList<Env> envs)
    {
        Env env = envs.getLast();
        
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        String no = env.funName+"_"+envs.getLast().orExpCounter++;
        String label = "AFTER_OR_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 1\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), envs);

        asm += twoArgs();
        asm += "\tor rax, rbx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }
}
