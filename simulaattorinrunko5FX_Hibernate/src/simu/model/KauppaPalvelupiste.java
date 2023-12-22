package simu.model;

import java.util.PriorityQueue;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;

//OMA ALILUOKKA KAUPALLE, joka palvelee kaikkia "jonoon tulleita" heti
public class KauppaPalvelupiste extends Palvelupiste {
	private int kaupassa;
	private PriorityQueue<Asiakas> kassalleJono = new PriorityQueue<Asiakas>();
	
	//TODO tarvitsee oman jonon, joka olisi PriorityQeue
	//jotta ottaa asiakkaan, joka on vähiten aikaa kaupassa, eikä
	//Linkded List ottaa aina seuraavana olevan(ei katso kauanko olisi oikeasti kaupassa)a
	public KauppaPalvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi, String id) {
		super(generator, tapahtumalista, tyyppi, id);

	}
	
	//overridattu metodi, lisää omaan priority queueen asiakkaan, ja sorttaa jo tässä?
	public void lisaaJonoon(Asiakas a){ 
		a.setKassalleAika(Kello.getInstance().getAika());
		kassalleJono.add(a);
	}
	
	@Override   //taitaa olla turha merkintä tämä override, jos overridaa automaattisesti
	public void aloitaPalvelu() {
		
		for(Asiakas a : kassalleJono) {
			//katsotaan onko jonossa ketään Asiakasta joka ei vielä "ole kaupassa"
			//ja "otetaan" se kauppaan
			if(a.getKaupassa() == false) {
				a.setKaupassa(true);
				kaupassa++;		//ostosalueella olevat lisääntyvät
				//super.varattu = true; // tää aiheuttaa null-asiakkaan
				double palveluaika = super.generator.sample();
				a.setOsteluAika(palveluaika);
				System.out.println("-+-+-+ -++- -+++Kaupassa ostoksilla nyt " + this.kaupassa);
				//Trace.out(Trace.Level.INFO, "Asiakas " + super.jono.peek().getId() + " on kaupassa. ");
				Trace.out(Trace.Level.INFO, " - - - - - - - - - Ostosalueella on " + this.kassalleJono);
				double aikaMennaKassalle = Kello.getInstance().getAika() + palveluaika;
				a.setKassalleAika(aikaMennaKassalle);
				super.tapahtumalista
						.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi,aikaMennaKassalle));
			}
			
		}
	}
	
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		//Asiakas a = kassalleJono.poll();
		kaupassa--;			//kaupassa ostosalueella olevat asiakkaat vähenevät
		System.out.println("-+-+-+ -++- -+++Kaupassa ostoksilla nyt " + this.kaupassa);
		return kassalleJono.poll();
	}
	
	public int ostoksilla() {
		return kassalleJono.size();
	}
	
	public PriorityQueue<Asiakas> getAsiakasIDt() {
		return kassalleJono;
	}
	
	public boolean onJonossa() {
		return kassalleJono.size() != 0;
	}

}
