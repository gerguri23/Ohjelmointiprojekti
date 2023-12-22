package simu.model;

import java.util.LinkedList;

import javax.persistence.Column;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;

// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class Palvelupiste {

	protected LinkedList<Asiakas> jono = new LinkedList<Asiakas>(); // Tietorakennetoteutus

	protected ContinuousGenerator generator;
	protected Tapahtumalista tapahtumalista;
	protected TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

	private String id;

	protected int palvellutAsiakkaat;


	private double aktiiviaika;


	private double lyhinPalvelu;


	private double pisinPalvelu;


	private double aloitusaika;
	private double lopetusaika;
	private double palveluaika;
	private double asiointiajat;

	// JonoStartegia strategia; //optio: asiakkaiden järjestys

	protected boolean varattu = false;

	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi,
			String id) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.id = id;
		this.aloitusaika = Kello.getInstance().getAika();
	}

	public Palvelupiste() {

	}

	public double getKeskimAsiointiAika() {
		System.out.println("asiointiaika " + asiointiajat);
		System.out.println("palvellut asiakkaat " + palvellutAsiakkaat);
		return asiointiajat / palvellutAsiakkaat;
	}

	public void lisaaJonoon(Asiakas a) {
		a.setKassalleAika(Kello.getInstance().getAika());// Jonon 1. asiakas aina palvelussa
		jono.add(a);

	}

	public Asiakas otaJonosta() { // Poistetaan palvelussa ollut
		varattu = false;
		double poistumisaika = Kello.getInstance().getAika();
		palvellutAsiakkaat++;
		aktiiviaika += palveluaika;
		try {
			Asiakas a = jono.peek();
			a.setPoistumisaika(poistumisaika);
			a.setJonotusAika();
			asiointiajat += a.getJonotusAika();
			System.out.println("+++++++++++++ asiakas" + a.getId() + "asiointiaika " + a.getJonotusAika());
			System.out.println("asiointiajat nyt " + asiointiajat);

		} catch (Exception e) {
			// älä tee mitään
		}
		vertaaAikoja();
		return jono.poll();

	}

	private void vertaaAikoja() {
		if (palveluaika > 0 && lyhinPalvelu > palveluaika || lyhinPalvelu == 0) {
			lyhinPalvelu = palveluaika;
		}
		if (pisinPalvelu < palveluaika) {
			pisinPalvelu = palveluaika;
		}
	}

	public int getPalvellutAsiakkaat() {
		return this.palvellutAsiakkaat;
	}

	public void aloitaPalvelu() { // Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		// Asiakas a = jono.peek();
		// a.setKassaAika();
		varattu = true;
		palveluaika = generator.sample();
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}

	public String getId() {
		return this.id;
	}

	public boolean onVarattu() {
		return varattu;
	}

	public boolean onJonossa() {
		return jono.size() != 0;
	}

	public double getAktiiviaika() {
		return aktiiviaika;
	}

	public double getLyhinPalvelu() {
		return lyhinPalvelu;
	}

	public double getPisinPalvelu() {
		return pisinPalvelu;
	}

	public double getKayttoaste() {
		return this.aktiiviaika / Kello.getInstance().getAika();
	}
	
	public int getJonoLength() {
		return this.jono.size();
	}

	public void setPalveluaika(double aika) {
		ContinuousGenerator gene = new Normal(aika, aika * 0.5);
		generator = gene;
	}

}
