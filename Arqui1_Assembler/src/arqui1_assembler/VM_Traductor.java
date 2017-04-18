/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arqui1_assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ByronMorales
 */
public class VM_Traductor {
    
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
                    break;        
                case "sub": 
                    break;
                case "neg": 
                    break;
                case "eq": 
                    break;
                case "lt": 
                    break;        
                case "gt": 
                    break;
                case "and": 
                    break;
                case "or": 
                    break;
                case "not": 
                    break;        
                case "label": 
                    break;
                case "goto": 
                    break;
                case "if-goto": 
                    break;
                case "call": 
                    break;        
                case "function": 
                    break;
                case "return": 
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
        Contenido[2] = Comandos;
        if (Contenido[1].equals("constant")) //Obtener el valor del numero y guardarlo en D.
        {
            InstruccionesASM.add("@"+Contenido[2]);
            InstruccionesASM.add("D=A");
        }
        else if (Contenido[1].equals("local")||Contenido[1].equals("argument")||Contenido[1].equals("this")||Contenido[1].equals("that")) 
        { 
            String PR = "";
            switch(Contenido[1])
            {
                case "local":
                    PR="LCL";
                    break;
                case "argument":
                    PR="ARG";
                    break;
                case "this":
                    PR="THIS";
                    break;
                case "that":
                    PR="THAT";
                    break;
                default:
                    break;
            }
            InstruccionesASM.add("@"+PR);
            InstruccionesASM.add("D=A");
            InstruccionesASM.add("@"+Contenido[2]);
            InstruccionesASM.add("D=D+M");
        }
        else if (Contenido[1].equals("pointer")||Contenido[1].equals("temp"))
        {
            int Base=0;
            switch(Contenido[1])
            {
                case "pointer":
                    Base=3;
                    break;
                case "temp":
                    Base=5;
                    break;
                default:
                    break;
            }
            InstruccionesASM.add("@"+Base);
            InstruccionesASM.add("D=A");
            InstruccionesASM.add("@"+Contenido[2]);
            InstruccionesASM.add("D=D+A");
        }
        else if (Contenido[1].equals("static")) {
            InstruccionesASM.add("@var"+Contenido[2]);
            InstruccionesASM.add("D=M");
        }
        
        if (Contenido[0].equals("push")) 
        {
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("M=D");
            InstruccionesASM.add("D=A+1");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("M=D");
        }
        else if (Contenido[0].equals("pop")) 
        {
            InstruccionesASM.add("@R13");
            InstruccionesASM.add("M=D");
            InstruccionesASM.add("@SP");
            InstruccionesASM.add("A=M-1");
            InstruccionesASM.add("D=M");
            InstruccionesASM.add("@R13");
            InstruccionesASM.add("A=M");
            InstruccionesASM.add("M=D");
        }
    }


public void arithmetic(String word){
    
    word = word.trim();
    
    if ((word.equals("add"))||(word.equals("sub"))||(word.equals("and"))||(word.equals("or"))) {
       
        if (word.equals("add")) {
            word = "add";
        }
        else if (word.equals("sub")){
            word = "sub";
        }
        
    }
    else if ((word.equals("neg"))||(word.equals("not"))){
        
    }
    else if ((word.equals("eq"))||(word.equals("lt"))||(word.equals("gt"))){
        
    }
    
        
    
} 
















}
