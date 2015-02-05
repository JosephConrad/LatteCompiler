package Latte;

import Latte.Absyn.*;

import java.lang.Void;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class AsmGenerator
{

    String classname;
    String asm = "";
    String bss = "";
    Latte.Absyn.Program program;
    Integer currentNumber;
    Env env;

    public AsmGenerator(Latte.Absyn.Program program, String string) {
        env = new Env();
        // System.out.println(string);
        this.program = program;
        this.classname = string.split("\\.")[0];
    }

    void generateASM(){
        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();
        program.accept(programVisitor, env);
        System.out.print("\n\n\n"+bss);
        System.out.print("\n\n\n"+asm);
    }

    public class ProgramVisitor<R,S>  implements Program.Visitor<R,S>
    {
        public R visit(Latte.Absyn.Program p, S args)
        {
            bss += "section .bss\n";
            asm += "section .text\n";
            asm += "\tglobal main\n\n";
            asm += "extern printInt, printString, readInt, readString, error, contactString, calloc, malloc\n";
            for (TopDef x : p.listtopdef_) {
                x.accept(new TopDefVisitor(), env);
            }

            return null;
        }

    }









    public class TopDefVisitor implements TopDef.Visitor<Void,Env>
    {
        public Void visit(Latte.Absyn.FnDef p, Env arg)
        {   
            if (env.predefinedFunctions.contains(p.ident_)) {
                return null;
            }
            asm += p.ident_+":\n";
            asm += "\tenter 0,0\n";

            p.type_.accept(new TypeVisitor<Void, Env>(), arg);
            for (Arg a: p.listarg_) {
                a.accept(new ArgVisitor(), arg);
            }
            p.block_.accept(new BlockVisitor(), arg);
            
            asm += "\tleave\n";
            asm += "\tret\n";

            return null;
        }
    }
    
    
    
    /*
     * Argument Visitor
     */

    public class ArgVisitor implements Arg.Visitor<Void,Latte.Env>
    {
        public Void visit(Latte.Absyn.Arg p, Latte.Env arg)
        {
      /* Code For Arg Goes Here */

            p.type_.accept(new TypeVisitor<Void,Latte.Env>(), arg);
            //p.ident_;

            return null;
        }

    }
    public class BlockVisitor implements Block.Visitor<Void,Env>
    {
        public Void visit(Latte.Absyn.Block p, Env arg)
        {
      /* Code For Block Goes Here */

            for (Stmt x : p.liststmt_) {
                x.accept(new StmtVisitor(), arg);
                System.out.println("; "+x);
            }

            return null;
        }

    }
    
    
    
    /*
     *  Statement VISITOR
     */
    
    
    
    
    public class StmtVisitor implements Stmt.Visitor<Void,Latte.Env>
    {
        public Void visit(Latte.Absyn.Empty p, Env arg)
        {
      /* Code For Empty Goes Here */


            return null;
        }
        public Void visit(Latte.Absyn.BStmt p, Env arg)
        {
      /* Code For BStmt Goes Here */

            p.block_.accept(new BlockVisitor(), arg);

            return null;
        }

        /* ****************************************************************************
         * Blok deklaracji
         * ****************************************************************************/
        public Void visit(Latte.Absyn.Decl p, Env arg)
        {
      /* Code For Decl Goes Here */
            System.out.println(";Jestesm w deklaracji zmiennych "+ p.type_ + "\n");
            arg.setCurrentType(";" + p.type_.toString());
            p.type_.accept(new TypeVisitor<Void, Env>(), env);
            for (Item x : p.listitem_) {
                x.accept(new ItemVisitor(), arg);
            }
            

            return null;
        }
        public Void visit(Latte.Absyn.Ass p, Env arg)
        {
      /* Code For Ass Goes Here */

            //p.ident_;
            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);
            asm+="\tmov ["+p.ident_+"], rax\n";

            return null;
        }
        public Void visit(Latte.Absyn.Incr p, Env arg)
        {
      /* Code For Incr Goes Here */

            //p.ident_;

            return null;
        }
        public Void visit(Latte.Absyn.Decr p, Env arg)
        {
      /* Code For Decr Goes Here */

            //p.ident_;

            return null;
        }
        public Void visit(Latte.Absyn.Ret p, Env arg)
        {
      /* Code For Ret Goes Here */

            asm += "\tmov eax, 0\n";
            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);

            return null;
        }
        public Void visit(Latte.Absyn.VRet p, Env arg)
        {
      /* Code For VRet Goes Here */
;
            return null;
        }
        public Void visit(Latte.Absyn.Cond p, Env arg)
        {
      /* Code For Cond Goes Here */

            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);
            p.stmt_.accept(new StmtVisitor(), arg);

            return null;
        }
        public Void visit(Latte.Absyn.CondElse p, Env arg)
        {
      /* Code For CondElse Goes Here */

            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);
            p.stmt_1.accept(new StmtVisitor(), arg);
            p.stmt_2.accept(new StmtVisitor(), arg);

            return null;
        }
        public Void visit(Latte.Absyn.While p, Env arg)
        {
      /* Code For While Goes Here */

            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);
            p.stmt_.accept(new StmtVisitor(), arg);

            return null;
        }
        public Void visit(Latte.Absyn.SExp p, Env arg)
        {
            
            p.expr_.accept(new ExprVisitor<Void,Env>(), arg);

            return null;
        }

    }
    public class ItemVisitor implements Item.Visitor<Void,Latte.Env>
    {
        public Void visit(Latte.Absyn.NoInit p, Latte.Env env)
        {
      /* Code For NoInit Goes Here */

            //p.ident_;section .bss
            env.addVariable(p.ident_, env.getCurrentType(), env.rbp);
            bss += "\t"+p.ident_+"\t"+ "resd\t1\n";
            asm += "\tmov ["+p.ident_+"], dword 0\n";
            env.rbp += 4;
            System.out.println("; dupa "+ env);

            return null;
        }
        public Void visit(Latte.Absyn.Init p, Latte.Env arg)
        {
      /* Code For Init Goes Here */

            //p.ident_;
            env.addVariable(p.ident_, env.rbp);
            bss += "\t"+p.ident_+"\t"+ "resd\t1\n";
            asm += "\tmov ["+p.ident_+"], dword "+p.expr_+"\n";
            env.rbp += 4;
            System.out.println(";init " + env);
            p.expr_.accept(new ExprVisitor<Void,Latte.Env>(), arg);

            return null;
        }

    }
    public class TypeVisitor<R,A> implements Type.Visitor<R,A>
    {
        public R visit(Latte.Absyn.Int p, A arg)
        {
      /* Code For Int Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Str p, A arg)
        {
      /* Code For Str Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Bool p, A arg)
        {
      /* Code For Bool Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Void p, A arg)
        {
      /* Code For Void Goes Here */


            return null;
        }

        @Override
        public R visit(Fun p, A arg) {
            return null;
        }

    }
    public class ExprVisitor<R,A> implements Expr.Visitor<R,A>
    {
        public R visit(Latte.Absyn.EVar p, A arg)
        {
      /* Code For EVar Goes Here */
            asm += "\tmov rax, ["+p.ident_+"]\n";
            //p.ident_;

            return null;
        }
        public R visit(Latte.Absyn.ELitInt p, A arg)
        {
      /* Code For ELitInt Goes Here */
            currentNumber = p.integer_;

            return null;
        }
        public R visit(Latte.Absyn.ELitTrue p, A arg)
        {
      /* Code For ELitTrue Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.ELitFalse p, A arg)
        {
      /* Code For ELitFalse Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.EApp p, A arg)
        {
      /* Code For EApp Goes Here */

            //p.ident_;
            for (Expr expr : p.listexpr_) { 
                expr.accept(new ExprVisitor<R,A>(), arg);
                asm += "\tmov rdi, rax\n";
            }
            asm += "\tcall " + p.ident_ + "\n";
            

            return null;
        }
        public R visit(Latte.Absyn.EString p, A arg)
        {
      /* Code For EString Goes Here */

            //p.string_;

            return null;
        }
        public R visit(Latte.Absyn.Neg p, A arg)
        {
      /* Code For Neg Goes Here */

            p.expr_.accept(new ExprVisitor<R,A>(), arg);
            currentNumber = (-1)  * currentNumber;
            asm += "\tmov rax, "+currentNumber+"\n";
            return null;
        }
        public R visit(Latte.Absyn.Not p, A arg)
        {
      /* Code For Not Goes Here */

            p.expr_.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.EMul p, A arg)
        {
      /* Code For EMul Goes Here */

            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.EAdd p, A arg)
        {
      /* Code For EAdd Goes Here */

            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.addop_.accept(new AddOpVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.ERel p, A arg)
        {
      /* Code For ERel Goes Here */

            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.relop_.accept(new RelOpVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.EAnd p, A arg)
        {
      /* Code For EAnd Goes Here */

            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.EOr p, A arg)
        {
      /* Code For EOr Goes Here */

            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }

    }
    public class AddOpVisitor<R,A> implements AddOp.Visitor<R,A>
    {
        public R visit(Latte.Absyn.Plus p, A arg)
        {
      /* Code For Plus Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Minus p, A arg)
        {
      /* Code For Minus Goes Here */


            return null;
        }

    }
    public class MulOpVisitor<R,A> implements MulOp.Visitor<R,A>
    {
        public R visit(Latte.Absyn.Times p, A arg)
        {
      /* Code For Times Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Div p, A arg)
        {
      /* Code For Div Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.Mod p, A arg)
        {
      /* Code For Mod Goes Here */


            return null;
        }

    }
    public class RelOpVisitor<R,A> implements RelOp.Visitor<R,A>
    {
        public R visit(Latte.Absyn.LTH p, A arg)
        {
      /* Code For LTH Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.LE p, A arg)
        {
      /* Code For LE Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.GTH p, A arg)
        {
      /* Code For GTH Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.GE p, A arg)
        {
      /* Code For GE Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.EQU p, A arg)
        {
      /* Code For EQU Goes Here */


            return null;
        }
        public R visit(Latte.Absyn.NE p, A arg)
        {
      /* Code For NE Goes Here */


            return null;
        }

    }
}