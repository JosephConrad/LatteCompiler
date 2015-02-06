package Latte;

import Latte.Absyn.Arg;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.Visitors.BlockVisitor;
import Latte.Visitors.TypeVisitor;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class AsmGenerator
{
    String classname;
    Latte.Absyn.Program program;
    Env env;

    public AsmGenerator(Latte.Absyn.Program program, String string) {
        env = new Env();
        this.program = program;
        this.classname = string.split("\\.")[0];
    }

    void generateASM(){
        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();
        System.out.print("SECTION .bss\n");
        String asm = program.accept(programVisitor, env);
        String data = "SECTION .data\n";
        for (String key : env.strings.keySet()){
            data += "\t" + key + "\tdb\t\"" + env.strings.get(key) + "\", 0\n";
        }
        System.out.println("\n\n\n"+data);
        System.out.println("\n\n\n"+asm);
    }

    public class ProgramVisitor<R,S>  implements Program.Visitor<String,S>
    {
        public String visit(Latte.Absyn.Program p, S args)
        {
            String asm = "SECTION .text\n";
            asm += "\tglobal main\n\n";
            asm += "EXTERN printInt, printString, error, readInt, readString\n\n\n";
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
                return "";
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
            p.type_.accept(new TypeVisitor<Void,Latte.Env>(), arg);
            //p.ident_;
            return null;
        }

    }

}