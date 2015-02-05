package Latte;

import Latte.Absyn.*;
import Latte.Visitors.RelOpVisitor;
import Latte.Visitors.TypeVisitor;

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
    String argument = "";
    String currentMnemonic;
    Latte.Absyn.Program program;
    Integer currentNumber = 0;
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
        System.out.print("\n\n\n" + bss);
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
            asm += "\n\n\n";

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
                asm += "\n";
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
            p.expr_.accept(new ExprVisitor<Void,String>(), "eax");
            //asm += "\tmov eax, "+currentNumber+"\n";
            asm+="\tmov ["+p.ident_+"], eax\n";
            currentNumber = 0;

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

            p.expr_.accept(new ExprVisitor<Void,String>(), "eax");
            //asm += "\tmov eax, "+currentNumber+"\n";

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
            p.expr_.accept(new ExprVisitor<Void,String>(), "eax");
            asm += " AFTER_IF\n\n";
            p.stmt_.accept(new StmtVisitor(), arg);
            asm += "AFTER_IF:\n";
            return null;
        }
        public Void visit(Latte.Absyn.CondElse p, Env arg)
        {
      /* Code For CondElse Goes Here */

            asm += p.expr_.accept(new ExprVisitor<Void,String>(), "eax");
            asm += '\t'+ currentMnemonic + " ELSE\n\n";
            p.stmt_1.accept(new StmtVisitor(), arg);
            asm += "\tjmp AFTER_IF\n\n";
            asm += "ELSE:\n";
            p.stmt_2.accept(new StmtVisitor(), arg);
            asm += "AFTER_IF:\n";

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

            p.expr_.accept(new ExprVisitor<Void,String>(), "eax");
            env.addVariable(p.ident_, env.rbp);
            bss += "\t"+p.ident_+"\t"+ "resd\t1\n";
            asm += "\tmov ["+p.ident_+"], eax\n";
            env.rbp += 4;
            System.out.println(";init " + env);

            return null;
        }

    }
    





    /* **********************************
     * Expressions
     ***********************************/
    public class ExprVisitor<R,A> implements Expr.Visitor<R,A>
    {
        public R visit(Latte.Absyn.EVar p, A arg)
        {
      /* Code For EVar Goes Here */
            argument = "["+p.ident_+"]";
            asm += "\tmov eax, "+ argument+"\n";
            //p.ident_;

            return null;
        }
        public R visit(Latte.Absyn.ELitInt p, A arg)
        {
      /* Code For ELitInt Goes Here */
            currentNumber = p.integer_;
            argument = currentNumber.toString();
            asm += "\tmov "+arg+", "+ currentNumber+"\n";
            return null;
        }
        public R visit(Latte.Absyn.ELitTrue p, A arg)
        {
      /* Code For ELitTrue Goes Here */

            asm += '\t' + "mov " + arg + ", 1\n" ;
            return null;
        }
        public R visit(Latte.Absyn.ELitFalse p, A arg)
        {
            asm += '\t' + "mov " + arg + ", 0\n" ;
            return null;
        }
        public R visit(Latte.Absyn.EApp p, A arg)
        {
            for (Expr expr : p.listexpr_) {
                expr.accept(new ExprVisitor<R,String>(), "eax");
                asm += "\tmov edi, eax\n";
            }
            asm += "\tcall " + p.ident_ + "\n";

            return null;
        }
        public R visit(Latte.Absyn.EString p, A arg)
        {
            return null;
        }
        public R visit(Latte.Absyn.Neg p, A arg)
        {
            p.expr_.accept(new ExprVisitor<R,String>(), "eax");

            asm += "\tsub eax, " + currentNumber+ " \n";
            asm += "\tsub eax, " + currentNumber+ " \n";
            return null;
        }

        public R visit(Latte.Absyn.Not p, A arg)
        {

            p.expr_.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }

        public R visit(Latte.Absyn.EMul p, A arg)
        {
            p.expr_1.accept(new ExprVisitor<R,A>(), arg);
            p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,A>(), arg);

            return null;
        }
        public R visit(Latte.Absyn.EAdd p, A arg)
        {
      /* Code For EAdd Goes Here */

            p.expr_1.accept(new ExprVisitor<R,String>(), "eax");
            p.addop_.accept(new AddOpVisitor<R,A>(), arg);
            p.expr_2.accept(new ExprVisitor<R,String>(), "edx");
            asm += "\tadd eax, edx\n";

            return null;
        }
        public R visit(Latte.Absyn.ERel p, A arg)
        {
      /* Code For ERel Goes Here */

            p.expr_1.accept(new ExprVisitor<R,String>(), "eax");
            p.expr_2.accept(new ExprVisitor<R,String>(), "edx");

            asm += '\t'+ "cmp eax, edx\n";
            p.relop_.accept(new RelOpVisitor<A>(), arg);
            currentMnemonic = p.relop_.getMnemonic();

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


    
    
    
    

}