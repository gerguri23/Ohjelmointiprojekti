package simu.model;

import simu.framework.Kello;
import simu.framework.Trace;


// TODO:
// Asiakas koodataan simulointimallin edellyttämällä tavalla (data!)
public class Asiakas implements Comparable<Asiakas>{
	private double saapumisaika;
	private double poistumisaika;
	private int id;
	private static int i = 1;
	private boolean kaupassa = false;
	private double kassalleAika;
	private double osteluAika;
	private double jonotusAika;;
	
	
	

	//parametriton konstruktori, ei tarvitse kirjoitella meneekö vai eikö mene noutopisteelle
	public Asiakas(){
		id = i++;	    
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika);	
	}


	public double getOsteluAika() {
		return osteluAika;
	}


	public void setOsteluAika(double osteluAika) {
		this.osteluAika = osteluAika;
	}

	public double getPoistumisaika() {
		return poistumisaika;
	}

	public void setPoistumisaika(double aika) {
		this.poistumisaika = aika;
	}

	public double getSaapumisaika() {
		return saapumisaika;
	}

	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	public int getId() {
		return id;
	}
	
	//jotta voidaan asettaa ja tietää kassallemenoaika, että oikea asiakas menee oikeaan kellonaikaan kassalle
	public void  setKassalleAika(double aika) {
		this.kassalleAika = aika;
	}
	
	public double getKassalleAika()
	{
		return this.kassalleAika;
	}
	//Jotta KAUPPAPALVELUPISTE tietää pitääkö asiakas "ottaa palveluun" vai onko se jo
	//ettei lähettele null asiakkaita.. "ottamalla jonosta" jo kaupassa olevia asiakkaita
	public boolean getKaupassa() {
		return this.kaupassa;
	}
	
	public void setKaupassa(boolean tila) {
		this.kaupassa = tila;
	}
	
	//resetoidaan id:t ennen kuin käynnistetään simu uudelleen(jossain muussa luokassa)
	public void resetId() {
		i = 1;
	}
	
	public String toString() {
		return "asiakas" + this.id;
	}
	


	
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakas "+id+
				" saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+
				" poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+
				" meni kassalle jonottamaan: " + kassalleAika);
		setJonotusAika();
		Trace.out(Trace.Level.INFO,"Asiakas "+id+
				"JONOTTI KASSALLA YHTEENSÄ: " + jonotusAika);
	}

	
	//tämän avulla järjestetään asiakkaat PriorityQueuessa oikeaan kassallemenojärjestykseen
	public int compareTo(Asiakas o) {
		if (this.getKassalleAika() - ((Asiakas) o).getKassalleAika() > 0) {return 1;}
		if (this.getKassalleAika() - ((Asiakas) o).getKassalleAika() < 0) { return -1;}
		return 0;
		}


	public void setJonotusAika() {
		this.jonotusAika = poistumisaika - kassalleAika;
	}
	
	public double getJonotusAika() {
		return this.jonotusAika;
	}
 
}
