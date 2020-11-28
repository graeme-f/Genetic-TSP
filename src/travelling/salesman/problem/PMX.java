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

import java.util.Arrays;
import java.util.Random;

/**
 * Select a subsequence from p2 and copy it into f1 (offspring).
 * Create a mapping of the individuals that appear in the sub sequence
 * Copy the individuals from p1 to f1 that don't appear in the mapping
 * If the appear in the mapping then find the value to use by looking it up
 * This can result in multiple lookups being needed.
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
 * O1 = ( 3 4 x | 1 6 8 | x 5 )
 * O2 = ( 4 x 5 | 2 7 1 | 3 x )
 * 
 * The remaining element need to be looked up from the mapping
 * O1: 8-> 1 -> 2
 * O1: 6-> 7
 * giving
 * O1 = ( 3 4 2 | 1 6 8 | 7 5 )
 * 
 * likewise
 * O2: 2->1 ->8
 * O2: 7->6
 * giving
 * O2 = ( 4 8 5 | 2 7 1 | 3 6 )
 * 
 * @author gfoster
 */
public class PMX extends Crossover {
    @Override
    protected Population[] crossover(Population pop1, Population pop2){
        Random r = new Random();
        int size = pop1.getSize();
        Population[] result = new Population[2];
        int[] p1 = pop1.getPop();
        int[] p2 = pop2.getPop();
        int[] offspring1 = new int[size];
        int[] offspring2 = new int[size];
        int subsequenceStart = r.nextInt(size);
        int subsequenceEnd = r.nextInt(size-subsequenceStart)+subsequenceStart+1;
        int [] mapping1 = new int[subsequenceEnd-subsequenceStart];
        int [] mapping2 = new int[subsequenceEnd-subsequenceStart];
        // crossover copy from other parent to offspring
        for (int i = subsequenceStart; i < subsequenceEnd; i++){
            offspring1[i] = p2[i];
            mapping1[i-subsequenceStart] = p2[i];
            offspring2[i] = p1[i];
            mapping2[i-subsequenceStart] = p1[i];
        }
        // copy from parent to offspring if no conflict otherwise use the mapping
        for (int i = 0; i < subsequenceStart; i++){
            offspring1[i]=mapping(mapping1,mapping2,p1[i]);
            offspring2[i]=mapping(mapping2,mapping1,p2[i]);
        }
        // copy from parent to offspring if no conflict otherwise use the mapping
        for (int i = subsequenceEnd; i < size; i++){
            offspring1[i]=mapping(mapping1,mapping2,p1[i]);
            offspring2[i]=mapping(mapping2,mapping1,p2[i]);            
        }
        result[0] = new Population(offspring1);
        result[1] = new Population(offspring2);
        // display crossover result
        return result;
    }
    
    private int mapping(int[] mapA, int[] mapB, int t){
        for (int i=0; i < mapA.length; i++){
            if (mapA[i]==t)
                return mapping(mapA, mapB, mapB[i]);
        }
        return t;
    }
} // end of class PMX
