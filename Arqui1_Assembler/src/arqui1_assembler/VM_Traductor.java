/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arqui1_assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ByronMorales
 */
public class VM_Traductor {
    
    String path = "";
    VM_Traductor(String ruta) throws FileNotFoundException, IOException{
        path = ruta;
        vm_conv(ruta);
        String rutaout = path.substring(0, path.length() - 2) + "asm";
        Escribir(rutaout);
        
    }
    public List<String> InstruccionesASM = new ArrayList();
        
    public void vm_conv(String path) throws FileNotFoundException{
    
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> lines = new ArrayList<>();
        
        //Carga del archivo en una lista
        try (Scanner s = new Scanner(new File(path))) {
            
            while (s.hasNextLine()){
                String aux = s.nextLine();
                aux = aux.trim();
                if (!aux.isEmpty()) {
                    list.add(aux);
                }
                
            }
        }
        
        //Proceso de eliminacion de lineas que no son instrucciones
        for (String item : list) {
            
            if((!item.startsWith("/"))||(!item.startsWith(""))) {
                lines.add(item);
            }    
        }
        
        
        //Proceso cada linea 
        for (String item : lines) {
         
            String[] split = item.split(" ");
            
            
            switch (split[0])
            {
                case "push":
                    PushnPop(item);
                    break;
                case "pop":
                    PushnPop(item);
                    break;
                case "add":
                    arithmetic(item);
                    break;        
                case "sub":
                    arithmetic(item);
                    break;
                case "neg":
                    arithmetic(item);
                    break;
                case "eq":
                    arithmetic(item);
                    break;
                case "lt":
                    arithmetic(item);
                    break;        
                case "gt":
                    arithmetic(item);
                    break;
                case "and":
                    arithmetic(item);
                    break;
                case "or":
                    arithmetic(item);
                    break;
                case "not":
                    arithmetic(item);
                    break;        
                case "label": 
                    Label(item);
                    break;
                case "goto": 
                    Goto(item);
                    break;
                case "if-goto": 
                    If(item);
                    break;
                case "call": 
                    Call(item);
                    break;        
                case "function": 
                    Function(item);
                    break;
                case "return": 
                    Return();
                    break;
                default:
                    break;
            }  
        }
    }
    
    public void PushnPop(String Instruccion)
    { 
        String[] Contenido = new String[3];
        String Comandos="";
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Comandos = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        Contenido[1] = Comandos.substring(0, Comandos.indexOf(" "));
        Comandos = Comandos.substring(Comandos.indexOf(" "), Comandos.length()).trim();
        Contenido[2] = Comandos.trim();
        if (Contenido[0].equals("push")) {
            switch(Contenido[1])
            {
                case "local":
                    InstruccionesASM.add("@LCL");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "argument":
                    InstruccionesASM.add("@ARG");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "this":
                    InstruccionesASM.add("@THIS");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "that":
                    InstruccionesASM.add("@THAT");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "pointer":
                {
                    if (Contenido[2].equals("0")) {
                        InstruccionesASM.add("@THIS");
                        InstruccionesASM.add("D=M");
                        Push();
                    }
                    else { 
                        InstruccionesASM.add("@THIS");
                        InstruccionesASM.add("D=M");
                        Push();
                    }
                    break;
                }
                case "constant":
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("D=A");
                    Push();
                    break;
                case "static":
                    InstruccionesASM.add("@var"+Contenido[2]);
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "temp":
                   InstruccionesASM.add("@R5");
                   InstruccionesASM.add("D=A");
                   InstruccionesASM.add("@"+Contenido[2]);
                   InstruccionesASM.add("A=D+A");
                   InstruccionesASM.add("D=M");
                   Push();
                   break;
                default:
                    break;
            }
        }
        else if (Contenido[0].equals("pop")) {
            switch(Contenido[1])
            {
                case "local":
                    InstruccionesASM.add("@LCL");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("D=D+A");
                    Push();
                    break;
                case "argument":
                    InstruccionesASM.add("@ARG");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "this":
                    InstruccionesASM.add("@THIS");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "that":
                    InstruccionesASM.add("@THAT");
                    InstruccionesASM.add("D=M");
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("A=D+A");
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "pointer":
                {
                    if (Contenido[2].equals("0")) {
                        InstruccionesASM.add("@THIS");
                        InstruccionesASM.add("D=M");
                        Push();
                    }
                    else { 
                        InstruccionesASM.add("@THIS");
                        InstruccionesASM.add("D=M");
                        Push();
                    }
                    break;
                }
                case "constant":
                    InstruccionesASM.add("@"+Contenido[2]);
                    InstruccionesASM.add("D=A");
                    Push();
                    break;
                case "static":
                    InstruccionesASM.add("@var"+Contenido[2]);
                    InstruccionesASM.add("D=M");
                    Push();
                    break;
                case "temp":
                   InstruccionesASM.add("@R5");
                   InstruccionesASM.add("D=A");
                   InstruccionesASM.add("@"+Contenido[2]);
                   InstruccionesASM.add("A=D+A");
                   InstruccionesASM.add("D=M");
                   Push();
                   break;
                default:
                    break;
            }
        }
    }

    public void Push()
    {
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("A=M");
        InstruccionesASM.add("M=D");
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("M=M+1");
    }
    
    public void Pop()
    { 
        InstruccionesASM.add("@R13");
        InstruccionesASM.add("M=D");
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("AM=M-1");
        InstruccionesASM.add("D=M");
        InstruccionesASM.add("@R13");
        InstruccionesASM.add("A=M");
        InstruccionesASM.add("M=D");
    }
    
    int eqs = 1;
    public void arithmetic(String word){

        word = word.trim();

        if ((word.equals("add"))||(word.equals("sub"))||(word.equals("and"))||(word.equals("or"))) {

            if (word.equals("add")) {
                word = "D=D+M";

            }
            else if (word.equals("sub")){
                word = "D=M-D";
            }
            else if (word.equals("and")){
                word = "D=D&M";
            }
            else if (word.equals("or")){
                word = "D=D|M";
            }

            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=M-1");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("D=M");
            InstruccionesASM.add("A=A-1");
            InstruccionesASM.add(word);
            InstruccionesASM.add("M=D");
            InstruccionesASM.add("D=A+1");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=D");

        }
        else if ((word.equals("neg"))||(word.equals("not"))){

            if (word.equals("neg")) {
                word = "M=-M";

            }
            else if (word.equals("not")){
                word = "M=!M";
            }
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=M-1");
            InstruccionesASM.add("A=M"); 
            InstruccionesASM.add(word);
            InstruccionesASM.add("D=A+1");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=D");
        }
        else if ((word.equals("eq"))||(word.equals("lt"))||(word.equals("gt"))){

            if (word.equals("eq")) {
                word = "D=D+M";

            }
            else if (word.equals("lt")){
                word = "D=M-D";
            }
            else if (word.equals("gt")){
                word = "D=D&M";
            }
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=M-1");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("D=M");
            InstruccionesASM.add("A=A-1");
            InstruccionesASM.add("D=M-D");
            InstruccionesASM.add("@COMP" + eqs);
            InstruccionesASM.add(word);
            InstruccionesASM.add("@0");
            InstruccionesASM.add("D=-A");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("M=D");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=M+1");
            InstruccionesASM.add("@FCOMP" + eqs);
            InstruccionesASM.add("0;JMP");
            InstruccionesASM.add("COMP" + eqs);
            InstruccionesASM.add("@1");
            InstruccionesASM.add("D=A");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("M=D");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=M+1");
            InstruccionesASM.add("FCOMP" + eqs);

            eqs++;
        }



    } 

    public void Label(String Instruccion){
        String[] Contenido = new String[2];
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Contenido[1] = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        InstruccionesASM.add("("+Contenido[1].toUpperCase()+")");
    }

    public void Goto(String Instruccion)
    {
        String[] Contenido = new String[2];
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Contenido[1] = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        InstruccionesASM.add("@"+Contenido[1].toUpperCase());
        InstruccionesASM.add("0;JMP");

    }
    
    public void If(String Instruccion)
    { 
        String[] Contenido = new String[2];
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Contenido[1] = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("A=M");
        InstruccionesASM.add("D=M");
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("M=M-1");
        InstruccionesASM.add("0;JMP");
        InstruccionesASM.add("@"+Contenido[1].toUpperCase());
        InstruccionesASM.add("D;JMP");   
    }

    public void Call(String Instruccion)
    { 
        String[] Contenido = new String[3];
        String Comandos="";
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Comandos = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        Contenido[1] = Comandos.substring(0, Comandos.indexOf(" "));
        Comandos = Comandos.substring(Comandos.indexOf(" "), Comandos.length()).trim();
        Contenido[2] = Comandos;
        InstruccionesASM.add("@RETURN"+Contenido[1].toUpperCase());
        SubirSP();
        InstruccionesASM.add("@LCL");
        SubirSP();
        InstruccionesASM.add("@ARG");
        SubirSP();
        InstruccionesASM.add("@THIS");
        SubirSP();
        InstruccionesASM.add("@THAT");
        SubirSP();
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("D=M");
        InstruccionesASM.add("@ARG");
        InstruccionesASM.add("D=D-"+Contenido[2]);
        InstruccionesASM.add("M=D-5");
        InstruccionesASM.add("@LCL");
        InstruccionesASM.add("M=D");
        Goto("goto "+Contenido[1]);
        InstruccionesASM.add("(RETURN"+Contenido[1].toUpperCase()+")");
    }
    
    public void SubirSP()
    {
        InstruccionesASM.add("D=A");
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("M=A");
        InstruccionesASM.add("M=D");
        InstruccionesASM.add("@SP");
        InstruccionesASM.add("M=M+1");
    }
    
    public void Function(String Instruccion)
    { 
        String[] Contenido = new String[3];
        String Comandos="";
        Contenido[0] = Instruccion.substring(0, Instruccion.indexOf(" "));
        Comandos = Instruccion.substring(Instruccion.indexOf(" "), Instruccion.length()).trim();
        Contenido[1] = Comandos.substring(0, Comandos.indexOf(" "));
        Comandos = Comandos.substring(Comandos.indexOf(" "), Comandos.length()).trim();
        Contenido[2] = Comandos;
        InstruccionesASM.add("("+Contenido[1].toUpperCase()+")");
        for (int i = 0; i < Integer.parseInt(Contenido[2]); i++) {
            InstruccionesASM.add("@0");
            SubirSP();
        }
    }
    
    public void Return()
    {
        InstruccionesASM.add("@LCL");
        InstruccionesASM.add("D=M");
        InstruccionesASM.add("@R13");
        InstruccionesASM.add("M=D");	
        InstruccionesASM.add("D=M-5");
        InstruccionesASM.add("@R14");
        InstruccionesASM.add("M=D\n");	
        InstruccionesASM.add("@SP\n");
        InstruccionesASM.add("M=A\n");
        InstruccionesASM.add("D=M\n");
        InstruccionesASM.add("@ARG\n");
        InstruccionesASM.add("A=M\n");
        InstruccionesASM.add("M=D\n");	
        InstruccionesASM.add("@ARG\n");
        InstruccionesASM.add("D=M\n");
        InstruccionesASM.add("@SP\n");
        InstruccionesASM.add("M=A\n");
        InstruccionesASM.add("M=D+1\n");		
        InstruccionesASM.add("@R13\n");
        InstruccionesASM.add("D=M\n");
        InstruccionesASM.add("@THAT\n");	
        InstruccionesASM.add("M=D-1\n");
        InstruccionesASM.add("@THIS\n");
        InstruccionesASM.add("M=D-2\n");	
        InstruccionesASM.add("@ARG\n");
        InstruccionesASM.add("M=D-3\n");	
        InstruccionesASM.add("@LCL\n");
        InstruccionesASM.add("M=D-4\n");	
        InstruccionesASM.add("@R14\n");
        InstruccionesASM.add("D=M\n");
        InstruccionesASM.add("@D\n");	
        InstruccionesASM.add("0;JMP\n");
    }
    
    public void Escribir(String nombreCompleto) throws IOException
    {
        FileWriter fw = new FileWriter(nombreCompleto);
        for(String b : InstruccionesASM)
        {
            fw.write(b + "\n");
        }
        fw.close();
    }
}