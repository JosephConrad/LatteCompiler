package Latte;

import Latte.Absyn.Arg;
import Latte.Absyn.Program;
import Latte.Absyn.TopDef;
import Latte.BackendASM.BlockVisitor;
import Latte.Exceptions.TypeException;
import Latte.TypeChecker.ProgTypeChecker;

import java.util.Map;

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
    void generateASM() throws TypeException {
        this.env = frontEnd();
        backEnd(this.env);
    }

    /*
     * Frontend function
     */
    Env frontEnd() throws TypeException {
        Env env = program.accept(new ProgTypeChecker(), new Env());
        return env;
    }

    /*
     * Backend function
     */
    void backEnd(Env env) throws TypeException {
        ProgramVisitor<String, Env> programVisitor = new ProgramVisitor<String, Env>();
        String asm = "";
        asm += "SECTION .data\n";
        for (Map.Entry<String, String> entry : env.getStringsMap().entrySet()) {
            asm += "\t" + entry.getValue() + "\tdb\t\"" + entry.getKey() + "\", 0\n";
        }
        asm += program.accept(programVisitor, env);
        System.out.println("\n" + asm);
    }

    /*
     * Program Visitor
     */
    public class ProgramVisitor<R, S> implements Program.Visitor<String, Env> {

        public String visit(Latte.Absyn.Program p, Env env) throws TypeException {
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

        public String visit(Latte.Absyn.FnDef function, Env env) throws TypeException {

            env.beginFunctionASM(function);
            if (env.isPredefinedFunction(function.ident_)) {
                return "";
            }

            int param = 2;
            for (Arg argument: function.listarg_) {
                env.addFunctionParams(argument.ident_, param * 8);
            }
            env.beginBlockASM();

            String asm = "";
            asm += function.ident_ + ":\n";
            asm += "\tpush ebp\n";
            asm += "\tmov ebp, esp\n";
            asm += "\tsub esp, " + (function.localVars + 1) * 4 + "\n";

            asm += function.block_.accept(new BlockVisitor(), env) ;

            asm += "\tadd esp, " + (function.localVars + 1) * 4 + "\n";
            asm += "ret_"+function.ident_ + ":\n";
            asm += "\tleave\n";
            asm += "\tret\n";
            asm += "\n\n";
            env.endBlockASM();
            env.endFunctionASM();
            return asm;
        }
    }
}