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
 * A random selection of individuals are selected from the population pool.
 * The number selected is based on tournamentSize. The fittest individual is
 * then selected to go into the mating pool. Same individuals can be selected 
 * more than once.
 * The greater the value of tournamentSize the greater the chance that the
 * fittest individuals will dominate the mating pool. The smaller the tournament
 * size then the greater the diversity of the mating pool will be.
 * 
 * Being selected for the tournament is always random, but once selected the 
 * fittest individual will always win.
 * 
 * @author gfoster
 */
public class Tournament extends Selection{
    int tournamentSize;
    Tournament (int poolSize, int tournamentSize){
        super(poolSize);
        this.tournamentSize = tournamentSize;
    }
    
    public void setTournamentSize(int size){
        tournamentSize = size;
    }
    
    @Override
    public ArrayList<Population> select(ArrayList<Population> pool){
        matingPool = new ArrayList();
        Random r = new Random();
        int N = pool.size();
        for (int i = 0; i < poolSize; i++){
            Population best = null;
            for (int j = 0; j < tournamentSize; j++){
                int selectID = r.nextInt(N);
                Population ind = pool.get(selectID);
                if ((best==null)||(ind.getFitness() < best.getFitness())){
                    best = ind;
                }
            }
            matingPool.add(best);
        }
        return matingPool;
    }
} // end of class Tournament
