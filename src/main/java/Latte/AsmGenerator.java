package Latte;

import Latte.Absyn.Arg;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.BackendASM.BlockVisitor;
import Latte.BackendASM.TypeVisitor;
import Latte.TypeChecker.ProgTypeChecker;

import java.util.HashMap;

/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/


public class AsmGenerator {
    String classname;
    Latte.Absyn.Program program;
    Env env;

    public AsmGenerator(Latte.Absyn.Program program, String string) {
        this.program = program;
        this.classname = string.split("\\.")[0];
    }

    /*
     * Run generating ASM code function
     */
    void generateASM() throws Exception {
        this.env = frontEnd();
        //backEnd();
    }

    /*
     * Frontend function
     */
    Env frontEnd() throws Exception {
        System.err.println("dupa frontEnd");
        Env env = program.accept(new ProgTypeChecker(), new Env());

//        Env.functionsReturnAchievibility = new HashMap<String, Boolean>();
//        Env.functionsReturnType = new HashMap<String, String>();
//
//        program.functionsRetType();
//        program.checkTypes(env);
        return env;
    }

    /*
     * Backend function
     */
    void backEnd() throws Exception {

        Env.strings = new HashMap<String, String>();

        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();

        String asm = program.accept(programVisitor, env);

        String data = "SECTION .data\n";
//        for (String key : env. keySet()) {
//            data += "\t" + key + "\tdb\t\"" + env.getLast().strings.get(key) + "\", 0\n";
//        }

//        if (!env.   .strings.isEmpty())
//            System.out.println("\n" + data);
        System.out.println("\n" + asm);
    }

    /*
     * Program Visitor
     */
    public class ProgramVisitor<R, S> implements Program.Visitor<String, Env> {

        public String visit(Latte.Absyn.Program p, Env env) throws Exception {
            String asm = "SECTION .text\n";
            asm += "\tglobal main\n\n";
            asm += "EXTERN printInt, printString, error, readInt, readString, concatenateString\n\n\n";
            for (TopDef x : p.listtopdef_) {
                asm += x.accept(new TopDefVisitor(), env);
            }
            return asm;
        }
    }

    /*
     * Function Definition Visitor
     */
    public class TopDefVisitor implements TopDef.Visitor<String, Env> {

        public String visit(Latte.Absyn.FnDef p, Env env) throws Exception {
            if (env.predefinedFunctions.contains(p.ident_)) {
                return "";
            }

            String prolog = p.ident_ + ":\n";
            prolog += "\tpush rbp\n";
            prolog += "\tmov rbp, rsp\n";

            String asm = "";
            //env.add(new Env(p.ident_));


            p.type_.accept(new TypeVisitor(), env);


            env.ileArgumentow = p.listarg_.size();
            env.localVarShift = (env.ileArgumentow + 1) * 8;

            for (Arg a : p.listarg_) {
                asm += a.accept(new ArgVisitor(), env);
                env.ileArgumentow--;
            }

            Env.ileZmiennych = 0;

            asm += p.block_.accept(new BlockVisitor(), env);

            int shift = env.localVarShift + (Env.ileZmiennych*8);
            Env.ileZmiennych = 0;
            prolog += "\tsub rsp, "+  shift + "\n";

            asm += "\tleave\n";
            asm += "\tret\n";
            asm += "\n\n";
            return prolog+asm;
        }
    }

    /*
     * Argument Visitor
     */
    public class ArgVisitor implements Arg.Visitor<String, Env> {

        public String visit(Latte.Absyn.Arg p, Env env) {

            int shift = 8 * (1 + env.ileArgumentow);
//            if (env.argumentsShifts.containsKey(p.ident_))
//                throw new IllegalArgumentException("Repeated argument name\n");

            env.argumentsShifts.put(p.ident_, shift);
            env.variableType.put(p.ident_, p.type_.toString());

            String asm = p.type_.accept(new TypeVisitor(), env);
            return asm;
        }
    }
}