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
public class TSM extends ProblemDomain{
    private final int citySize;
    private ArrayList<Point> cityList;
    private final int[][] costMatrix;
    private final int mapSize;
    private final int mapScale;
    private final int mapMargin;
    
    TSM (int popSize, int mutate, int generations, int citys, int size, int scale, int margin){
        super(popSize, mutate, generations);
        citySize = citys;
        mapSize = size;
        mapScale = scale;
        mapMargin = margin;
        costMatrix = new int[citySize][citySize];
        generateCities();
        generateCost();
        generatePool();
    }
    
    TSM (int popSize, int mutate, int generations, ArrayList cities, int size, int scale, int margin){
        super(popSize, mutate, generations);
        citySize = cities.size();
        mapSize = size;
        mapScale = scale;
        mapMargin = margin;
        costMatrix = new int[citySize][citySize];
        generateCities(cities);
        generateCost();
        generatePool();
    }
    
    public final void generateCities(){
        Random r = new Random();
        cityList = new ArrayList<>();
        for (int i = 0; i < citySize; i++){
            int x = r.nextInt((mapSize) + 1)*mapScale;
            int y = r.nextInt((mapSize) + 1)*mapScale;
            cityList.add(new Point(x,y));
        }
    }

    public final void generateCities(ArrayList<String> cities){
        cityList = new ArrayList<>();
        for (int i = 0; i < citySize; i++){
            String[] inputNumber =  cities.get(i).split(",");
            int x = Integer.parseInt(inputNumber[0])*mapScale;
            int y = Integer.parseInt(inputNumber[1])*mapScale;
            cityList.add(new Point(x,y));
        }        
    }
    
    public final void generateCost(){
         for (int i = 0; i < citySize; i++){
             Point p1 = cityList.get(i);
             for (int j = i; j < citySize; j++){
                 Point p2 = cityList.get(j);
                 int cost = (int)Math.hypot(p1.getX()-p2.getX(), p1.getY()-p2.getY());
                 costMatrix[i][j] = cost;
                 costMatrix[j][i] = cost;
             } // end of inner loop
         } // end of outer loop
    }
    
    public final void generatePool(){
        populationPool = new ArrayList<>();
        for (int i = 0; i < populationSize; i++){
            Population pop = new Population(citySize);
            populationPool.add(pop);
        }
        populationHistory = new ArrayList<>();
        generateFitnesses();
    }
    
    public final void generateFitnesses(){
        populationPool.get(0).setFitness(fitness(populationPool.get(0)));
        Population currentBest = populationPool.get(0);
        int bestValue = currentBest.getFitness();
        for (Population pop : populationPool){
            pop.setFitness(fitness(pop));
            if (pop.getFitness() < bestValue){
                currentBest = pop;
                bestValue = pop.getFitness();
            }
        }
        
        if (populationHistory.size()==0 
         || populationHistory.get(populationHistory.size()-1).getFitness()> bestValue){
            bestPop = currentBest;
            populationHistory.add(currentBest);
        }
    }
    
    @Override
    protected final int fitness(Population pop){
        int cost = 0;
        int [] individuals = pop.getPop();
        for (int i = 0; i < individuals.length;i++){
            int id1 = individuals[i];
            int id2 = individuals[(i+1)%individuals.length];
            cost += costMatrix[id1][id2];
        }
        return cost;
    }
    
    public ArrayList<Point> getCityList() { return cityList;}
    public int[][] getCostMatrix() {return costMatrix;}
} // end of class TSM
