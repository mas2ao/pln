/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pln;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author shaolin
 */
public class Article {
    private String originalText;
    private HashMap<String, Integer> allWords;  //palavra+numero repet
    private HashMap<String, Integer> nonSW;  //palavra + numero sem stopword
    private ArrayList<Palavra> sortedWords;
    private ArrayList<Palavra> stopWords; //stop words
    private String refe;
    private int numWord;
    private ArrayList<Palavra> top10;
    private ArrayList<String> references;
    private ArrayList<String> contributions;
    private ArrayList<String> method;
    private ArrayList<String> problem;
    private ArrayList<String> objective;
    private ArrayList<String> author;
    private ArrayList<String> adress;
    

    public Article(String originalText) {
        this.originalText = originalText.toLowerCase();
        this.allWords = new HashMap<>();
        this.allWords = new HashMap<>();
        this.sortedWords = new ArrayList<>();
        this.stopWords = new ArrayList<>();
        this.refe = null;
        this.numWord = 0;
        this.top10 = new ArrayList<>();
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public HashMap<String, Integer> getAllWords() {
        return allWords;
    }

    public void setAllWords(HashMap<String, Integer> allWords) {
        this.allWords = allWords;
    }

    public ArrayList<Palavra> getSortedWords() {
        return sortedWords;
    }

    public void setSortedWords(ArrayList<Palavra> sortedWords) {
        this.sortedWords = sortedWords;
    }

    public ArrayList<Palavra> getStopWords() {
        return stopWords;
    }

    public void setStopWords(ArrayList<Palavra> stopWords) {
        this.stopWords = stopWords;
    }

    public int getNumWord() {
        return numWord;
    }

    public void setNumWord(int numWord) {
        this.numWord = numWord;
    }

    public HashMap<String, Integer> getNonSW() {
        return nonSW;
    }

    public void setNonSW(HashMap<String, Integer> nonSW) {
        this.nonSW = nonSW;
    }

    public ArrayList<Palavra> getTop10() {
        return top10;
    }

    public void setTop10(ArrayList<Palavra> top10) {
        this.top10 = top10;
    }

    public ArrayList<String> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<String> references) {
        this.references = references;
    }

    public ArrayList<String> getContributions() {
        return contributions;
    }

    public void setContributions(ArrayList<String> contributions) {
        this.contributions = contributions;
    }

    public ArrayList<String> getMethod() {
        return method;
    }

    public void setMethod(ArrayList<String> method) {
        this.method = method;
    }

    public ArrayList<String> getProblem() {
        return problem;
    }

    public void setProblem(ArrayList<String> problem) {
        this.problem = problem;
    }

    public ArrayList<String> getObjective() {
        return objective;
    }

    public void setObjective(ArrayList<String> objective) {
        this.objective = objective;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }

    public ArrayList<String> getAdress() {
        return adress;
    }

    public void setAdress(ArrayList<String> adress) {
        this.adress = adress;
    }

    public String getRefe() {
        return refe;
    }

    public void setRefe(String refe) {
        this.refe = refe;
    }
    
    
    
}
