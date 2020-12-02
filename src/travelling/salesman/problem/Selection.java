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

import java.util.ArrayList;

/**
 *
 * @author gfoster
 */
abstract public class Selection {
    int poolSize;
    ArrayList<Population> matingPool;
    double [] probability;
    
    Selection (int poolSize){
        this.poolSize = poolSize;
        probability = new double[poolSize];
    }
    
    public void prepare(ArrayList<Population> pool){
        // The travellign Salesman Problem is looking for a minimum so the inverse fitness is 
        // required to calculate the probabilities
        inverseProbability(pool);
    }

    private void inverseProbability(ArrayList<Population> pool){
        // Calculate the probability of each population being selected
        // This is based on the inverse of the fitness
        double sum =0;
        double p;
        for (int i = 0; i < poolSize; i++){
            p = 1.0/pool.get(i).getFitness();
            sum += p;
            probability[i] = p;
        }
        for (int i = 0; i < poolSize; i++){
            probability[i] /= sum;
            // make the probability cumulative
            if (i != 0){
                probability[i] += probability[i-1];
            }
        }        
    } // end of method inverseProbability()
    
    abstract public ArrayList<Population> select(ArrayList<Population> pool);
} // end of class Selection
