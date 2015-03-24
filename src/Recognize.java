import java.util.Scanner;
import java.io.*;

public class Recognize extends Perceptron {
        
    public void readTemplates(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            templates = (Letter[]) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i) {
            i.printStackTrace();
            return;
        } catch(ClassNotFoundException c) {
            System.out.println("Letter[] class not found");
            c.printStackTrace();
            return;
        }
    }
    
    public void recognizeCharacters(String filename) {
        StringBuffer output = new StringBuffer();
        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(file);
        while (scan.hasNext()) {
            String line = scan.nextLine();
            Letter pattern = Letter.getNewLetterFromRecognitionPatternString(line);
            int guess = guess(pattern);     
            output.append(templates[guess].getLetter());
        }
        scan.close();
        int start = 0;
        int end = 70;
        while (end < output.length()) {
            System.out.println(output.substring(start, end));
            start = end;
            end = end + 70;
        }
        System.out.println(output.substring(start, output.length()));  
    }
    
    public static void main(String[] args) {
        String recognitionFilename = args[0];
        String templateFilename = args[1];
        
        Recognize r = new Recognize();
        
        r.readTemplates(templateFilename);
        r.recognizeCharacters(recognitionFilename);
    }
}