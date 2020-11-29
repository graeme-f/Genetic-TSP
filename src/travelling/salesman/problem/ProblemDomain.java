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
import java.util.Random;

/**
 *
 * @author gfoster
 */
abstract public class ProblemDomain {
    protected int populationSize;
    protected ArrayList<Population> populationPool;
    protected Population bestPop;
    public Selection selection;
    Crossover crossover;
    protected int mutationRate;
    protected int generationRun;

    ProblemDomain(int popSize, int mutate, int generations){
        populationSize = popSize;
        mutationRate = mutate;
        generationRun = generations;
    }
    
    public ArrayList<Population> mutate(ArrayList<Population> pool){
        int N = pool.size();
        for (int i = 0; i < N; i++){
            pool.get(i).mutate(mutationRate);
        }
        return pool;
    }
    
    public void setMutationRate(int mutate){
        mutationRate = mutate;
    }
    
    public void setGenerationRun(int generations){
        generationRun = generations;
    }
    public int getGenerationRun(){
        return generationRun;
    }
    
    public void promoteMatingPool(ArrayList<Population> matingPool){
        populationPool = matingPool;
    }
    
    abstract protected int fitness(Population pop);
} // end of class ProblemDomain
