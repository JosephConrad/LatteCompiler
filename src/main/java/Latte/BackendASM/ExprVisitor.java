package Latte.BackendASM;

import Latte.Absyn.Expr;
import Latte.Env;
import Latte.Exceptions.TypeException;

/**
 * Created by konrad on 05/02/15.
 */

public class ExprVisitor implements Expr.Visitor<String, Env>
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
    public String visit(Latte.Absyn.EVar p, Env env) {

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
    public String visit(Latte.Absyn.ELitInt p, Env env) {
        String asm = "\tmov rax, " + p.integer_+"\n";
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * True value expression
     */
    public String visit(Latte.Absyn.ELitTrue p, Env env) {
        String asm = '\t' + "mov rax, 1\n" ;
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * False value expression
     */
    public String visit(Latte.Absyn.ELitFalse p, Env env)
    {
        String asm = '\t' + "mov rax, 0\n" ;
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Function application expression
     */
    public String visit(Latte.Absyn.EApp p, Env env) throws TypeException {

        String asm = "";
        if (env.predefinedFunctions.contains(p.ident_)) {
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), env);
                asm += "\tpop rax\n";
            }
            asm += "\tmov rdi, rax\n";
            asm += "\tcall " + p.ident_ + "\n";
        } else {
            int shift = (p.listexpr_.size())*8;
            for (Expr expr : p.listexpr_) {
                env.ileArgumentow++;
                asm += expr.accept(new ExprVisitor(), env);
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
    public String visit(Latte.Absyn.EString p, Env env) {

        env.addString(p.string_);
        String asm = "\tmov rax, (" + env.getStringAddress(p.string_) + ")\n";
        asm += "\tpush rax\n";
        //envs.getLast().addIsString = true;
        return asm;
    }

    /*
     * Negative expression
     */
    public String visit(Latte.Absyn.Neg p, Env env) throws TypeException {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\tneg rax\n";
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Negated expression
     */
    public String visit(Latte.Absyn.Not p, Env env) throws TypeException {

        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\txor rax, 1\n";
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Multiplication expression
     */
    public String visit(Latte.Absyn.EMul p, Env env) throws TypeException {

        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += twoArgs();
        asm += p.mulop_.accept(new MulOpVisitor(), env);
        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Sum expression
     */
    public String visit(Latte.Absyn.EAdd p, Env env) throws TypeException {
        String asm = "";
        asm += p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);
        asm += twoArgs();

        asm += p.addop_.accept(new AddOpVisitor(), env);

        asm += "\tpush rax\n";
        return asm;
    }

    /*
     * Relation expression
     */
    public String visit(Latte.Absyn.ERel p, Env env) throws TypeException {
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);

        String no = env.funName+"_"+env.jmpExpCounter++;

        asm += twoArgs();

        asm += '\t'+ "cmp rax, rcx\n";
        asm += p.relop_.accept(new RelOpVisitor(), env) + " REL_TRUE_"+no + "\n\n";

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
    public String visit(Latte.Absyn.EAnd p, Env env) throws TypeException {
        String asm = p.expr_1.accept(new ExprVisitor(), env);

        String no = env.funName+"_"+env.andExpCounter++;

        String label = "AFTER_AND_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 0\n";
        asm += "\tje "+label+"\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);

        asm += twoArgs();
        asm += "\tand rax, rcx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }

    /*
     * Alternative expression
     */
    public String visit(Latte.Absyn.EOr p, Env env) throws TypeException {

        String asm = p.expr_1.accept(new ExprVisitor(), env);
        String no = env.funName+"_"+env.orExpCounter++;

        String label = "AFTER_OR_"+no;
        asm += "\tmov rax, [rsp]\n";
        asm += "\tcmp rax, 1\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);

        asm += twoArgs();
        asm += "\tor rax, rcx\n";
        asm += "\tpush rax\n";
        asm += label+":\n";
        return asm;
    }
}