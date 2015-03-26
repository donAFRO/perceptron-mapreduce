/*
 *  Copyright (C) 2015 Karl R. Wurst
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

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
