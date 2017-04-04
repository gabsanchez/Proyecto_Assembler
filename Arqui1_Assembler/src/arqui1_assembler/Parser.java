/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arqui1_assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.nio.file.Files.list;
import static java.rmi.Naming.list;
import java.util.ArrayList;
import java.util.BitSet;
import static java.util.Collections.list;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ByronMorales
 */
public final class Parser {
    
    String path = "";
     Parser(String _path) throws FileNotFoundException{
         path= _path;
         asm_conv(path);
     }
    public void asm_conv(String path) throws FileNotFoundException{
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Integer> index = new ArrayList<>();
        ArrayList<String> bin_list = new ArrayList<>();
        
        //Carga del archivo en una lista
        try (Scanner s = new Scanner(new File(path))) {
            
            while (s.hasNextLine()){
                list.add(s.nextLine());
            }
        }
     
        /*
        for (int i = 0; i < list.size(); i++) {
            list.set(i,list.get(i).trim());
            
            if (!list.get(i).equals("")) {
               String m = list.get(i).substring(0,1);
            
            if (m.equals("/")){
                index.add(i);
                } 
            }
            else{
                index.add(i);
            }
            
            
        }
        */
       
        //Proceso cada linea 
        for (String item : list) {
            if (!item.equals("")) {
                for (int i = 0; i < item.length(); i++) {
                    String m = item.substring(i,1);

                    //Instruccion tipo -A 
                    if (m.equals("@")) { 
                        m= item.substring(1);
                        bin_list.add("0" + String.format("%15s",Integer.toBinaryString(Integer.parseInt(m))).replace(' ', '0'));
                        break;
                    }
                    //Encuentra espacios en blanco
                    else if (m.equals(" ")){

                    }
                    else if (m.equals("/")){
                        break;
                    }

                }
            }
        }
        
    }
    private BitSet Dest(String instruccion)
    {
        BitSet salida = new BitSet(3);//d1, d2, d3 --los indices son 0, 1 y 2
        switch(instruccion)
        {
            case "M":
                salida.set(2);
                break;
            case "D":
                salida.set(1);
                break;
            case "MD":
                salida.set(1);
                salida.set(2);
                break;
            case "A":
                salida.set(0);
                break;
            case "AM":
                salida.set(0);
                salida.set(2);
            case "AD":
                salida.set(0);
                salida.set(1);
            case "AMD":
                salida.set(0);
                salida.set(1);
                salida.set(2);
            default:
                break;
        }
        return salida;
    }
    private BitSet Salto(String instruccion)
    {
        BitSet j = new BitSet(3);
        switch(instruccion)
        {
            case "JGT":
                j.set(2);
                break;
            case "JEQ":
                j.set(1);
                break;
            case "JGE":
                j.set(1);
                j.set(2);
                break;
            case "JLT":
                j.set(0);
                break;
            case "JNE":
                j.set(0);
                j.set(2);
                break;
            case "JLE":
                j.set(0);
                j.set(1);
                break;
            case "JMP":
                j.set(0);
                j.set(1);
                j.set(2);
                break;
            default:
                break;
        }
        return j;
    }
}
