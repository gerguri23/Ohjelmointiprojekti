package controller;

import simu.model.Palvelupiste;

public interface IKontrolleri {
	
	// Rajapinta, joka tarjotaan  käyttöliittymälle:
	
	public void kaynnistaSimulointi();
	public void nopeuta();
	public void hidasta();
	
	// Rajapinta, joka tarjotaan moottorille:
	
	public void naytaLoppuaika(double aika);
	public void visualisoiAsiakas(int palvelupiste);
	public void poistaAsiakas(int palvelupiste);
	
	//vapauttaa käynnistysnapin
	public void valmistauduUudelleen();
	
	public void otaLopputiedot();

}
