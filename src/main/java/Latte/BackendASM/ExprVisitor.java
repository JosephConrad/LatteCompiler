package Latte.BackendASM;

import Latte.Absyn.Expr;
import Latte.Env;
import Latte.Exceptions.TypeException;

import java.util.LinkedList;

/**
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, LinkedList<Env>>
{
    public String oneArg() {
        return "\n\tpop rax\n";
    }

    public String twoArgs() {
        String asm = "\n\tpop rcx\n";
        asm += "\tpop rax\n";
        return asm;
    }

    /*
     * Variable expression
     */
    public String visit(Latte.Absyn.EVar p, LinkedList<Env> envs) {
        Env env = envs.getLast();
        
        String argument = "";
        if (env.variableShifts.containsKey(p.ident_)) {
            argument = "[rbp-"+env.variableShifts.get(p.ident_)+"]";
        }
        if (env.argumentsShifts.containsKey(p.ident_)) {
            argument = "[rbp+"+env.argumentsShifts.get(p.ident_)+"]";
        }
        
        String asm = "\tmov rax, "+ argument+"\n";
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Integer literal expression
     */
    public String visit(Latte.Absyn.ELitInt p, LinkedList<Env> env) {
        String asm = "\tmov rax, " + p.integer_+"\n";
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * True value expression
     */
    public String visit(Latte.Absyn.ELitTrue p, LinkedList<Env> env) {
        String asm = '\t' + "mov rax, 1\n" ;
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * False value expression
     */
    public String visit(Latte.Absyn.ELitFalse p, LinkedList<Env> env)
    {
        String asm = '\t' + "mov rax, 0\n" ;
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Function application expression
     */
    public String visit(Latte.Absyn.EApp p, LinkedList<Env> envs) {
        Env env = envs.getLast();

        String asm = "";
        if (env.predefinedFunctions.contains(p.ident_)) {
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), envs);
                asm += "\tpop rax\n";
            }
            asm += "\tmov rdi, rax\n";
            asm += "\tcall " + p.ident_ + "\n";
        } else {
            int shift = (p.listexpr_.size())*8;
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), envs); 
            }
            asm += "\tcall " + p.ident_ + "\n";
            asm += "\tadd rsp, " + shift + "\n";
        }
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * String expression
     */
    public String visit(Latte.Absyn.EString p, LinkedList<Env> envs) {
        Env env = envs.getLast();
        
        int stringNo = envs.getLast().strings.size();
        String stringID = "STR_"+env.funName + "_" + stringNo;
        
        Env.strings.put(stringID, p.string_);
        
        String asm = "\tmov rax, (" + stringID + ")\n";
        asm += "\tpush rax\n";
        
        envs.getLast().addIsString = true;
        
        return asm;
    }

    /*
     * Negative expression
     */
    public String visit(Latte.Absyn.Neg p, LinkedList<Env> env) {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\tneg rax\n";
        asm += "\tpush rax\n";
        return asm;
    }
    
    /*
     * Negated expression
     */
    public String visit(Latte.Absyn.Not p, LinkedList<Env> envs) {
        
        String asm = p.expr_.accept(new ExprVisitor(), envs);
        asm += oneArg();
        asm += "\txor rax, 1\n";
        asm += "\tpush rax\n";
        return asm;
    }
    
    /*
     * Multiplication expression
     */
    public String visit(Latte.Absyn.EMul p, LinkedList<Env> envs) {
        
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);
        asm += twoArgs();
        asm += p.mulop_.accept(new MulOpVisitor(), envs);
        asm += "\tpush rax\n";
        return asm;
    }
    
    /*
     * Sum expression
     */
    public String visit(Latte.Absyn.EAdd p, LinkedList<Env> envs) {
        String type = null;
        try {
            type = p.returnExprType(envs, "");
        } catch (TypeException e) {
            e.printStackTrace();
        }

        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);
        asm += twoArgs();
        
        if (type == "string")
            envs.getLast().addIsString = true;
        
        asm += p.addop_.accept(new AddOpVisitor(), envs);
        
        envs.getLast().addIsString = false;
        
        asm += "\tpush rax\n";
        return asm;
    }
    
    /*
     * Relation expression
     */
    public String visit(Latte.Absyn.ERel p, LinkedList<Env> envs) {
        Env env = envs.getLast();
        
        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        asm += p.expr_2.accept(new ExprVisitor(), envs);

        String no = env.funName+"_"+envs.getLast().jmpExpCounter++;
        
        asm += twoArgs();

        asm += '\t'+ "cmp rax, rcx\n";
        asm += p.relop_.accept(new RelOpVisitor(), envs) + " REL_TRUE_"+no + "\n\n";
        
        asm += "\tmov rax, 0\n";
        asm += "\tjmp REL_FINISH_"+no + "\n";
        
        asm += "\nREL_TRUE_"+no+":\n";
        asm += "\tmov rax, 1\n";
        asm += "\nREL_FINISH_"+no+":\n";

        asm += "\tpush rax\n\n";
        return asm;
    }

    /*
     * Conjunction expression
     */
    public String visit(Latte.Absyn.EAnd p, LinkedList<Env> envs) {
        Env env = envs.getLast();

        String asm = p.expr_1.accept(new ExprVisitor(), envs);

        String no = env.funName+"_"+envs.getLast().andExpCounter++;
        
        String label = "AFTER_AND_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje "+label+"\n";

        asm += p.expr_2.accept(new ExprVisitor(), envs);

        asm += twoArgs();
        asm += "\tand rax, rcx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }

    /*
     * Alternative expression
     */
    public String visit(Latte.Absyn.EOr p, LinkedList<Env> envs) {
        Env env = envs.getLast();

        String asm = p.expr_1.accept(new ExprVisitor(), envs);
        String no = env.funName+"_"+envs.getLast().orExpCounter++;
        
        String label = "AFTER_OR_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 1\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), envs);

        asm += twoArgs();
        asm += "\tor rax, rcx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }
}