package Latte;

import Latte.Lib.PrettyPrinter;
import Latte.Lib.Yylex;
import Latte.Lib.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

public class Run
{
    public static void main(String args[]) throws Exception
    {
        Yylex l = null;
        parser p;
        try
        {
            if (args.length == 0) l = new Yylex(System.in);
            else l = new Yylex(new FileReader(args[0]));
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Error: File not found: " + args[0]);
            System.exit(1);
        }
        p = new parser(l);
        try
        {
            String[] parts = args[0].split("/");
            String[] file = parts[2].split("\\.");
            PrintStream out = new PrintStream(new FileOutputStream("/Users/konrad/Dropbox/09_semestr/mrjp/LatteCompiler/tests/out/"+ file[0]+".asm"));
            System.setOut(out);
            Latte.Absyn.Program parse_tree = p.pProgram();
            System.out.print("; ");
            System.out.println(PrettyPrinter.show(parse_tree));
            System.out.println("; Nasm - Asembly code generator for Latte");
            System.out.println("; Author: Konrad Lisiecki");
            System.out.println("; Classes: Compilers 2014/15\n\n");
            //System.out.println(PrettyPrinter.print(parse_tree));

            AsmGenerator asmGenerator = new AsmGenerator(parse_tree, args[0]);
            asmGenerator.generateASM();
            System.err.println("OK");
            System.exit(0);
        }
        catch(Throwable e)
        {
            System.err.println("ERROR");
            System.err.println("At line " + String.valueOf(l.line_num()) + ", near \"" + l.buff() + "\" :");
            System.err.println("     " + e.getMessage());
            System.exit(1);
        }
    }
}
