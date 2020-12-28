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

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author gfoster
 */
public class FXMLHistoryController implements Initializable  {

    @FXML private Canvas    historyCanvas;

    private GraphicsContext gc;
    
    private boolean linear; // Is the horizontal axis linear or logarithmic?

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void displayHistoryGraph(ArrayList<Population> history){
        gc = historyCanvas.getGraphicsContext2D();
        int maxCost = history.get(0).getFitness();
        int minCost = history.get(history.size()-1).getFitness();
        int totalGenerations = ProblemDomain.getGeneration();
        int verticalMax = verticalBand(maxCost);
        int horizontalMax = horizontalBand(totalGenerations);
        
        // Draw the axis
        gc.clearRect(0, 0, historyCanvas.getWidth(), historyCanvas.getHeight());
        gc.setStroke(Color.rgb(0,0,0));
        gc.setLineDashes();
        gc.setLineWidth(2);
        gc.strokeLine(40,0,40,394);
        gc.strokeLine(40,407,40,420);
        gc.strokeLine(0,400,35,400);
        gc.strokeLine(45,400,660,400);
        gc.setLineWidth(1);
 
        int yValue = verticalMax;
        for (int i = 0; i < 10; i++){
            gc.fillText(Integer.toString(yValue), 0, 15+40*i);
            yValue -= verticalMax/10;
            gc.strokeLine(35, 10+40*i, 45, 10+40*i);
            // add a horizonatl guide line
            gc.setLineDashes(2d);
            gc.strokeLine(40, 10+40*i, 660, 10+40*i);
            gc.setLineDashes();
        }
        
        gc.setTextAlign(TextAlignment.CENTER);
        if (linear){
            int xValue = horizontalMax/10;
            for (int i = 0; i < 10; i++){
                gc.fillText(Integer.toString(xValue), 100+60*i, 415);
                xValue += horizontalMax/10;
                // add the horizontal tick marks
                gc.strokeLine(100+60*i, 405, 100+60*i, 395);
                // add a vertical guide line
                gc.setLineDashes(2d);
                gc.strokeLine(100+60*i, 0, 100+60*i, 390);
            }
        } else {
            int xValue = 10;
            int xGap = 600/horizontalMax;
            for (int i = 0; i < horizontalMax; i++){
                gc.fillText(Integer.toString(xValue), 40+xGap*(i+1), 415);
                xValue *= 10;
                // add the horizontal tick marks
                gc.strokeLine(40+xGap*(i+1), 405, 40+xGap*(i+1), 395);                
                // add a vertical guide line
                gc.setLineDashes(2d);
                gc.strokeLine(40+xGap*(i+1), 0, 40+xGap*(i+1), 390);
                // add intermediate guide lines (to emphasis the log scale)
                gc.setLineWidth(0.5);
                for (int j = 2; j<10; j++){
                    double offset = i+Math.log10(j);
                    gc.strokeLine(40+xGap*offset, 0, 40+xGap*offset, 390);                
                }
                gc.setLineWidth(1);
            }
        }
        gc.setLineDashes();
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("0", 37, 405);
        
        // Draw the graph
        double oldXCoord = 0;
        double oldYCoord = 0;
        double xCoord, yCoord;
        for (int i = 0; i < history.size(); i++){
            int x = history.get(i).getGeneration();
            int y = history.get(i).getFitness();
            yCoord = 400*(verticalMax - y)/verticalMax;
            if (linear){
                xCoord = 600*x/horizontalMax+40;
            } else {
                xCoord = 600*(Math.log10( x+1 ))/horizontalMax+40;
            }
            gc.fillOval(xCoord-2, yCoord-2, 4, 4);
            if (oldXCoord != 0){
                gc.strokeLine(oldXCoord, oldYCoord, xCoord, yCoord);
            }
            oldXCoord = xCoord;
            oldYCoord = yCoord;
        }
        if (linear){
            xCoord = 600*totalGenerations/horizontalMax+40;
        } else {
            xCoord = 600*(Math.log10( totalGenerations+1 ))/horizontalMax+40;
        }
        gc.strokeLine(xCoord, oldYCoord, oldXCoord, oldYCoord);

    }
    
    private int verticalBand(int max){
        int bands[] = {80, 160, 200, 400, 800, 1600, 2000, 4000, 8000, 16000, 20000, 40000};
        for (int i =0; i < bands.length; i++){
            if (max <= bands[i])
                return bands[i];
        }
        return (int) (Math.ceil(max / 10000.0) * 10000);
    }
    
    private int horizontalBand(int max){
        linear = true;
        if (max > 1200){
            linear = false;
            int digits = (int)Math.floor( Math.log10( max ) ) + 1;
            return digits;
        } else {
            int bands[] = {60, 120, 240, 300, 600, 1200};
            for (int i =0; i < bands.length; i++){
                if (max <= bands[i])
                    return bands[i];
            }
        }
        return 1200;
    }

} // end of class FXMLHistoryController
