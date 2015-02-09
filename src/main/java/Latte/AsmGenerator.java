package Latte;

import Latte.Absyn.Arg;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.Visitors.BlockVisitor;
import Latte.Visitors.TypeVisitor;

import java.util.LinkedList;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class AsmGenerator {
    String classname;
    Latte.Absyn.Program program;


    public AsmGenerator(Latte.Absyn.Program program, String string) {
        this.program = program;
        this.classname = string.split("\\.")[0];
    }

    void generateASM() {
        LinkedList<Env> envs = new LinkedList<Env>();
        envs.add(new Env("main"));
        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();

        program.functionsRetType();
        // Calculate functions return Type
        for (String key: Env.functionsReturnType.keySet()){
            //System.err.println(key + " "+  Env.functionsReturnType.get(key));
        }
       //System.out.print("SECTION .bss\n");
        String asm = program.accept(programVisitor, envs);
        

        String data = "SECTION .data\n";
        for (String key : Env.strings.keySet()) {
            data += "\t" + key + "\tdb\t\"" + envs.getLast().strings.get(key) + "\", 0\n";
        }
        
        // checkReturnAchievibility
        
        
        
        System.out.println("\n\n\n" + data);
        System.out.println("\n\n\n" + asm);
    }

    public class ProgramVisitor<R, S> implements Program.Visitor<String, LinkedList<Env>> {
        public String visit(Latte.Absyn.Program p, LinkedList<Env> envs) {
            String asm = "SECTION .text\n";
            asm += "\tglobal main\n\n";
            asm += "EXTERN printInt, printString, error, readInt, readString, concatenateString\n\n\n";
            for (TopDef x : p.listtopdef_) {
                asm += x.accept(new TopDefVisitor(), envs);
            }
            return asm;
        }
    }


    public class TopDefVisitor implements TopDef.Visitor<String, LinkedList<Env>> {
        public String visit(Latte.Absyn.FnDef p, LinkedList<Env> envs) {
            if (envs.getLast().predefinedFunctions.contains(p.ident_)) {
                return "";
            }
            String asm = p.ident_ + ":\n";
            asm += "\tenter 0,0\n";
            asm += "\tsub rsp, 100\n";

            envs.add(new Env(p.ident_));
            Env env = envs.getLast();

            p.type_.accept(new TypeVisitor(), envs);

            env.ileArgumentow = p.listarg_.size();
            env.localVarShift = (env.ileArgumentow + 1) * 8;
            for (Arg a : p.listarg_) {// ustawiam liczbe argumentow dla kazdego wywolania funkcji
                asm += a.accept(new ArgVisitor(), envs);
                env.ileArgumentow--;
            }


            asm += p.block_.accept(new BlockVisitor(), envs);


            envs.removeLast();

            asm += "\tleave\n";
            asm += "\tret\n";
            asm += "\n\n\n";

            return asm;
        }
    }


    /*
     * Argument Visitor
     */

    public class ArgVisitor implements Arg.Visitor<String, LinkedList<Env>> {
        public String visit(Latte.Absyn.Arg p, LinkedList<Env> envs) {

            Env env = envs.getLast();

            int shift = 8 * (1 + env.ileArgumentow);
            if (env.argumentsShifts.containsKey(p.ident_))
                throw new IllegalArgumentException("Repeated argument name\n");
            env.argumentsShifts.put(p.ident_, shift);
            
            
            //System.out.print("\t" + p.ident_ + "\t" + "resq\t1\n");
            //String asm = "\tmov rax, [rbp+" + shift + "]\n";


            //asm += "\tmov [rbp-" + env.localVarShift + "], rax\n";
            //env.variableShifts.put(p.ident_, env.localVarShift);
            //env.localVarShift -= 8;

            String asm = p.type_.accept(new TypeVisitor(), envs);
            //p.ident_;
            return asm;
        }
    }
}