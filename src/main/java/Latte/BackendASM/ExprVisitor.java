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
        return "\n\tpop eax\n";
    }

    public String twoArgs() {
        String asm = "\n\tpop ecx\n";
        asm += "\tpop eax\n";
        return asm;
    }

    /*
     * Variable expression
     */
    public String visit(Latte.Absyn.EVar p, Env env) throws TypeException {

        String argument = "";

        argument = "[ebp"+env.getVarStack(p.ident_)+"]";

        String asm = "\tmov eax, "+ argument+"\n";
        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * Integer literal expression
     */
    public String visit(Latte.Absyn.ELitInt p, Env env) {
        String asm = "\tmov eax, " + p.integer_+"\n";
        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * True value expression
     */
    public String visit(Latte.Absyn.ELitTrue p, Env env) {
        String asm = '\t' + "mov eax, 1\n" ;
        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * False value expression
     */
    public String visit(Latte.Absyn.ELitFalse p, Env env)
    {
        String asm = '\t' + "mov eax, 0\n" ;
        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * Function application expression
     */
    public String visit(Latte.Absyn.EApp p, Env env) throws TypeException {

        String asm = "";

            // Aplickacja funkcji - wolany musi zadbac o odpowiednie ustawienie parametrow funkcji
            int shift = (p.listexpr_.size())*4;
            for (int i=p.listexpr_.size()-1; i>=0;i--) {
                asm += p.listexpr_.get(i).accept(new ExprVisitor(), env);
            }
            asm += "\tcall " + p.ident_ + "\n";
            if (shift > 0){
                asm += "\tadd esp, " + shift + "\n";
            }
            asm += "\tpush eax\n";
        return asm;
    }

    /*
     * String expression
     */
    public String visit(Latte.Absyn.EString p, Env env) {

        env.addString(p.string_);
        String asm = "\tmov eax, (" + env.getStringAddress(p.string_) + ")\n";
        asm += "\tpush eax\n";
        //envs.getLast().addIsString = true;
        return asm;
    }

    /*
     * Negative expression
     */
    public String visit(Latte.Absyn.Neg p, Env env) throws TypeException {
        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\tneg eax\n";
        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * Negated expression
     */
    public String visit(Latte.Absyn.Not p, Env env) throws TypeException {

        String asm = p.expr_.accept(new ExprVisitor(), env);
        asm += oneArg();
        asm += "\txor eax, 1\n";
        asm += "\tpush eax\n";
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
        asm += "\tpush eax\n";
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

        env.determinePlusOperation(p.expr_1.expressionType);

        asm += p.addop_.accept(new AddOpVisitor(), env);

        asm += "\tpush eax\n";
        return asm;
    }

    /*
     * Relation expression
     */
    public String visit(Latte.Absyn.ERel p, Env env) throws TypeException {
        String asm = p.expr_1.accept(new ExprVisitor(), env);
        asm += p.expr_2.accept(new ExprVisitor(), env);

        String no = env.getCurrentFunctionIdent()+"_"+env.getNextLabel();
        String labelTrue  = env.getNextLabel();
        String labelFalse = env.getNextLabel();
        asm += twoArgs();

        asm += '\t'+ "cmp eax, ecx\n";
        asm += p.relop_.accept(new RelOpVisitor(), env) + " REL_TRUE_"+no + "\n\n";

        asm += "\tmov eax, 0\n";
        asm += "\tjmp REL_FINISH_"+no + "\n";

        asm += "\nREL_TRUE_"+no+":\n";
        asm += "\tmov eax, 1\n";
        asm += "\nREL_FINISH_"+no+":\n";

        asm += "\tpush eax\n\n";
        return asm;
    }

    /*
     * Conjunction expression
     */
    public String visit(Latte.Absyn.EAnd p, Env env) throws TypeException {
        String asm = p.expr_1.accept(new ExprVisitor(), env);

        String no = env.getCurrentFunctionIdent()+"_"+env.getNextLabel();

        String label = "AFTER_AND_"+no;
        asm += "\tmov eax, [esp]\n";
        asm += "\tcmp eax, 0\n";
        asm += "\tje "+label+"\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);

        asm += twoArgs();
        asm += "\tand eax, ecx\n";
        asm += "\tpush eax\n";
        asm += label+":\n";
        return asm;
    }

    /*
     * Alternative expression
     */
    public String visit(Latte.Absyn.EOr p, Env env) throws TypeException {

        String asm = p.expr_1.accept(new ExprVisitor(), env);
        String no = env.getCurrentFunctionIdent()+"_"+env.getNextLabel();

        String label = "AFTER_OR_"+no;
        asm += "\tmov eax, [esp]\n";
        asm += "\tcmp eax, 1\n";
        asm += "\t je " + label + "\n";

        asm += p.expr_2.accept(new ExprVisitor(), env);

        asm += twoArgs();
        asm += "\tor eax, ecx\n";
        asm += "\tpush eax\n";
        asm += label+":\n";
        return asm;
    }
}