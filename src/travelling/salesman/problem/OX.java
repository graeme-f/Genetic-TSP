/*
 * The MIT License
 *
 * Copyright 2020 gfoster.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package travelling.salesman.problem;

import java.util.Random;

/**
 * Select a subsequence from p2 and copy it into f1 (offspring).
 * Copy the elements that don't appear in the subsequence in order
 * Starting with the first element after the subsequence
 * 
 * Example
 * 
 * P1 = ( 3 4 8 | 2 7 1 | 6 5 )
 * P2 = ( 4 2 5 | 1 6 8 | 3 7 )
 * 
 * The subsequence is shown above and they will be crossed over to the "other"
 * offspring as follows
 * 
 * O1 = ( x x x | 1 6 8 | x x )
 * O2 = ( x x x | 2 7 1 | x x )
 * 
 * Then O1 is completed from P1 and O2 from P2
 * O1 = ( 4 2 7 | 1 6 8 | 5 3 )
 * O2 = ( 5 6 8 | 2 7 1 | 3 4 )
 * 
 * @author gfoster
 */
public class OX extends Crossover {
    @Override
    protected Population[] crossover(Population pop1, Population pop2, int generation){
        Random r = new Random();
        int size = pop1.getSize();
        Population[] result = new Population[2];
        int[] p1 = pop1.getPop();
        int[] p2 = pop2.getPop();
        int[] offspring1 = new int[size];
        int[] offspring2 = new int[size];
        int subsequenceStart = r.nextInt(size);
        int subsequenceEnd = r.nextInt(size-subsequenceStart)+subsequenceStart+1;
        // crossover copy from other parent to offspring
        for (int i = subsequenceStart; i < subsequenceEnd; i++){
            offspring1[i] = p2[i];
            offspring2[i] = p1[i];
        }
        // copy the order of unused cities from the parent to offspring 
        // starting at the end of the subsequence.
        int next1 = subsequenceEnd%size;
        int next2 = subsequenceEnd%size;
        for (int i = subsequenceEnd; i < size; i++){
            boolean contains1 = false;
            boolean contains2 = false;
            for (int c = subsequenceStart; c < subsequenceEnd; c++){
                if ((p1[i]) == offspring1[c]) contains1 = true;
                if ((p2[i]) == offspring2[c]) contains2 = true;
            }
            if (!contains1){
                offspring1[next1] = p1[i];
                next1 = (next1+1)%size;
            }
            if (!contains2){
                offspring2[next2] = p2[i];
                next2 = (next2+1)%size;
            }
        }
        for (int i = 0; i < subsequenceEnd; i++){
            boolean contains1 = false;
            boolean contains2 = false;
            for (int c = subsequenceStart; c < subsequenceEnd; c++){
                if ((p1[i]) == offspring1[c]) contains1 = true;
                if ((p2[i]) == offspring2[c]) contains2 = true;
            }
            if (!contains1){
                offspring1[next1] = p1[i];
                next1 = (next1+1)%size;
            }
            if (!contains2){
                offspring2[next2] = p2[i];
                next2 = (next2+1)%size;
            }
        }
        result[0] = new Population(offspring1, generation);
        result[1] = new Population(offspring2, generation);
        // display crossover result
        return result;
    }

} // end of class OX
