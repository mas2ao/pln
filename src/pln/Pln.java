/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pln;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author shaolin
 */
public class Pln {

    /**
     * @param args the command line arguments
     */
    public static HashMap<String, Integer> allSW = new HashMap<>();

    static String pdftoText(String fileName) {
        PDFParser parser;
        String parsedText = null;;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File(fileName);
        if (!file.isFile()) {
            System.err.println("File " + fileName + " does not exist.");
            return null;
        }
        try {
            parser = new PDFParser(new FileInputStream(file));
        } catch (IOException e) {
            System.err.println("Unable to open PDF Parser. " + e.getMessage());
            return null;
        }
        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            System.err
                    .println("An exception occured in parsing the PDF Document."
                            + e.getMessage());
        } finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                if (pdDoc != null) {
                    pdDoc.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parsedText;
    }

    public static HashMap<String, Integer> countWord(String text) {
        HashMap<String, Integer> biblioteca = new HashMap<>();
        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9\\s]", " ");
        text = text.replaceAll("[\\n]", " ");
        text = text.replaceAll("[\\s][\\s]*", " ");

        String[] palavras = text.split(" ");
        for (String p : palavras) {
            int quant = 1;
            if (biblioteca.containsKey(p)) {
                quant = biblioteca.get(p);
                quant++;
            }
            biblioteca.put(p, quant);
            quant = 1;
            if (allSW.containsKey(p)) {
                quant = allSW.get(p);
                quant++;
            }
            allSW.put(p, quant);
        }
//        for (String p: biblioteca.keySet()){
//            System.out.println(p+" -> "+biblioteca.get(p));
//        }
        return biblioteca;
    }

    public static HashMap<String, Integer> removeSmall(HashMap<String, Integer> biblioteca) {
        ArrayList<String> small = new ArrayList<>();
        for (String p : biblioteca.keySet()) {
            if (p.length() < 3) {
                small.add(p);
            }
        }
        for (String p : small) {
            biblioteca.remove(p);
        }

        return biblioteca;
    }

    public static ArrayList<Palavra> ordWord(HashMap<String, Integer> biblioteca) {
        String palavra_idx = "";
        ArrayList<Palavra> sorted = new ArrayList<>();
        while (!biblioteca.isEmpty()) {
            int maior = 0;
            for (String p : biblioteca.keySet()) {
                if (biblioteca.get(p) > maior) {
                    maior = biblioteca.get(p);
                    palavra_idx = p;
                }
            }
            Palavra w = new Palavra(palavra_idx, maior);
            sorted.add(w);
            biblioteca.remove(palavra_idx);
        }
        return sorted;
    }

    public static ArrayList<Palavra> top10(ArrayList<Palavra> sorted) {
        ArrayList<Palavra> top10 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            top10.add(sorted.get(i));
        }
        return top10;
    }

    public static String removeSW(ArrayList<String> stopWords, String text) {
        for (String sw : stopWords) {
//            System.out.println("        removeu "+sw);
            text = text.replaceAll("\\b" + sw + "\\b", "");
        }
        return text;
    }

    public static Article removeRefs(String text) {
        String word = "References";
        String refs = null;
        int indice = 0;
        indice = text.lastIndexOf(word);
        if (indice == -1) {
            word = "Reference";
            indice = text.lastIndexOf(word);
            if (indice == -1) {
                word = "R E F E R E N C E S";
                indice = text.lastIndexOf(word);
            }
        }
        if (indice == -1) {
            refs = "no refs";
        } else {
            refs = text.substring(indice);
            text = text.substring(0, indice);
        }
        Article art = new Article(text);
        art.setRefe(refs);

        return art;
    }

    public static ArrayList<String> getStatements(String pdf) {
        ArrayList<String> st = new ArrayList();

        int start = 0;
        for (int i = 0; i < pdf.length(); i++) {
            if (pdf.charAt(i) == '.') {
                st.add(pdf.substring(start, i));
                start = i + 1;
            }
        }
        st.add(pdf.substring(start, pdf.length() - 1));

        return st;
    }

    public static ArrayList<String> findAuthors(String txt) {
        String namePattern = "([A-Z][a-z]+)((\\s*)(([A-Z](\\.|[a-z]*))|(de|o)\\s[A-Z](\\.|[a-z]*)))+";
        String pattern = "\\A" + namePattern
                + "((\\sand|,)\\s" + namePattern + ")*\\z";

        String pdf[] = txt.split("\n");

        ArrayList<String> r = new ArrayList<>();

        Pattern p = Pattern.compile(pattern);
        for (String s : pdf) {
            s = s.replaceAll("[^A-Za-z0-9\\.]", " ").replaceAll("\\s+", " ");
//            System.out.println(s);
            Matcher match = p.matcher(s);
            while (match.find() && match.end() - match.start() > 8) {
//                System.out.println("------------------------");
//                System.out.println(s);
                r.add(s);
            }
        }

        return r;
    }

    public static ArrayList<String> findAddress(ArrayList<String> pdf) {
        String universityPattern = "(.*)[Uu]niversity(.*)";
        String departmentPattern = "(.*)[Dd]epartment(.*)";
        String corporationPattern = "(.*)[Cc]orporation(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + universityPattern + "|" + departmentPattern + "|"
                + corporationPattern + ")";

        Pattern p = Pattern.compile(pattern);

        for (String s : pdf) {
//            System.out.println(s);
            Matcher match = p.matcher(s);
            if (match.find()) {
//                System.out.println("------------------------");
                r.add(s);
//                System.out.println(s);
            }
        }
        return r;
    }

    public static ArrayList<String> findObjective(ArrayList<String> pdf) {
        String patternOne = "(.*)[Pp]urpose(.*)";
        String patternTwo = "(.*)[Oo]bjective(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + patternOne + "|" + patternTwo + ")";

        Pattern p = Pattern.compile(pattern);

        for (String s : pdf) {
            Matcher match = p.matcher(s);
            if (match.find()) {
//                System.out.println("------------------------");
                r.add(s);
//                System.out.println(s);
            }
        }
        return r;
    }

    public static ArrayList<String> findProblem(ArrayList<String> pdf) {
        String patternOne = "(.*)[Pp]roblem(.*)";
        String patternTwo = "(.*)[Oo]vercomes(.*)";
        String patternThree = "(.*)[Ss]olve(.*)";
        String patternFour = "(.*)[Ss]olution(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + patternOne + "|" + patternTwo + "|"
                + patternThree + "|" + patternFour + ")";

        Pattern p = Pattern.compile(pattern);

        for (String s : pdf) {
            Matcher match = p.matcher(s);
            if (match.find()) {
//                System.out.println("------------------------");
                r.add(s);
//                System.out.println(s);
            }
        }
        return r;
    }

    public static ArrayList<String> findMethod(ArrayList<String> pdf) {
        String patternOne = "(.*)[Mm]ethodology(.*)";
        String patternTwo = "(.*)[Aa]nalisys(.*)";
        String patternThree = "(.*)[Aa]nalise(.*)";
        String patternFour = "(.*)[Cc]ontent analisys(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + patternOne + "|" + patternTwo + "|"
                + patternThree + "|" + patternFour + ")";

        Pattern p = Pattern.compile(pattern);

        for (String s : pdf) {
            Matcher match = p.matcher(s);
            if (match.find()) {
//                System.out.println("------------------------");
                r.add(s);
//                System.out.println(s);
            }
        }
        return r;
    }

    public static ArrayList<String> findContribution(ArrayList<String> pdf) {
        String patternOne = "(.*)[Cc]ontributes to(.*)";
        String patternTwo = "(.*)[Hh]elps(.*)";
        String patternThree = "(.*)[Ss]olves(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + patternOne + "|" + patternTwo + "|"
                + patternThree + ")";

        Pattern p = Pattern.compile(pattern);

        for (String s : pdf) {
            Matcher match = p.matcher(s);
            if (match.find()) {
//                System.out.println("------------------------");
                r.add(s);
//                System.out.println(s);
            }
        }
        return r;
    }

    public static ArrayList<String> extractReferences(String pdf) {
        String patternOne = "(.*)\\([0-9]*\\),(\\s*)“(.*)”,.*";
        String patternTwo = "(.*)\\([0-9]*\\),(\\s*)“(.*)\\n(.*)”,(.*)\\.";
        String patternThree = "(.*),(\\s*)[0-9]*,(\\s*)(.*),(.*)";
        String patternFour = "(.*),(\\s*)[0-9]*,(\\s*)(.*)\\n(.*),";
        String patternFive = "(.*)\\[[0-9]*\\](\\s*)[A-Z][a-z]+(.*)";
        String patternSix = "(.*),(\\s*)[0-9]*(\\s*)\\.(.*)";
        String patternSeven = "(.*)(\\s*)\\([0-9]{4}\\)(\\s*)(.*)";

        ArrayList<String> r = new ArrayList<>();

        String pattern = "(" + patternOne + "|" + patternTwo + "|"
                + patternThree + "|" + patternFour + "|" + patternFive + "|"
                + patternSix + "|" + patternSeven + ")";

        Pattern p = Pattern.compile(pattern);
//        System.out.println(pdf);
        Matcher match = p.matcher(pdf);
        while (match.find()) {
//            System.out.println("------------------------");
            r.add(pdf.substring(match.start(), match.end()));
//            System.out.println(pdf.substring(match.start(), match.end()));
        }

        return r;
    }

    public static void main(String args[]) {
        ArrayList<String> defaultSW = new ArrayList<>();
        HashMap<String, Article> artigos = new HashMap<>();
        ArrayList<HashMap<String, Integer>> num_words = new ArrayList<>();

        File arq = new File("defaultSW.txt");
        FileReader fr;
        try {
            fr = new FileReader(arq);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                String linha = br.readLine();
                defaultSW.add(linha);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pln.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Pln.class.getName()).log(Level.SEVERE, null, ex);
        }

        File dir = new File("Corpus");
        String[] children = dir.list();
        int contador = 0;
        for (int i = 0; i < children.length; i++) {
//            if (!children[i].equals("92.pdf")) continue;
            System.out.println("Reading file " + children[i]);
            String text = pdftoText("Corpus/" + children[i]);
            String pdfLine = text;
            String text2 = text;
            pdfLine = pdfLine.replaceAll("[^A-Za-z0-9\\.]", " ")
                    .replaceAll("\\s+", " ");
            Article art = removeRefs(text);

            ArrayList<String> st = getStatements(pdfLine);

            art.setAuthor(findAuthors(text));
//            art.setAdress(findAddress(st));
//            art.setObjective(findObjective(st));
//            art.setProblem(findProblem(st));
//            art.setMethod(findMethod(st));
//            art.setContributions(findContribution(st));
//            art.setReferences(extractReferences(art.getRefe()));
            artigos.put(children[i], art);
        }
//        System.out.println("stop word");
//        for (String k: artigos.keySet()){
//            System.out.println("Count words of file "+k);            
//            Article art = artigos.get(k);
//            art.setAllWords(countWord(art.getOriginalText()));
//            art.setNumWord(art.getAllWords().size());
//            art.setOriginalText(removeSW(defaultSW, art.getOriginalText()));
//            art.setNonSW(countWord(art.getOriginalText()));
//            art.setNonSW(removeSmall(art.getNonSW()));
//            art.setSortedWords(ordWord(art.getNonSW()));
//            art.setTop10(top10(art.getSortedWords()));
//        }
//        
        Article a = artigos.get("90.pdf");
//        for (Palavra p:a.getSortedWords()){
//            System.out.println(p.getPalavra()+" "+p.getQuant());
//        }
//        System.out.println(a.getOriginalText());
//        for (Palavra p: a.getTop10()){
//            System.out.println(p.getPalavra()+" "+p.getQuant());
//        }
//        for (String p: a.getReferences()){
//            System.out.println("Ref: "+p);
//        }
//        for (String key : artigos.keySet()) {
//            System.out.println("----------------"+ key +"------------");
//            for (String p : artigos.get(key).getAuthor()) {
//                System.out.println("Author: " + p);
//            }
//        }

    }

}
