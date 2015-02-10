package Latte;

import Latte.Lib.Yylex;
import Latte.Lib.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Run
{
    public static void main(String args[]) throws Exception {
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
        String[] parts = args[0].split("/");
        String[] file = parts[2].split("\\.");
        try
        {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            PrintStream out = new PrintStream(new FileOutputStream(s+ file[0]+".asm"));
            System.setOut(out);
            Latte.Absyn.Program parse_tree = p.pProgram();
            System.out.println("; Nasm - Assembly code generator for Latte");
            System.out.println("; Author: Konrad Lisiecki");
            System.out.println("; Classes: Compilers 2014/15\n");

            AsmGenerator asmGenerator = new AsmGenerator(parse_tree, args[0]);
            asmGenerator.generateASM();
            System.err.println("OK");
            //System.exit(0);
        }
        catch(Throwable e)
        {
            System.err.println("ERROR");
            System.err.println("During executing file: " + args[0]);
           // System.err.println("At line " + String.valueOf(l.line_num()) + ", near \"" + l.buff() + "\" :");
            System.err.println("" + e.getMessage());
            //System.exit(1);
        }
    }
}
