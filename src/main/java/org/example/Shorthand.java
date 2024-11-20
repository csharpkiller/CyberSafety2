package org.example;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// https://stackoverflow.com/questions/40318507/how-do-i-change-color-of-a-particular-word-document-using-apache-poi
public class Shorthand {
    private final String codeFont = "Times New Roman";
    private final String RED_COLOR_HEX = "DC143C";
    private final String BLACK_COLOR_HEX = "1F1E1E"; // 31, 30, 30 rgb
    private String CURRENT_COLOR = RED_COLOR_HEX;

    public void code(String text){
        XWPFDocument readDoc;
        try {
            readDoc = new XWPFDocument(new FileInputStream("welcome.docx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileOutputStream ostream;
        try {
            File file = new File("coded.docx");
            ostream = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[] charsToCode = text.toCharArray();
        int charId = 0;

        for (XWPFParagraph p : readDoc.getParagraphs()) {
            int runNumber = 0;
            while (runNumber < p.getRuns().size()) {
                XWPFRun r = p.getRuns().get(runNumber);
                String runText = r.getText(0);
                if(charId == charsToCode.length){
                    break;
                }
                if (runText != null && runText.contains(String.valueOf(charsToCode[charId]))) {
                    char[] runChars = runText.toCharArray();
                    StringBuffer sb = new StringBuffer();
                    for (int charNumber = 0; charNumber < runChars.length; charNumber++) {
                        if (runChars[charNumber] == charsToCode[charId]) {
                            r.setText(sb.toString(), 0);
                            r = p.insertNewRun(++runNumber);
                            r.setText(String.valueOf(charsToCode[charId]), 0);
                            r.setColor(CURRENT_COLOR);
                            r = p.insertNewRun(++runNumber);
                            sb = new StringBuffer();
                            charId++;
                            if(charId == charsToCode.length){
                                break;
                            }
                        } else {
                            sb.append(runChars[charNumber]);
                        }
                    }
                    r.setText(sb.toString(), 0);
                }
                runNumber++;
            }
        }

        if(charId < charsToCode.length){
            System.out.println("Не хватает символов в тексте");
            try {
                ostream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        try {
            readDoc.write(ostream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ostream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void decode(){
        XWPFDocument readDoc;
        try {
            readDoc = new XWPFDocument(new FileInputStream("welcome.docx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuffer stringBuffer = new StringBuffer();

        for(XWPFParagraph p : readDoc.getParagraphs()){
            for(XWPFRun r: p.getRuns()){
                System.out.println(r.getColor());
            }
        }
    }*/

}
