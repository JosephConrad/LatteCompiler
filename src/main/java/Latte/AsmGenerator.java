package Latte;

import Latte.Absyn.*;
import Latte.Visitors.ExprVisitor;
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
        System.out.print("\n\n\n" + bss);
        System.out.println(program.accept(programVisitor, env));
        //System.out.print("\n\n\n"+asm);
    }

    public class ProgramVisitor<R,S>  implements Program.Visitor<String,S>
    {
        public String visit(Latte.Absyn.Program p, S args)
        {
            bss += "section .bss\n";
            String asm = "section .text\n";
            asm += "\tglobal main\n\n";
            asm += "extern printInt, printString, readInt, readString, error, contactString, calloc, malloc\n";
            for (TopDef x : p.listtopdef_) {
                asm += x.accept(new TopDefVisitor(), env);
            }

            return asm;
        }

    }









    public class TopDefVisitor implements TopDef.Visitor<String,Env>
    {
        public String visit(Latte.Absyn.FnDef p, Env arg)
        {
            if (env.predefinedFunctions.contains(p.ident_)) {
                return null;
            }
            String asm = p.ident_+":\n";
            asm += "\tenter 0,0\n";

            p.type_.accept(new TypeVisitor<Void, Env>(), arg);
            for (Arg a: p.listarg_) {
                asm += a.accept(new ArgVisitor(), arg);
            }
            asm += p.block_.accept(new BlockVisitor(), arg);

            asm += "\tleave\n";
            asm += "\tret\n";
            asm += "\n\n\n";

            return asm;
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
    public class BlockVisitor implements Block.Visitor<String,Env>
    {
        public String visit(Latte.Absyn.Block p, Env arg)
        {
      /* Code For Block Goes Here */
            String asm = "";
            for (Stmt x : p.liststmt_) {
                asm += x.accept(new StmtVisitor(), arg);
                asm += "\n";
            }
            return asm;
        }

    }
    
    
    
    /*
     *  Statement VISITOR
     */
    
    
    
    
    public class StmtVisitor implements Stmt.Visitor<String,Env>
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
        public String visit(Latte.Absyn.Decl p, Env arg)
        {
            System.out.println(";Jestesm w deklaracji zmiennych "+ p.type_ + "\n");
            arg.setCurrentType(";" + p.type_.toString());
            p.type_.accept(new TypeVisitor<Void, Env>(), env);
            for (Item x : p.listitem_) {
                x.accept(new ItemVisitor(), arg);
            }
            return null;
        }
        
        // Przypisanie
        public String visit(Latte.Absyn.Ass p, Env arg)
        {
            String asm = p.expr_.accept(new ExprVisitor(), arg);//eax
            //asm += "\tmov eax, "+currentNumber+"\n";
            asm+="\tmov ["+p.ident_+"], eax\n";
            currentNumber = 0;

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
            asm += p.expr_.accept(new ExprVisitor(), arg); //eax
            asm += " AFTER_IF\n\n";

            System.out.println("haha "+p.stmt_.accept(new StmtVisitor(), arg));
            asm += p.stmt_.accept(new StmtVisitor(), arg);
            asm += "AFTER_IF:\n";
            return asm;
        }
        
        // Warunek z elsem
        public String visit(Latte.Absyn.CondElse p, Env arg)
        {
            asm += p.expr_.accept(new ExprVisitor(), arg);//eax
            asm += '\t'+ currentMnemonic + " ELSE\n\n";
            System.out.println("haha "+p.stmt_1.accept(new StmtVisitor(), arg));
            asm += "\tjmp AFTER_IF\n\n";
            asm += "ELSE:\n";
            p.stmt_2.accept(new StmtVisitor(), arg);
            asm += "AFTER_IF:\n";

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

            asm = asm + p.expr_.accept(new ExprVisitor(), arg);
            env.addVariable(p.ident_, env.rbp);
            bss += "\t"+p.ident_+"\t"+ "resd\t1\n";
            asm += "\tmov ["+p.ident_+"], eax\n";
            env.rbp += 4;
            System.out.println(";init " + env);

            return null;
        }

    }


    
    
    

}