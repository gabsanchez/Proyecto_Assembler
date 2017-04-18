/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arqui1_assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ByronMorales
 */
public class VM_Traductor {
    
        
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
                    break;
                case "pop": 
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
}
