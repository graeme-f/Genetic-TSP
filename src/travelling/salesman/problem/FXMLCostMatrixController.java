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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author gfoster
 */


public class FXMLCostMatrixController implements Initializable {

    @FXML private GridPane gpCostMatrix;
    @FXML private AnchorPane apScene;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void buildCostMatrix(int[][] costMatrix, Population bestPop){
        gpCostMatrix = new GridPane();
        int labelWidth = 600/costMatrix.length;
        Border solid = new Border(new BorderStroke(Color.BLACK, 
                                                   BorderStrokeStyle.SOLID, 
                                                   CornerRadii.EMPTY,
                                                   BorderWidths.DEFAULT
                                                  )
                                 );
        for (int r = 0; r < costMatrix.length; r++){
            for (int c = 0; c < costMatrix[r].length; c++){
                Label lbl = new Label(Integer.toString(costMatrix[r][c]));
                lbl.setBorder(solid);
                lbl.setMinWidth(labelWidth);
                lbl.setMinHeight(labelWidth);
                lbl.setAlignment(Pos.CENTER_RIGHT);
                if (onBestRoute(bestPop.getPop(), r, c)){
                    lbl.setStyle("-fx-border-color:red; -fx-background-color: blue;");
                    lbl.setAlignment(Pos.CENTER);
                }
                gpCostMatrix.add(lbl, r,c,1,1);
            }
        }
        gpCostMatrix.setMinSize(600, 600);
        //gpCostMatrix.setBorder();
        apScene.getChildren().add(gpCostMatrix);
    }
    
    private boolean onBestRoute(int[] pop, int r, int c){
        for (int i = 0; i < pop.length; i++){
            if (pop[i]==r && pop[(i+1)%pop.length]==c)
                return true;
            if (pop[i]==c && pop[(i+1)%pop.length]==r)
                return true;
        }
        return false;
    }
}
