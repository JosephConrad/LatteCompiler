package Latte;

import Latte.Lib.Yylex;
import Latte.Lib.parser;

import java.io.*;

public class Run
{
    public static void main(String args[]) throws Exception {
        Yylex l = null;
        parser p = new parser();
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
            String[] fileName = parts[2].split("\\.");
            File file = new File(args[0]);
            
            String absolutePath = file.getAbsolutePath();
            String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
            PrintStream out = new PrintStream(new FileOutputStream(filePath+File.separator+fileName[0]+".s"));
            System.setOut(out);
           // Latte.Absyn.Program parse_tree = p.pProgram();

            System.out.println("; Nasm - Assembly code generator for Latte");
            System.out.println("; Author: Konrad Lisiecki");
            System.out.println("; Classes: Compilers 2014/15\n");

            Latte.Absyn.Program parse_tree = p.pProgram();
            AsmGenerator asmGenerator = new AsmGenerator(parse_tree, args[0]);
            asmGenerator.generateASM();
            System.err.println("OK");
            Runtime rt = Runtime.getRuntime();

            String objectFile = filePath + File.separator + "lat.o";
            String assFile = filePath + File.separator + fileName[0] + ".s";
            String execFile = filePath+File.separator+fileName[0];
            Process pr = rt.exec("nasm -g -f elf64 -o " + objectFile + "  " + assFile);
            Process pr1 = rt.exec("gcc -o  " + execFile + " -Wall -g "+ objectFile + "  lib/runtime.o");
            //System.exit(0);
        }
        catch(Throwable e)
        {
            System.err.println("ERROR");
            System.err.println("During executing file: " + args[0]);
            System.err.println("" + e.getMessage());
            if (e.getClass() == Exception.class)    
                System.err.println("\tAt line " + String.valueOf(l.line_num()) + ", near \"" + l.buff() + "\".");
            System.err.println("\n\n");
            //System.exit(1);
        }
    }


}
