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
public class SUS extends Selection{
    SUS (int poolSize){
        super(poolSize);
    }
    
    @Override
    public ArrayList<Population> select(ArrayList<Population> pool){
        double distance = 1.0/poolSize;
        Random r = new Random();
        double posn = r.nextDouble();
        matingPool = new ArrayList();
        for (int i = 0; i < poolSize; i++){
            for (int j = 0; j < poolSize; j++){
                if (posn < probability[j]){
                    matingPool.add(pool.get(j));
                    break;
                }
                posn += distance;
                if (posn > 1){
                    posn -= 1;
                }
            }
        }
        return matingPool;
    }

} // end of class SUS
