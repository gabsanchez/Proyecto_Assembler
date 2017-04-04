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
    
   
}
