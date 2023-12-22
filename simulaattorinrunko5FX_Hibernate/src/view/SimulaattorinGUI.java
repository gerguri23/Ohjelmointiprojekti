package view;


import java.text.DecimalFormat;
import java.util.ArrayList;

import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;



public class SimulaattorinGUI extends Application implements ISimulaattorinUI{

	//Kontrollerin esittely (tarvitaan käyttöliittymässä)
	private IKontrolleri kontrolleri;

	// Käyttöliittymäkomponentit:
	private TextField aikaTeksti;
	private Double aika;
	private TextField viiveTeksti;
	private Long viive;
	private Label tulos;
	private Label aikaLabel;
	private Label viiveLabel;
	private Label tulosLabel;
	
	private Button kaynnistaButton;
	private Button hidastaButton;
	private Button nopeutaButton;

	private IVisualisointi ostosAlue;
	private IVisualisointi kassa1;
     private IVisualisointi kassa2;
     private IVisualisointi kassa3;
    private IVisualisointi kassa4;
    private IVisualisointi kassa5;
    private IVisualisointi noutop;
    private ChoiceBox<String> kassavalikko;
    private GridPane stats;
    //noutoasiakkaiden todennäköisyys prosentteina
     private TextField noutoOsuusField;
     private int noutoOsuus;
     
     
     private TextField noutoPalveluAikaField;
     private TextField kassaPalveluAikaField;
     private TextField asiakastiheysField;
     private double noutoPalveluAika;
     private double kassaPalveluAika;
     private double asiakastiheys;
     
     private boolean allOK = true;
    
    private Label kassa1Label, kassa2Label, kassa3Label, kassa4Label, 
    kassa5Label, ostosalueLabel, noutokassaLabel, kassatLabel;
    Label[] statsLabelit;
    
    //loppustatteja
    private Label keskimAsiointiAika;
    private Label keskimNoutoAika;
    private Label palvellutAsiakkaat;
    
    


	@Override
	public void init(){
		
		Trace.setTraceLevel(Level.INFO);
		
		kontrolleri = new Kontrolleri(this);
	}

	@Override
	public void start(Stage primaryStage) {
		// Käyttöliittymän rakentaminen
		try {
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
						
			
			primaryStage.setTitle("Simulaattori");

			kaynnistaButton = new Button();
			kaynnistaButton.setText("Käynnistä simulointi");
			kaynnistaButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	try {
	            		viive = Long.parseLong(viiveTeksti.getText());
	            	} catch (Exception e) {
	            		Alert alert = new Alert(AlertType.INFORMATION);
	        			alert.setTitle("Anna määrä numeroina");
	        			alert.setHeaderText(null);
	        			alert.setContentText("Viive täytyy olla numeroina.");
	        			viiveTeksti.setText("");
	        			
	        			alert.showAndWait();
	            	}
	            	try {
	            		aika = Double.parseDouble(aikaTeksti.getText());
	            	} catch (Exception e) {
	            		Alert alert = new Alert(AlertType.INFORMATION);
	            		alert.setTitle("Ajan täytyy olle numeroina");
	            		alert.setHeaderText(null);
	            		alert.setContentText("Anna aika numeroina!");
	            		aikaTeksti.setText("");
	            		alert.showAndWait();
	            	}
	            	setNoutoAsiakkaidenOsuus();
	            	setPalveluPisteidenAjat();
	            	if(allOK) {
	            		kontrolleri.kaynnistaSimulointi();
	            		 kaynnistaButton.setDisable(true);
	            	}
	            		
	            	
	               
	            }

				
	        });

			hidastaButton = new Button();
			hidastaButton.setText("Hidasta");
			hidastaButton.setOnAction(e -> kontrolleri.hidasta());

			nopeutaButton = new Button();
			nopeutaButton.setText("Nopeuta");
			nopeutaButton.setOnAction(e -> kontrolleri.nopeuta());
			
			kassatLabel = new Label("Kassojen määrä");
			
			String[] kassamaara = {"1", "2", "3", "4", "5"};
			kassavalikko = new ChoiceBox<>(FXCollections.observableArrayList(kassamaara));

			
			
			aikaLabel = new Label("Simulointiaika:");
			aikaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        aikaTeksti = new TextField("100");
	        aikaTeksti.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        aikaTeksti.setPrefWidth(150);

	        viiveLabel = new Label("Viive:");
			viiveLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        viiveTeksti = new TextField("5");
	        viiveTeksti.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        viiveTeksti.setPrefWidth(150);
	                	        
	        tulosLabel = new Label("Kokonaisaika:");
			tulosLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        tulos = new Label();
	        tulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        tulos.setPrefWidth(150);

	        
	        
	        HBox hBox = new HBox();
	        hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylä, oikea, ala, vasen
	        hBox.setSpacing(10);   // noodien välimatka 10 pikseliä
	        
	        GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setVgap(10);
	        grid.setHgap(5);

	        grid.add(aikaLabel, 0, 0);   // sarake, rivi
	        grid.add(aikaTeksti, 1, 0);          // sarake, rivi
	        grid.add(viiveLabel, 0, 1);      // sarake, rivi
	        grid.add(viiveTeksti, 1, 1);           // sarake, rivi
	        grid.add(tulosLabel, 0, 2);      // sarake, rivi
	        grid.add(tulos, 1, 2);           // sarake, rivi
	        grid.add(kaynnistaButton,0, 3);  // sarake, rivi
	        grid.add(nopeutaButton, 0, 4);   // sarake, rivi
	        grid.add(hidastaButton, 1, 4);   // sarake, rivi
	        grid.add(kassatLabel, 0, 5);
	        grid.add(kassavalikko, 1, 5);
	        grid.add(new Label("Noutokassan asiakkaiden osuus"), 0,6);
	        noutoOsuusField = new TextField("10");
	        grid.add(noutoOsuusField, 0, 7);
	        grid.add(new Label("Noutokassan palveluaika"), 0, 8);
	        noutoPalveluAikaField = new TextField("4");
	        grid.add(noutoPalveluAikaField, 0, 9);
	        kassaPalveluAikaField = new TextField("3");
	        grid.add(new Label("Kassojen palveluajat"),0, 10);
	        grid.add(kassaPalveluAikaField, 0, 11);
	        grid.add(new Label("Asiakastiheys"), 0, 12);
	        asiakastiheysField = new TextField("2");
	        grid.add(asiakastiheysField, 0, 13);
	        
	        ostosAlue = new Visualisointi(400,600);  //tässä Visualisointi.java tai Visualisointi2
	        
	        //KASSAVISUALISOINNIT
	        kassa1 = new Visualisointi(400,100);
	        kassa2 = new Visualisointi(400,100);
	        kassa3 = new Visualisointi(400,100);
	        kassa4 = new Visualisointi(400,100);
	        kassa5 = new Visualisointi(400,100);
	        noutop = new Visualisointi(400,100);
	        
	        kassa1Label = new Label("Kassa 1");
	        kassa2Label = new Label("Kassa 2");
	        kassa3Label = new Label("Kassa 3");
	        kassa4Label = new Label("Kassa 4");
	        kassa5Label = new Label("Kassa 5");
	        ostosalueLabel = new Label("Ostosalue");
	        noutokassaLabel = new Label("Noutopalvelupiste");
	        
	        
	        //loppusatteja...
	        stats = new GridPane();
	        
	        stats.add(new Label("Kassa"),  1,  0);
	        stats.add(new Label("Palvellut asiakkaat"),  2,  0);
	        stats.add(new Label("Käyttöaste"),  3,  0);
	        stats.add(new Label("Aktiiviaika"),  4,  0);
	        stats.add(new Label("Noutopiste"),  1,  1);
	        stats.add(new Label("Kassa 1"),  1,  2);
	        stats.add(new Label("Kassa 2"),  1,  3);
	        stats.add(new Label("Kassa 3"),  1,  4);
	        stats.add(new Label("Kassa 4"),  1,  5);
	        stats.add(new Label("Kassa 5"),  1,  6);
	        
	        
	        Label kassa1Palvellut = new Label("1");
	        Label kassa1kayttoaset = new Label("1");
	        Label kassa1Aktiiviaika = new Label("1");
	        Label kassa2Palvellut = new Label("2");
	        Label kassa2kayttoaset = new Label("2");
	        Label kassa2Aktiiviaika = new Label("2");
	        Label kassa3Palvellut = new Label("3");
	        Label kassa3kayttoaset = new Label("3");
	        Label kassa3Aktiiviaika = new Label("3");
	        Label kassa4Palvellut = new Label("4");
	        Label kassa4kayttoaset = new Label("4");
	        Label kassa4Aktiiviaika = new Label("4");
	        Label kassa5Palvellut = new Label("5");
	        Label kassa5kayttoaset = new Label("5");
	        Label kassa5Aktiiviaika = new Label("5");
	        Label noutoPalvellut = new Label("n");
	        Label noutokayttoaset = new Label("n");
	        Label noutoAktiiviaika = new Label("n");
	        
	        Label keskimKassaAikaLabel = new Label("Keskimääräinen kassallaoloaika");
	        Label keskimNoutoAikaLabel = new Label("Keskimääräinen noutokassallaoloaika");
	        Label palvellutAsiakkaatLabel = new Label ("Palvellut asiakkaat yhteensä");
	        
	        statsLabelit = new Label[] {
	        		noutoPalvellut, noutokayttoaset, noutoAktiiviaika,
	        		kassa1Palvellut, kassa1kayttoaset , kassa1Aktiiviaika,
	        		kassa2Palvellut, kassa2kayttoaset , kassa2Aktiiviaika,
	        		kassa3Palvellut, kassa3kayttoaset , kassa3Aktiiviaika,
	        		kassa4Palvellut, kassa4kayttoaset , kassa4Aktiiviaika,
	        		kassa5Palvellut, kassa5kayttoaset , kassa5Aktiiviaika
	        		};
	        
	        
	        for(int i = 0; i < statsLabelit.length; i++) {
	        	for(int y = 1; y < 7; y++) {
	        		for(int x = 2; x < 5; x++) {
		        		stats.add(statsLabelit[i], x, y);
		        		i++;
		        	}
				}
	        }
	        
	        keskimAsiointiAika = new Label("");//tyhjat merkkijonot odottamassa
	        keskimNoutoAika = new Label ("");
	        palvellutAsiakkaat = new Label ("JOO");
	        VBox stattiLaatikko = new VBox();
	        stattiLaatikko.getChildren().addAll(stats, keskimKassaAikaLabel, keskimAsiointiAika, 
	        		keskimNoutoAikaLabel, keskimNoutoAika,
	        		palvellutAsiakkaatLabel, palvellutAsiakkaat);
	        
	        VBox kassaLaatikko = new VBox();
	        kassaLaatikko.setSpacing(3);
	        kassaLaatikko.getChildren().addAll(kassa1Label,(Canvas)kassa1, kassa2Label,(Canvas)kassa2,
	        		kassa3Label,(Canvas)kassa3, kassa4Label,(Canvas)kassa4, kassa5Label,(Canvas)kassa5, noutokassaLabel,(Canvas)noutop);
	        // Täytetään boxi:
	        hBox.getChildren().addAll(grid,ostosalueLabel, (Canvas)ostosAlue, kassaLaatikko, stattiLaatikko);
	        
	        Scene scene = new Scene(hBox);
	        primaryStage.setScene(scene);
	        primaryStage.show();

	        

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	//Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)

	@Override
	public double getAika(){
		return aika;
	}

	@Override
	public long getViive(){
		return viive;
	}

	@Override
	public void setLoppuaika(double aika){
		 DecimalFormat formatter = new DecimalFormat("#0.00");
		 this.tulos.setText(formatter.format(aika));
	}


	@Override
	public IVisualisointi getVisualisointi(int p) {
		switch (p) {
		case 0:
			{return ostosAlue;}
		case 1:
			{return noutop;}
		case 2:
			{return kassa1;}
		case 3:
			{return kassa2;}
		case 4:
			{return kassa3;}
		case 5:
			{return kassa4;}
		case 6:
			{return kassa5;}	
		}
		return ostosAlue;
	}
	
	public void asetaTekstit(Double[] teksti) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        //update application thread
		    	for (int i = 0; i < teksti.length; i++) {
		    		
		    		if(teksti[i] == null) {
		    			teksti[i] = 0.0;
		    		}
		    		
		    		try {
		    			statsLabelit[i].setText(Double.toString(teksti[i]));	
		    		} catch(Exception e) {
		    			//statsLabelit[i].setText("");
		    			continue;
		    		}
					
				}
		    }
		    
		    //...
		});		
	}
	
	
	public void setNoutoAsiakkaidenOsuus() {		
		try {
			noutoOsuus = Integer.parseInt(noutoOsuusField.getText());
			allOK = true;
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Anna määrä numeroina");
			alert.setHeaderText(null);
			alert.setContentText("Osuuden on oltava prosentteina - numeroina");
			noutoOsuusField.setText("");
			allOK = false;
			
			alert.showAndWait();
		}
 	}
	public int getNoutoAsiakkaidenOsuus() {
		return noutoOsuus;
	}
	
	
	public int getKassojenMaara() {
		String s = (String) kassavalikko.getValue();
		if (s == null) { s = "5";} //jos ei ole valittu mitään, käytetään kaikkia kassoja
		int i = Integer.parseInt(s);

		System.out.println("Kassavalikosta valittiin " + i);
		return i;
	}
	
	// JavaFX-sovelluksen (käyttöliittymän) käynnistäminen
	public static void main(String[] args) {
		launch(args);
	}
	
	//että nappia painamalla voi käynnistää uudelleen
	public void vapautaKNappi() {
	kaynnistaButton.setDisable(false);		
	}
	
	public void nollaaStatsit () {
		for(Label l : statsLabelit) {
			l.setText("");
		}		
	}
	
	private void setPalveluPisteidenAjat() {
       
		try {
			noutoPalveluAika = Double.parseDouble(noutoPalveluAikaField.getText());
			if (noutoPalveluAika > 0) {allOK = true; }
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Anna määrä numeroina");
			alert.setHeaderText(null);
			alert.setContentText("Palveluajan on oltava numeroina");
			noutoPalveluAikaField.setText("");
			allOK = false;
			
			alert.showAndWait();
		}
		try {
			kassaPalveluAika = Double.parseDouble(kassaPalveluAikaField.getText());
			if (kassaPalveluAika > 0) {
				allOK = true;
			}
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Anna määrä numeroina");
			alert.setHeaderText(null);
			alert.setContentText("Palveluajan on oltava numeroina");
			kassaPalveluAikaField.setText("");
			allOK = false;
			
			alert.showAndWait();
		}
		try {
			asiakastiheys = Double.parseDouble(asiakastiheysField.getText());
			if (asiakastiheys <= 0) {
				asiakastiheys = 1;
			}
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Anna määrä numeroina");
			alert.setHeaderText(null);
			alert.setContentText("Asiakastiheyden on oltava numeroina, piste desimaalierottimena");
			kassaPalveluAikaField.setText("");
			allOK = false;
			
			alert.showAndWait();
		}
		
	}
	
	public double getNoutoPalveluAika()
	{
		return this.noutoPalveluAika;
	}
	
	public double getKassaPalveluAika() {
		return this.kassaPalveluAika;
	}
	
	public double getAsiakastiheys() {
		return this.asiakastiheys;
	}
	//toimenpiteet ennen kuin suoritetaan uusi ajo
	public void ajaUudelleen() {
		nollaaStatsit();
	}

	@Override
	public void setKeskimPalveluaika(double aika) {
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        //update application thread
		    	
				keskimAsiointiAika.setText(String.format("%.2f", aika));
				System.out.println("Keskimääräinen asiointiaika kassalla " + keskimAsiointiAika.getText()+ "!!!");
		    }
		    
		    //...
		});		
	}
	
	public void setKeskimNoutoPalveluaika(double aika) {
		Platform.runLater(new Runnable() {
			public void run() {
				keskimNoutoAika.setText(String.format("%.2f", aika));
			}
		});
	}
	
	public void setPalvellutAsiakkaat(int maara) {
		Platform.runLater(new Runnable() {
			public void run() {
				palvellutAsiakkaat.setText(Integer.toString(maara));
			}
		});
	}
	
	
	//ei tarvitse tätä metodia
	public ArrayList<Object> getAllInfo() {
		ArrayList<Object> kaikki = new ArrayList<Object>();
		//asetusten tiedot
		//simulaation pituus(double)
		kaikki.add(this.aika);
		//noutokassojen osuus prosentteina(int)
		kaikki.add(kassavalikko.getValue());
		kaikki.add(noutoOsuusField.getText());
		kaikki.add(noutoPalveluAikaField.getText());//double
		kaikki.add(kassaPalveluAikaField.getText());//double
		kaikki.add(Double.parseDouble(asiakastiheysField.getText()));//double
		
		//tulosten tiedot
		//kaikki noutopisteen ja kassojen tiedot.. pitää olla tarkkana mikä on mikä
		for (int i = 0; i < statsLabelit.length; i++) {
			kaikki.add(" * " + statsLabelit[i]);  //tähti mitä tulostaa konsoliin.. testissä
		}
		/*
		 * onkohan oikein?
		 * NOUTOPISTE: 0 palvellut asiakkaat, 1 - käyttöaste, 2 - aktiiviaika
		 * Kassat		3 - palvellut asiakkaat, 4 käyttöaste, 5, aktiiviaika
		 * loput kassat:		6,9,12,15,				7,10,13,16			8,11,14,17
		 */
		kaikki.add(keskimAsiointiAika.getText());
		kaikki.add(keskimNoutoAika.getText());
		kaikki.add(palvellutAsiakkaat.getText());
		return kaikki;
	}
}
