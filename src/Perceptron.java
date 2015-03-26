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

public abstract class Perceptron {

    public static final int CHARS = 95;
    protected static Letter[] templates;
    
    public static int numOnes(Letter pattern) {
        int count = 0;
        for (int bit: pattern.getBits()) {
            count += bit;
        }
        return count;
    }
    
    public static int guess(Letter pattern) {
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

    public static int findCorrect(Letter pattern) {
        return (int)pattern.getLetter() - 32;
    }

    public static boolean checkLearn(int guess, int correct, Letter pattern) {
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

}
