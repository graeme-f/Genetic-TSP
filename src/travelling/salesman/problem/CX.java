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

/**
 *
 * @author gfoster
 */
public class CX extends Crossover {

    @Override
    protected Population[] crossover(Population pop1, Population pop2, int generation){
//        System.out.println ("Before");
//        System.out.println (Arrays.toString(pop1.getPop()));
//        System.out.println (Arrays.toString(pop2.getPop()));
        
        int size = pop1.getSize();
        Population[] result = new Population[2];
        int[] p1 = pop1.getPop();
        int[] p2 = pop2.getPop();
        int[] offspring1 = new int[size];
        int[] offspring2 = new int[size];
        // Initialise the new popultion by copyign the first two cities
        // The first city is always the same, so copy the second to start the cycle.
        offspring1[0] = p1[0];
        offspring2[0] = p2[0];
        offspring1[1] = p1[1];
        offspring2[1] = p2[1];
        // cycle through population P1
        boolean finished = false;
        int i = 1;
        while (!finished){
            // find the position of p1[i] in p2
            int p = 1;
            for (; p < size; p++){
                if (p1[i] == p2[p]){
                    offspring1[p] = p1[p];
                    i = p;
                    break;
                }
            }
            if (p==1)
                finished = true;
        }
        // cycle through population P2
        finished = false;
        i = 1;
        while (!finished){
            // find the position of p2[i] in p1
            int p = 0;
            for (; p < size; p++){
                if (p2[i] == p1[p]){
                    offspring2[p] = p2[p];
                    i = p;
                    break;
                }
            }
            if (p==1)
                finished = true;
        }
//        System.out.println ("After Cycle");
//        System.out.println (Arrays.toString(offspring1));
//        System.out.println (Arrays.toString(offspring2));
        
        // copy the gaps from the other parent
        for (i = 1; i < size; i++){
            if (offspring1[i]==0){
                offspring1[i] = p2[i];
            }
            if (offspring2[i]==0){
                offspring2[i] = p1[i];
            }
        }
        result[0] = new Population(offspring1, generation);
        result[1] = new Population(offspring2, generation);
//        System.out.println ("After");
//        System.out.println (Arrays.toString(offspring1));
//        System.out.println (Arrays.toString(offspring2));
        // display crossover result
        return result;
    }

} // end of class CX
