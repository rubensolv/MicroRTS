/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class ValidatorLines {
    
    
    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        ICompiler compiler = new MainGPCompiler(); 
        String path = "C:\\Users\\Ingeniero Gil\\git\\GAmRTSSOAScale_SG\\GAforMicroRTSSOAScaleSG\\Table\\ScriptsTable.txt";
        
        File file = new File(path);
        String linha = "";
        
        try {
            FileReader arq = new FileReader(file);
            java.io.BufferedReader learArq = new BufferedReader(arq);
            linha = learArq.readLine();
            while (linha != null) {
                linha = linha.substring(linha.indexOf(" "));
                try {
                    List<ICommand> commandsGP = compiler.CompilerCode(linha, utt);
                } catch (Exception e) {
                    System.err.println(linha);
                }
                
                linha = learArq.readLine();
            }

            arq.close();
        } catch (Exception e) {
            System.err.printf("Erro na leitura da linha de configuração");
            System.out.println(e.toString());
        }        

        
        
    }
}
