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

public class Letter implements java.io.Serializable {
    
    public static final int SIZE = 35;
    private int[] bits;
    private char letter;
    
    private Letter(char letter) {
        bits = new int[SIZE];
        this.letter = letter;
    }
    
    private Letter(int[] bits, char letter) {
        this.bits = bits;
        this.letter = letter;
    }

    public static Letter getNewBlankLetter(char letter) {
        return new Letter(letter);
    }
    
    public static Letter getNewLetterFromBits(int[] bits, char letter) {
        return new Letter(bits, letter);
    }
    
    public static Letter getNewLetterFromTrainingPatternString(String line) {
        String bitsString = line.substring(0, SIZE);
        char letter = line.charAt(SIZE);
        int[] bits = getBitArrayFromString(bitsString);
        return getNewLetterFromBits(bits, letter);
    }
    
    public static Letter getNewLetterFromRecognitionPatternString(String line) {
        String bitsString = line.substring(0, SIZE);
        int offset = Integer.parseInt(line.substring(SIZE, line.length()));
        int[] bits = getBitArrayFromString(bitsString);
        return getNewLetterFromBits(bits, ' ');
    }
    
    public static int[] getBitArrayFromString(String bitsString) {
        int[] bits = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            bits[i] = (int)(bitsString.charAt(i)) - (int)'0';
        }
        return bits;
    }
    
    public char getLetter() {
        return letter;
    }
    
    public int[] getBits() {
        return bits;
    }

    public int correlate(Letter letter) {
        int sum = 0;
        for (int i = 0; i < bits.length; i++) {
            sum += bits[i] * letter.bits[i];
        }
        return sum;
    }
    
    public void reward(Letter pattern) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] += pattern.bits[i];
        }
    }
    
    public void punish(Letter pattern) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] -= pattern.bits[i];
        }
    }    

}
