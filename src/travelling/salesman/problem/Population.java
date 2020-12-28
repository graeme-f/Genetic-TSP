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
 *
 * @author gfoster
 */
public class Population {
    
    private int generation;
    private int[] individuals;
    private int fitnessValue;
    private boolean selected;
    
    Population(int[] individuals, int generationCount){
        this.individuals = individuals;
        generation = generationCount;
    }
    
    Population(int size){
        generation = 0;
        individuals = new int[size];
        for (int i = 0; i < size; i++){
            individuals[i] = i;
        }
        // Shuffle the population
        Random rand = new Random();
	
        for (int i = 1; i < size; i++) {
            int randomIndexToSwap = rand.nextInt(size);
            int temp = individuals[randomIndexToSwap];
            individuals[randomIndexToSwap] = individuals[i];
            individuals[i] = temp;
        }
        rotate();
    }
    
    // Rotate the population so that it starts at zero
    private void rotate(){
        int[] newArray = new int[individuals.length];
        int i = 0;
        for (; i < individuals.length; i++) {
            if (individuals[i] == 0)
                break;
        }
        for (int j = i; j < individuals.length; j++) {
            newArray[j-i] = individuals[j];
        }
        for (int j = 0; j < i; j++) {
            newArray[j+individuals.length-i] = individuals[j];
        }
        individuals = newArray;
    }
    
    public void mutate(int mutationRate){
        Random r = new Random();
        // Because we rotate the population around so that it always starts with the first cty
        // Don't mutate the first city.
        for (int i = 1; i < individuals.length; i++) {
            int chance = r.nextInt(100);
            if (mutationRate>chance){
                int swap = r.nextInt(individuals.length-1)+1;
                int  temp = individuals[swap];
                individuals[swap] = individuals[i];
                individuals[i] = temp;
            }
        }
    }
    
    public int getGeneration(){return generation;}
    public int[] getPop(){return individuals;}
    public int getSize(){return individuals.length;}
    public void setFitness(int fitness){fitnessValue = fitness;}
    public int getFitness(){return fitnessValue;}
} // end of class Population
