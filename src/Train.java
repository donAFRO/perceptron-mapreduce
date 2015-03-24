import java.util.Scanner;
import java.io.*;

public class Train {

    public final int CHARS = 95;
    private Letter[] templates;
    
    public Train() {
        templates = new Letter[CHARS];
        for (int i = 0; i < CHARS; i++) {
            templates[i] = Letter.getNewBlankLetter((char)(i + 32));
        }
    }
    
    public int numOnes(Letter pattern) {
        int count = 0;
        for (int bit: pattern.getBits()) {
            count += bit;
        }
        return count;
    }
    
    public int guess(Letter pattern) {
        int guess = 0;
        if (numOnes(pattern) <= 2)
            return 0;
        int maxCorrelate = templates[0].correlate(pattern);
        for (int i = 1; i < CHARS; i++) {
            int currCorrelate = templates[i].correlate(pattern);
            if (currCorrelate > maxCorrelate) {
                guess = i;
                maxCorrelate = currCorrelate;
            }        
        }
        return guess;
    }

    public int findCorrect(Letter pattern) {
        return (int)pattern.getLetter() - 32;
    }

    public boolean checkLearn(int guess, int correct, Letter pattern) {
        if (guess != correct) {
            for (int i =0; i < CHARS; i ++) {
                if (i == correct) {
                    templates[i].reward(pattern);
                } else {
                    templates[i].punish(pattern);
                }
            }
        }
        return guess == correct;
    }
    
    public void trainTemplates(String filename) {
        FileReader file = null;
        double count = 1;
        double correctCount = 0;
        
        while (correctCount/count < .90) {
            count = 0;
            correctCount = 0;
            System.out.print("Training Run... ");
            try {
                file = new FileReader(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                count++;
                String line = scan.nextLine();
                Letter pattern = Letter.getNewLetterFromTrainingPatternString(line);
                int guess = guess(pattern);
                int correct = findCorrect(pattern);
                if (checkLearn(guess, correct, pattern))
                    correctCount++;
            }
            System.out.println((double)correctCount/(double)count);
            scan.close();
        }
    }
    
    public void writeTemplates(String filename) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(templates);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + filename);
        } catch(IOException i) {
            i.printStackTrace();
        }
    }
     
    public static void main(String[] args) {
        String trainingFilename = args[0];
        String templateFilename = args[1];
        
        Train t = new Train();
        
        t.trainTemplates(trainingFilename);
        t.writeTemplates(templateFilename);
    }
}