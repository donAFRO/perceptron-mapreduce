import java.util.Scanner;
import java.io.*;

public class Train extends Perceptron {

    public Train() {
        templates = new Letter[CHARS];
        for (int i = 0; i < CHARS; i++) {
            templates[i] = Letter.getNewBlankLetter((char)(i + 32));
        }
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