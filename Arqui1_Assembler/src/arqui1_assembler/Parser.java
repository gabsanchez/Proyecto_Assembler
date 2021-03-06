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
import static java.sql.DriverManager.println;
//import java.io.IOException;
//import static java.nio.file.Files.list;
//import static java.rmi.Naming.list;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
//import static java.util.Collections.list;
//import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JFileChooser;
//import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ByronMorales
 */
public final class Parser {
    
    String path = "";
     Parser(String _path) throws FileNotFoundException, IOException{
         path= _path;
         asm_conv(path);
         String rutaout = path.substring(0, path.length() - 3) + "hack";
         Escribir(rutaout);
     }
     
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<String> labels_ROM = new ArrayList<>();
    ArrayList<String> vars = new ArrayList<>();
    ArrayList<String> bin_list = new ArrayList<>();
    public void asm_conv(String path) throws FileNotFoundException{
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> lines = new ArrayList<>();
        
        //Se carga las etiquetas de ROM
        labels_ROM.add("SCREEN|16384");
        labels_ROM.add("KBD|24576");
        labels_ROM.add("R0|0");
        labels_ROM.add("R1|1");
        labels_ROM.add("R2|2");
        labels_ROM.add("R3|3");
        labels_ROM.add("R4|4");
        labels_ROM.add("R5|5");
        labels_ROM.add("R6|6");
        labels_ROM.add("R7|7");
        labels_ROM.add("R8|8");
        labels_ROM.add("R9|9");
        labels_ROM.add("R10|10");
        labels_ROM.add("R11|11");
        labels_ROM.add("R12|12");
        labels_ROM.add("R13|13");
        labels_ROM.add("R14|14");
        labels_ROM.add("R15|15");
        labels_ROM.add("Temp0|5");
        labels_ROM.add("Temp1|6");
        labels_ROM.add("Temp2|7");
        labels_ROM.add("Temp3|8");
        labels_ROM.add("Temp4|9");
        labels_ROM.add("Temp5|10");
        labels_ROM.add("Temp6|11");
        labels_ROM.add("Temp7|12");
        labels_ROM.add("SP|0");
        labels_ROM.add("LCL|1");
        labels_ROM.add("ARG|2");
        labels_ROM.add("THIS|3");
        labels_ROM.add("THAT|4");
        
        
        //Carga del archivo en una lista
        try (Scanner s = new Scanner(new File(path))) {
            
            while (s.hasNextLine()){
                String aux = s.nextLine();
                aux = aux.trim();
                aux = aux.replace(" ","");
                aux = aux.replace("\t","");
                
                if (!aux.isEmpty()) {
                    list.add(aux);
                }
                
            }
        }
     
        
        //Proceso de eliminacion de lineas que no son instrucciones
        for (String item : list) {
            
            if(!item.startsWith("/")) {
                lines.add(item);
            }    
        }
        
        int x = 0;
        //Proceso de agregar ETIQUETAS
        for (String item : lines) {
           
            if(item.startsWith("(")){
                String n = item.substring(1,item.indexOf(")"));
                labels.add(n + "|" + (x - labels.size()));
            }
            x++;
        }
        
        //Proceso cada linea 
        for (String item : lines) {
            if ((!item.equals("")) &&(!item.startsWith("("))) {
                
                    //Instruccion tipo -A 
                    if (item.startsWith("@")) { 
                        String[] n = item.split("/");
                        String m = n[0].replace("@", "");
                        
                        //@Numero
                        if (isNumeric(m)) {
                            bin_list.add("0" + String.format("%15s",Integer.toBinaryString(Integer.parseInt(m))).replace(' ', '0'));
                        }
                        //@String
                        else{
                            
                            //Verifico que venga una ETIQUETA
                            if (m.equals(m.toUpperCase())) {
                                String nemo = finding_Nemo(m);
                                
                                if (!nemo.equals("")) {
                                    bin_list.add("0" + String.format("%15s",Integer.toBinaryString(Integer.parseInt(nemo))).replace(' ', '0'));    
                                }
                                //No encontro ninguna etiqueta con ese nombre
                                else{
                                    System.out.println(m);
                                }
                                
                            }
                            //Es una variable
                            else{
                                String dory = finding_Dory(m);
                                
                                //Si la variable existe.
                                if (!dory.equals("")) {
                                   bin_list.add("0" + String.format("%15s",Integer.toBinaryString(Integer.parseInt(dory))).replace(' ', '0')); 
                                }
                                //Si la variable no existe la agrego a la lista.
                                else{
                                    vars.add(m);
                                    dory = finding_Dory(m);
                                    bin_list.add("0" + String.format("%15s",Integer.toBinaryString(Integer.parseInt(dory))).replace(' ', '0'));
                                }
                            }
                        }
                        
                        
                        
                    }
                    else{
                        bin_list.add(InstruccionC(item));
                    }
                   
                   

                
            }
        }
        
    }
    
    public String finding_Nemo(String label){
 
        for (int i = 0; i < labels.size(); i++) {
            String m = labels.get(i).substring(0,labels.get(i).indexOf("|"));
            String n = labels.get(i).substring(labels.get(i).indexOf("|") + 1,labels.get(i).length());
            if (label.equals(m)) {                
                return n;
            }
        }
        
        for (int i = 0; i < labels_ROM.size(); i++) {
            String x = labels_ROM.get(i).substring(0,labels_ROM.get(i).indexOf("|"));
            String y = labels_ROM.get(i).substring(labels_ROM.get(i).indexOf("|") + 1,labels_ROM.get(i).length());
            if (label.equals(x)) {
                return y;
            }
        }
        return "";
        
    }
    
    public String finding_Dory(String var){
 
        for (int i = 0; i < vars.size(); i++) {
            
            if (var.equals(vars.get(i))) {                
                return (i + 16) + "";
            }
        }
        
        
        return "";
        
    }
    
    public boolean isNumeric(String s) {  
    return s.matches("[-+]?\\d*\\.?\\d+");  
}  
    
    private String InstruccionC(String Instruccion)
    {
        if (Instruccion.contains("/")) {
            Instruccion = Instruccion.split("/")[0];
        }      
        String Salida = "";
        String[] DCJ= new String[2];
        if (Instruccion.contains("=")) {
            DCJ = Instruccion.split("=");
            String Aux="";
            for (int i = 0; i < 10; i++) {
                Aux= Aux + comp(DCJ[1])[i];
            }
            Salida = Aux + Dest(DCJ[0]) + "000";
        }
        else if (Instruccion.contains(";")) {
            DCJ = Instruccion.split(";");
            String Aux="";
            for (int i = 0; i < 10; i++) {
                Aux= Aux + comp(DCJ[0])[i];
            }
            Salida = Aux +"000" + Salto(DCJ[1]);
        }
        return Salida;
    }
    
    private byte[] comp(String Instruccion)
    {
        byte[] Salida = new byte[10];
        Salida[0] = 1;
        Salida[1] = 1;
        Salida[2] = 1;
        switch (Instruccion) {
            case "0":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 0;
                Salida[6] = 1;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "1":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "-1":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 1;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "D":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 0;
                Salida[9] = 0;
                break;
            case "A":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 0;
                break;
            case "!D":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 0;
                Salida[9] = 1;
                break;
            case "!A":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 1;
                break;
            case "-D":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "-A":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "D+1":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 1;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "A+1":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "D-1":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 1;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 0;
                break; 
            case "A-1":
                Salida[3] = 0;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "D+A":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "D-A":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "A-D":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "D&A":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 0;
                break;
            case "D|A":
                Salida[3] = 0;
                Salida[4] = 0;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 0;
                Salida[9] = 1;
            case "M":
                Salida[3] = 1;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 0;
                break;
            case "!M":
                Salida[3] = 1;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 1;
                break;
            case "-M":
                Salida[3] = 1;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "M+1":
                Salida[3] = 1;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "M-1":
                Salida[3] = 1;
                Salida[4] = 1;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "D+M":
                Salida[3] = 1;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 0;
                break;
            case "D-M":
                Salida[3] = 1;
                Salida[4] = 0;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "M-D":
                Salida[3] = 1;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 1;
                Salida[9] = 1;
                break;
            case "D&M":
                Salida[3] = 1;
                Salida[4] = 0;
                Salida[5] = 0;
                Salida[6] = 0;
                Salida[7] = 0;
                Salida[8] = 0;
                Salida[9] = 0;
                break;
            case "D|M":
                Salida[3] = 1;
                Salida[4] = 0;
                Salida[5] = 1;
                Salida[6] = 0;
                Salida[7] = 1;
                Salida[8] = 0;
                Salida[9] = 1;
            default:
                break;   
        }
        return Salida;
    }
        
    private String Dest(String instruccion)
    {
        String salida;
        switch(instruccion)
        {
            case "M":
                salida = "001";
                break;
            case "D":
                salida = "010";
                break;
            case "MD":
                salida = "011";
                break;
            case "A":
                salida = "100";
                break;
            case "AM":
                salida = "101";
                break;
            case "AD":
                salida = "110";
                break;
            case "AMD":
                salida = "111";
                break;
            default:
                salida = "000";
                break;
        }
        return salida;
    }
    private String Salto(String instruccion)
    {
        String j;
        switch(instruccion)
        {
            case "JGT":
                j = "001";
                break;
            case "JEQ":
                j = "010";
                break;
            case "JGE":
                j = "011";
                break;
            case "JLT":
                j = "100";
                break;
            case "JNE":
                j = "101";
                break;
            case "JLE":
                j = "110";
                break;
            case "JMP":
                j = "111";
                break;
            default:
                j = "000";
                break;
        }

        return j;
    }
    public void Escribir(String nombreCompleto) throws IOException
    {
        FileWriter fw = new FileWriter(nombreCompleto);
        for(String b : bin_list)
        {
            fw.write(b + "\n");
        }
        fw.close();
    }
}
