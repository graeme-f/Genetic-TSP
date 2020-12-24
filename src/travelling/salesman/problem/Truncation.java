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
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author gfoster
 */
public class Truncation extends Selection{
    int cutoff;
    Truncation (int poolSize, int cutoff){
        super(poolSize);
        this.cutoff = cutoff;
    }
    
    public void setCutoffPercentage(int value){
        cutoff = value;
    }
    

    @Override
    public ArrayList<Population> select(ArrayList<Population> pool){
        matingPool = new ArrayList();
        // Sort the pool
        Collections.sort(pool, (Population o1, Population o2)->o1.getFitness()-o2.getFitness());
        
        int rank = 0;
        double mult = 100.0 / poolSize;
        for (int i = 0; i < poolSize; i++){
            if (rank * mult > cutoff){
                rank = 0;
            }
            matingPool.add(pool.get(rank));
            rank++;
        }
        Collections.shuffle(matingPool);
        return matingPool;
    }
} // end of class Truncation
