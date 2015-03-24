public abstract class Perceptron {

    public final int CHARS = 95;
    protected Letter[] templates;
    
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

}