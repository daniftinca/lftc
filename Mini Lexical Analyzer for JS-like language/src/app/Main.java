package app;

import javafx.util.Pair;
import service.CodeProcessor;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * Main part of the program
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Program Start...");
        try {
            CodeProcessor codeProcessor = new CodeProcessor();
            codeProcessor.analyzeCode(".\\assets\\cerc.js");
            Hashtable<String, Integer> codeProcessorTsConst = codeProcessor.getTsConst();
            Hashtable<String, Integer> codeProcessorTsId = codeProcessor.getTsId();
            List<Pair<String, String>> codeProcessorFip = codeProcessor.getFip();
            System.out.println("TSConst: ");
            System.out.println(codeProcessorTsConst.toString());
            System.out.println("TSId: ");
            System.out.println(codeProcessorTsId.toString());
            System.out.println("Fip: ");
            codeProcessorFip.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
