package view;

import java.util.ArrayList;

public interface ISimulaattorinUI {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	public double getAika();
	public long getViive();
	
	//Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa 
	public void setLoppuaika(double aika);
	
	// Kontrolleri tarvitsee  
	public IVisualisointi getVisualisointi(int i);
	public void vapautaKNappi();
	
	public int getKassojenMaara();
	public void asetaTekstit(Double[] tulokset);
	public void ajaUudelleen();
	public void nollaaStatsit();
	public int getNoutoAsiakkaidenOsuus();
	public double getNoutoPalveluAika();
	public double getKassaPalveluAika();
	public void setKeskimPalveluaika(double aika);
	public void setKeskimNoutoPalveluaika(double aika);
	public double getAsiakastiheys();
	public void setPalvellutAsiakkaat(int x);
	public ArrayList<Object> getAllInfo();
}
