package simu.framework;

import java.util.ArrayList;
import simu.model.Palvelupiste;

public interface IMoottori { // UUSI
		
	// Kontrolleri käyttää tätä rajapintaa
	
	public void setSimulointiaika(double aika);
	public void setViive(long aika);
	public long getViive();
	public ArrayList<Double> getTulokset();
	public void setNoutoAsiakasProsentti(int noutoAsikkaidenOsuus);
	public void setNoutoPalveluaika(double aika);
	public void setPalveluajat(double kassaPalveluAika);
	public Palvelupiste[] getPalvelupisteet();
	public int getKassojenMaara();
	public double laskeKassojenKeskiarvoAsiointiAika();
	public double laskeNoutoPisteenKeskiarvoAsiointiaika();
	public void setAsiakastiheys(double tiheys);
	public int getPalvellutAsiakkaat();

}