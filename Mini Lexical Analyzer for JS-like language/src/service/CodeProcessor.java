package service;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class which contains methods for grammar validation
 */
public class CodeProcessor {

    private static final String ATOM_FILE = ".\\assets\\atom.properties";
    private Map<String, Integer> atomMap = new HashMap<String, Integer>();
    private FileHandlerService fileHandlerService = new FileHandlerService();
    private Properties prop = new Properties();
    private Hashtable<String, Integer> tsConst = new Hashtable<>();
    private Hashtable<String, Integer> tsId = new Hashtable<>();
    private Integer currentTsConst = 0;
    private Integer currentTsId = 0;
    private List<Pair<String, String>> fip = new ArrayList<>();
    private int currenLineNr = 0;

    /**
     * The class initializes its Atoms from the properties file located in the assets directory.
     *
     * @throws IOException
     */
    public CodeProcessor() throws IOException {
        prop.load(new FileInputStream(ATOM_FILE));
    }

    /**
     * A method which takes a file and analyzes the code, populating the FIP and TSs.
     *
     * @param fileName name of the file to be analyzed
     * @throws IOException
     */
    public void analyzeCode(String fileName) throws IOException {
        List<String> lines = fileHandlerService.readFromFile(fileName);
        System.out.println("Now parsing " + fileName);
        lines.forEach(line -> {
            currenLineNr++;
            List<String> tokens = getTokens(line);
            fixTokens(tokens);
            tokens.forEach(token -> {
                String tokenId = prop.getProperty(token);
                addCorresponding(token, tokenId, currenLineNr);
            });
        });

    }

    /**
     * Handles the conditions and adds the tokens to their respective sets.
     *
     * @param token   token to add
     * @param tokenId tokenID
     * @param lineNr  The line nr. at which the token is located in the file.
     */
    private void addCorresponding(String token, String tokenId, int lineNr) {
        if (tokenId == null) {
            if (isConst(token)) {
                if (!tsConst.containsKey(token)) {
                    tsConst.put(token, currentTsConst);
                    currentTsConst++;
                }
                fip.add(new Pair<>("2", tsConst.get(token).toString()));
            } else {
                if (token.contains(".")) {
                    List<String> ids = Arrays.asList(token.split("\\."));
                    ids.forEach(this::addId);
                } else {
                    addId(token);
                }
            }
        } else {
            fip.add(new Pair<>(tokenId, "-"));
        }
    }

    /**
     * Handles validating and adding the id token in the corresponding sets
     *
     * @param token Id token to handle
     */
    private void addId(String token) {
        if (isValidId(token)) {
            if (!tsId.containsKey(token)) {
                tsId.put(token, currentTsId);
                currentTsId++;
            }
            fip.add(new Pair<>("1", tsId.get(token).toString()));

        } else {
            fip.add(new Pair<>("1", "ID is not valid at line " + currenLineNr));
        }
    }

    /**
     * Checks if the ID token is valid.
     *
     * @param toTest Id token to test
     * @return true or false
     */
    private boolean isValidId(String toTest) {
        return toTest.matches("(^(_|[a-z]|[A-Z])(_|[a-zA-Z.0-9])+$)|(^([a-z]|[A-Z])+$)") && toTest.length() <= 250;
    }

    /**
     * Checks if the const token is valid.
     *
     * @param toTest const token to test
     * @return true or false
     */
    private boolean isConst(String toTest) {
        return toTest.matches("(^[\"'][\\w ]+[\"']$)|(^[0-9]+$)|(^[0-9]+.[0-9]+$)");
    }


    /**
     * Checks the tokens for double operators. (eg ==, >=,...) and fixes them accordingly.
     *
     * @param tokens
     */
    private void fixTokens(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("=")) {
                if (tokens.get(i + 1).equals("=")) {
                    if (tokens.get(i + 2).equals("=")) {
                        tokens.set(i, "===");
                        tokens.remove(i + 1);
                        tokens.remove(i + 1);
                    } else {
                        tokens.set(i, "==");
                        tokens.remove(i + 1);
                    }
                }
            } else if (tokens.get(i).equals("<")) {
                if (tokens.get(i + 1).equals("=")) {
                    tokens.set(i, "<=");
                    tokens.remove(i + 1);
                }
            } else if (tokens.get(i).equals(">")) {
                if (tokens.get(i + 1).equals("=")) {
                    tokens.set(i, ">=");
                    tokens.remove(i + 1);
                }
            }
            if(tokens.get(i).startsWith("\"")&&!tokens.get(i).equals("\"\"")){
               tokens.set(i,tokens.get(i)+" ");
                while(!tokens.get(i).endsWith("\"")){
                    tokens.set(i,tokens.get(i)+" "+tokens.get(i+1));
                    tokens.remove(i+1);
                }

                tokens.set(i, tokens.get(i).replaceAll("( )+", " "));
            }
        }
    }


    /**
     * Parses a line to extract the tokens.
     *
     * @param line string to be parsed
     * @return a list of strings representing the extracted tokens
     */
    private List<String> getTokens(String line) {
        return Arrays
                .asList(line.split("(?<=(===)|[\\<\\>\\,\\;\\=\\-\\+\\/\\*\\(\\)\\[\\] ])|(?=(===)|[\\<\\>\\,\\;\\=\\-\\+\\/\\*\\(\\)\\[\\] ])"))
                .stream()
                .map(word -> {
                    String regex = "^\\s+";
                    return word.replaceAll(regex, "");
                })
                .filter(word -> !word.equals(""))
                .collect(Collectors.toList());
    }

    public Hashtable<String, Integer> getTsConst() {
        return tsConst;
    }

    public void setTsConst(Hashtable<String, Integer> tsConst) {
        this.tsConst = tsConst;
    }

    public Hashtable<String, Integer> getTsId() {
        return tsId;
    }

    public void setTsId(Hashtable<String, Integer> tsId) {
        this.tsId = tsId;
    }

    public List<Pair<String, String>> getFip() {
        return fip;
    }

    public void setFip(List<Pair<String, String>> fip) {
        this.fip = fip;
    }
}
