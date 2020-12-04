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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author gfoster
 */
public class FXML_TSM_Controller implements Initializable {
    
    @FXML private Label     lblCities;
    @FXML private Slider    sldrCities;
    @FXML private Button    btnGenerate;
    @FXML private Button    btnShowCostMatrix;
    @FXML private Label     lblPopulation;
    @FXML private Slider    sldrPopulation;
    @FXML private ChoiceBox chbSelectionRule;
    @FXML private Label     lblEntries;
    @FXML private Slider    sldrEntries;
    @FXML private ChoiceBox chbCrossoverRule;
    @FXML private Label     lblMutationRate;
    @FXML private Slider    sldrMutationRate;
    @FXML private CheckBox  cbElitism;
    @FXML private CheckBox  cbShowRoute;
    @FXML private CheckBox  cbShowBestRoute;
    @FXML private Label     lblRouteDistance;
    @FXML private Button    btnNextIndividual;
    @FXML private Label     lblIndividual;
    @FXML private Label     lblBestPop;
    @FXML private CheckBox  cbShowGrid;
    @FXML private Slider    sldrGenerations;
    @FXML private Button    btnNextGeneration;
    @FXML private ProgressBar pbNextGenerations;
    @FXML private Button    btnShowHistory;
    @FXML private RowConstraints rcProgressBar;
    
    @FXML private void showCostMatrix(ActionEvent event) {
        event.consume();
        
        Scene newScene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCostMatrix.fxml"));
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            System.out.println("Fail");
            System.out.println(e);
            return;
        }
        
        Stage inputStage = new Stage();
        inputStage.initOwner(TravellingSalesmanProblem.primaryStage);
        inputStage.setTitle("Cost Matrix");
        inputStage.setScene(newScene);
        FXMLCostMatrixController cmc = loader.<FXMLCostMatrixController>getController();
        cmc.buildCostMatrix(tsm.getCostMatrix(),tsm.bestPop);
        inputStage.showAndWait();
    }
    
    @FXML private void showHistory(ActionEvent event) throws IOException {
        event.consume();
        
        Scene newScene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLHistory.fxml"));
        newScene = new Scene(loader.load());
        
        Stage inputStage = new Stage();
        inputStage.initOwner(TravellingSalesmanProblem.primaryStage);
        inputStage.setTitle("History");
        inputStage.setScene(newScene);
        FXMLHistoryController hc = loader.<FXMLHistoryController>getController();
        hc.displayHistoryGraph(tsm.populationHistory);
        inputStage.showAndWait();
    } // end of method showHistory()
    
    @FXML private Canvas    canvas;
    
    private TSM tsm;
    private GraphicsContext gc;
    private int currentRoute;
    private Population bestPop = null;
    private boolean elitism = true;
    private final int MAP_SCALE = 10;

    @FXML private void generateAction(ActionEvent event) {
        // Initialise
        tsm = new TSM((int)sldrPopulation.getValue()
                     ,(int)sldrMutationRate.getValue()
                     ,generationCount((int)sldrGenerations.getValue())
                     ,(int)sldrCities.getValue()
                     ,57
                     ,MAP_SCALE
                     , 20);
        String selectionValue = (String)chbSelectionRule.getValue();
        switch (selectionValue) {
            case "Roulette":
                tsm.selection = new Roulette((int)sldrPopulation.getValue());
                break;
            case "Stochastic Universal Sampling":
                tsm.selection = new SUS((int)sldrPopulation.getValue());
                break;
            case "Tournament":
                tsm.selection = new Tournament((int)sldrPopulation.getValue(), (int)sldrEntries.getValue());
                break;
            default:
                break;
        }
        String value = (String) chbCrossoverRule.getValue();
        if ("Partially Mapped Crossover".equals(value)){
            tsm.crossover = new PMX();
        } else if  ("Order Crossover".equals(value)){
            tsm.crossover = new PMX(); // OX();
        }
        // Evaluate
        bestPop = tsm.generateFitnesses();
        
        currentRoute = 0;
        drawCities(event);
        tsm.getCostMatrix();
        btnShowCostMatrix.setDisable(false);
        btnNextGeneration.setDisable(false);
        btnNextIndividual.setDisable(false);
        ArrayList<Population> pool = tsm.getPopPool();
    }

    @FXML private void nextGeneration(ActionEvent event){
        if (tsm != null){
            // Create a background Task
            Task<Void> task = new Task<Void>() {
            @Override
                protected Void call() throws Exception {
                    tsm.setProgressUpdate((workDone, totalWork) -> 
                        updateProgress(workDone, totalWork));
                    if (tsm.getGenerationRun() > 5000){
                        pbNextGenerations.setVisible(true);
                        rcProgressBar.setMinHeight(30);
                        rcProgressBar.setMaxHeight(30);
                    }
                    bestPop = tsm.runGenerations(elitism, bestPop);
                    return null;
                }
            };
            task.setOnFailed(wse -> {
                wse.getSource().getException().printStackTrace();
            });

            // If the task completed successfully, perform other updates here
            task.setOnSucceeded(wse -> {
                drawCities(event);
                btnShowHistory.setDisable(false);
                pbNextGenerations.setVisible(false);
                rcProgressBar.setMinHeight(0);
                rcProgressBar.setMaxHeight(0);
            });
            
            pbNextGenerations.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        }
    }
    
    @FXML private void elitism(ActionEvent event){
        elitism = cbElitism.isSelected();
    }
    
    @FXML private void drawCities(ActionEvent event) {
        // Display
        lblIndividual.setText("Route "+(currentRoute+1)+" of "+ (int)sldrPopulation.getValue());
        lblRouteDistance.setText("Distance ");
        lblBestPop.setText("Best route" + bestPop.getFitness());
        displayMap();
        if (tsm != null){
            displayLocations();
            if (cbShowRoute.isSelected()){
                displayRoute();
            }
            if (cbShowBestRoute.isSelected()){
                drawRoute(bestPop);
            }
        }
    }

    @FXML private void nextRoute(ActionEvent event){
        currentRoute = (currentRoute+1)%(int)sldrPopulation.getValue();
        lblIndividual.setText("Route "+(currentRoute+1)+" of "+ (int)sldrPopulation.getValue());
        // Display
        lblIndividual.setText("Route "+(currentRoute+1)+" of "+ (int)sldrPopulation.getValue());
        lblRouteDistance.setText("Distance ");
        lblBestPop.setText("Best route" + bestPop.getFitness());
        drawCities(event);
    }
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chbSelectionRule.getItems().add("Roulette");        
        chbSelectionRule.getItems().add("Stochastic Universal Sampling");
        chbSelectionRule.getItems().add("Tournament");
        chbSelectionRule.setValue("Roulette");
        chbCrossoverRule.getItems().add("Partially Mapped Crossover");
        chbCrossoverRule.setValue("Partially Mapped Crossover");
        
        sldrCities.valueProperty().addListener((ObservableValue<? extends Number> ov
                                               ,Number old_val
                                               ,Number new_val) -> {
            lblCities.setText("Cities (" + new_val.intValue() +")");
        });

        sldrPopulation.valueProperty().addListener((ObservableValue<? extends Number> ov
                                                   ,Number old_val
                                                   ,Number new_val) -> {
            lblPopulation.setText("Population (" + new_val.intValue() +")");
            sldrEntries.setMax(new_val.intValue());
        });

        sldrEntries.valueProperty().addListener((ObservableValue<? extends Number> ov
                                                ,Number old_val
                                                ,Number new_val) -> {
            if (null!=tsm && (tsm.selection instanceof Tournament)){
                ((Tournament)tsm.selection).setTournamentSize(new_val.intValue());
            }
            lblEntries.setText("Entries (" + new_val.intValue() +")");
        });
        
        sldrMutationRate.valueProperty().addListener((ObservableValue<? extends Number> ov
                                                   ,Number old_val
                                                   ,Number new_val) -> {
            lblMutationRate.setText("Mutation (" + new_val.intValue() +"%)");
            if (tsm != null){
                tsm.setMutationRate(new_val.intValue());
            }
        });
        
        sldrGenerations.valueProperty().addListener((ObservableValue<? extends Number> ov
                                                   ,Number old_val
                                                   ,Number new_val) -> {
            int generations = generationCount(new_val.doubleValue());
            if (generations == 1){
                btnNextGeneration.setText("Next Genaration");
            } else {
                btnNextGeneration.setText(generations +" Genarations");
            }
            if (tsm != null){
                tsm.setGenerationRun(generations);
            }
        });
        
        gc = canvas.getGraphicsContext2D();
        currentRoute = 0;
        displayMap();
    }
    
    private int generationCount(double g){
        int exp = (int)g;
        int mult = 1;
        if (g-exp > 0.5) mult = 5;
        return mult*(int)Math.pow(10,exp);
    }
    public void displayMap(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.rgb(0,0,0));
        gc.setLineDashes();
        gc.setLineWidth(2);
        gc.strokeLine(20,0,20,600);
        gc.strokeLine(0,580,600,580);
        gc.setLineWidth(1);
        for (int i = 20; i < 600; i+=50){
            gc.strokeLine(10,i+10,20,i+10);
            gc.strokeLine(i,580,i,590);
        }
        if (cbShowGrid.isSelected()){
            gc.setStroke(Color.rgb(80,80,80));
            gc.setLineDashes(2d);
            gc.setLineWidth(0.5);
            for (int i = 20; i < 600; i+=50){
                gc.strokeLine(20,i+10,600,i+10);
                gc.strokeLine(i,0,i,580);
            } 
        }
    } // end if method displayMap()
    
    private void plotCity(int id, Point coord){
        if (id == 1){
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(Color.BLACK);
        }
        gc.fillOval(coord.getX()+18,578-coord.getY(),4,4);
        
        if (coord.getY()<290){
            gc.fillText(Integer.toString(id), coord.getX()+14,573-coord.getY());
        } else {
            gc.fillText(Integer.toString(id), coord.getX()+14,593-coord.getY());
        }
    }
    public void displayLocations(){
        for (int i = 0; i <  tsm.getCityList().size(); i++){
            plotCity(i+1, tsm.getCityList().get(i));
        }
    }
    
    private void displayRoute(){
        lblRouteDistance.setText("Distance " + tsm.getPopPool().get(currentRoute).getFitness());
        Population pop = tsm.getPopPool().get(currentRoute);
        drawRoute(pop);
    }

    private void drawRoute(Population pop){
        int[] route = pop.getPop();
        ArrayList<Point> cities = tsm.getCityList();
        Point p0 = cities.get(route[0]);
        if (pop == bestPop){
            gc.setStroke(Color.rgb(255,80,80));
        } else {
            gc.setStroke(Color.rgb(80,80,255));
        }
        gc.setLineDashes();
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(p0.getX()+20,580-p0.getY());
        for (int i = 1; i < route.length; i++){
            Point p = cities.get(route[i]);
            gc.lineTo(p.getX()+20,580-p.getY());
        }
        gc.lineTo(p0.getX()+20,580-p0.getY());
        gc.stroke();                
    }
}
