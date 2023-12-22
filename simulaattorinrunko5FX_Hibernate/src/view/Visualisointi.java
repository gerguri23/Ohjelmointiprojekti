package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualisointi extends Canvas implements IVisualisointi{

	private GraphicsContext gc;
	
	double i =0;	//alkup0
	double j = 10;	//alkup10
	double fillSize = 10;
	
	
	public Visualisointi(int w, int h) {
		super(w, h);
		this.fillSize = w / 20;
		gc = this.getGraphicsContext2D();
		tyhjennaNaytto();
	}
	

	public void tyhjennaNaytto() {
		gc.setFill(Color.ORANGE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		resetCoordinates();
	}
	
	public void uusiAsiakas() {
		gc.setFill(Color.BLACK);
		//gc.fillOval(i,j,10,10);
		gc.fillOval(i, j, fillSize, fillSize);	
		//i = (i + 10) % this.getWidth();
		i += fillSize;
		//j = (j + 12) % this.getHeight();
		if (i>=super.getWidth()) {
			j+=fillSize;	
			i = 0;
		}
	}
	
	private void resetCoordinates() {
	this.i = 0;
	this.j = 10;}
	
	
	//värjätään background colorilla "asiakkaan" päälle
	public void poistaAsiakas() {
		gc.setFill(Color.ORANGE);
		
		if(i < 0) {
			i = super.getWidth();
			j -= fillSize;
		}
		i -= fillSize;
		//rectangle peittää mustan ovaalin kunnolla(ovaali ei peitä)
		gc.fillRect(i, j, fillSize, fillSize);
		
		
		
		
	}
}
