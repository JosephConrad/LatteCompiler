package Latte;

import Latte.Absyn.Arg;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.BackendASM.BlockVisitor;
import Latte.BackendASM.TypeVisitor;

import java.util.HashMap;
import java.util.LinkedList;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/


public class AsmGenerator {
    String classname;
    Latte.Absyn.Program program;
    
    public AsmGenerator(Latte.Absyn.Program program, String string) {
        this.program = program;
        this.classname = string.split("\\.")[0];
    }
    
    /*
     * Run generating ASM code function
     */
    void generateASM() {
        frontEnd();
        backEnd();
    }
    
    /*
     * Frontend function
     */
    void frontEnd() {        
        LinkedList<Env> envs = new LinkedList<Env>();
        envs.add(new Env("main"));
        
        Env.functionsReturnAchievibility = new HashMap<String, Boolean>();
        Env.functionsReturnType = new HashMap<String, String>();

        program.functionsRetType();
        program.checkTypes(envs);
    }
    
    /*
     * Backend function
     */
    void backEnd() {
        LinkedList<Env> envs = new LinkedList<Env>();
        envs.add(new Env("main"));
        
        Env.strings = new HashMap<String, String>();

        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();
        
        String asm = program.accept(programVisitor, envs);

        String data = "SECTION .data\n";
        for (String key : Env.strings.keySet()) {
            data += "\t" + key + "\tdb\t\"" + envs.getLast().strings.get(key) + "\", 0\n";
        }
        
        if (!Env.strings.isEmpty())
            System.out.println("\n" + data);
        System.out.println("\n" + asm);
    }

    /*
     * Program Visitor
     */
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

    /*
     * Function Definition Visitor
     */
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
            
            for (Arg a : p.listarg_) {
                asm += a.accept(new ArgVisitor(), envs);
                env.ileArgumentow--;
            }

            asm += p.block_.accept(new BlockVisitor(), envs);

            envs.removeLast();

            asm += "\tleave\n";
            asm += "\tret\n";
            asm += "\n\n";
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
            env.variableType.put(p.ident_, p.type_.toString());
            
            String asm = p.type_.accept(new TypeVisitor(), envs);
            return asm;
        }
    }
}