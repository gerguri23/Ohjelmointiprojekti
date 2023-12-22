package simu.model;

import java.util.ArrayList;
import java.util.Random;
import controller.IKontrolleri;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;

public class OmaMoottori extends Moottori {

	private Saapumisprosessi saapumisprosessi;
	
	private int kassojenMaara;
	private int noutoAsiakkaidenOsuus;
	private double noutoPalveluaika;
	private double kassaPalveluaika;
	private double noutoVariaatio;
	private double kassaVariaatio;
	private double asiakastiheys;

	public OmaMoottori(IKontrolleri kontrolleri, int kassojenlukumaara) { // UUSI

		// TODO singleton johon listataan tärkeät/lopputallennukseen menevät infot?
		// tai kaikki mikä näkyy GUI:sta...
		super(kontrolleri);
		// aktiivisten kassojen lukumäärä, saadaan kontrollerilta
		kassojenMaara = kassojenlukumaara;
		noutoVariaatio = noutoPalveluaika / 100;
		if(noutoVariaatio < 1) { noutoVariaatio = 1;}
		kassaVariaatio = kassaVariaatio / 100;
		if(kassaVariaatio < 1) { kassaVariaatio = 1;} 
		// ENUM taulukko, jota käytetään luodessa kassa-palvelupisteille tapahtumat.
		TapahtumanTyyppi[] tapahtumat = { TapahtumanTyyppi.POIS1, TapahtumanTyyppi.POIS2, TapahtumanTyyppi.POIS3,
				TapahtumanTyyppi.POIS4, TapahtumanTyyppi.POIS5 };

		// koko on "kaupassa" palvelupiste, noutopiste ja loput kassoja
		palvelupisteet = new Palvelupiste[2 + kassojenMaara];

		// asiakkaiden "kaupassa" palvelupiste
		// univorm random, min aika ja max aika
		palvelupisteet[0] = new KauppaPalvelupiste(new Uniform(4, 30), tapahtumalista, TapahtumanTyyppi.KASSJON,
				"kauppa"); // saapuminen kauppaan

		// Jokainen kauppakassa luodaan erikseen, ja aktivoidaan booleantaulosta ne
		// joita käytetään

		// noutopalvelupiste
		palvelupisteet[1] = new Palvelupiste(new Normal(2, 1), tapahtumalista, TapahtumanTyyppi.POISNOUT, "noutopiste"); // noutojonoon
		//palvelupisteet[1] = new Palvelupiste(new Normal(noutoPalveluaika, 1), tapahtumalista, TapahtumanTyyppi.POISNOUT, "noutopiste"); // noutojonoon
		// loput on kassoja

		// KASSA ja siltä poistuminen

		for (int i = 2; i < kassojenMaara + 2; i++) {
			String nimi = "kassa " + (i - 1);
			palvelupisteet[i] = new Palvelupiste(new Normal(kassaPalveluaika, 1), tapahtumalista, tapahtumat[i - 2], nimi);
			//palvelupisteet[i] = new Palvelupiste(new Uniform(1, 2), tapahtumalista, tapahtumat[i - 2], nimi);
		}


		saapumisprosessi = new Saapumisprosessi(new Normal(2, 1), tapahtumalista, TapahtumanTyyppi.SAAP);
	}

	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Tapahtuma t) { // B-vaiheen tapahtumat

		switch (t.getTyyppi()) {

		case SAAP:
			Random ran = new Random();
			int arvottu = ran.nextInt(100);
			System.out.println("randon numero: " + arvottu);
			Asiakas a = new Asiakas();
			// TODO arvottavat todennäköisyydet asetetaan GUI:ssa joka simulaation alussa
			// kuten palveluajat..
			if (arvottu < noutoAsiakkaidenOsuus) {
				// noutopisteelle
				//a.setKassalleAika(Kello.getInstance().getAika());
				palvelupisteet[1].lisaaJonoon(a);
				kontrolleri.visualisoiAsiakas(1);
			} else {
				// ostoksille
				palvelupisteet[0].lisaaJonoon(a);
				kontrolleri.visualisoiAsiakas(0);
			}
			saapumisprosessi.generoiSeuraava();
			break;
		case KASSJON:
			Asiakas a1 = new Asiakas();
			a1 = palvelupisteet[0].otaJonosta();
			// arpoo mihin aktiivisen kassan kassajonoon menee
			// käytössä oleva missä lyhin jono vs random/pisin
			int kassanNumero = etsiKassa(); 	// palauttaa randmonina jonkun kassan numeron
			int	kassanNumero2 = etsiKassa(); 
			if(palvelupisteet[kassanNumero2].getJonoLength() < palvelupisteet[kassanNumero].getJonoLength()) {
				kassanNumero = kassanNumero2;
			}
			kontrolleri.poistaAsiakas(0);
			kontrolleri.visualisoiAsiakas(kassanNumero);
			

			palvelupisteet[kassanNumero].lisaaJonoon(a1);
			System.out.println("asiakas " + a1.getId() + " meni jonoon " + palvelupisteet[kassanNumero].getId());
			System.out.println("OSTOKSILLA ON " + ((KauppaPalvelupiste) palvelupisteet[0]).getAsiakasIDt());
			// lisätään asiakas kyseisen kassan jonoon
			break;
		// jokaisen kassan poistumistapahtumalle tapahtuma
		case POIS1:
			// metodi saa parametrina kassan numeron, jolta asiakas poistuu
			poistaKaupasta(1);
			break;
		case POIS2:
			poistaKaupasta(2);
			break;
		case POIS3:
			poistaKaupasta(3);
			break;
		case POIS4:
			poistaKaupasta(4);
			break;
		case POIS5:
			poistaKaupasta(5);
			break;
		case POISNOUT:
			// poistetaan noutopalvelun jonosta
			poistaKaupasta(0);
			// eli ei mene enää kassalle
			break;
		}
	}

	public int etsiKassa() {
		Random ran = new Random();
		int kassa = ran.nextInt(kassojenMaara);// + 3 jos käyttää ordinal.Tapahtumantyyppi //TODO refactoroi?
		// Sittenkin kase.. kassojenMaara random kertoo mitkä caset voi toteutua?
		if (kassa == 0) {
			return 2;
		} // indeksi palvelupisteet-taulukossa..
		if (kassa == 1) {
			return 3;
		}
		if (kassa == 2) {
			return 4;
		}
		if (kassa == 3) {
			return 5;
		} else {
			return 6;
		}
	}

	public void setKassojenMaara(int maara) {
		if (maara < 1) {
			maara = 1;
		}
		if (maara > 5) {
			maara = 5;
		}
		this.kassojenMaara = maara;
	}

	public int getKassojenMaara() {
		System.out.println("KASSOJENMAARA MOOTTORISSA " + kassojenMaara);
		return this.kassojenMaara;
	}

	public void poistaKaupasta(int kassa) {
		Palvelupiste p = palvelupisteet[kassa + 1];
		Asiakas a = p.otaJonosta();
		System.out.println(a + "lähti kassalta " + p.getId());
		kontrolleri.poistaAsiakas(kassa + 1); // +1??? että löytyy oikea kohta listasta
		a.raportti();
	}
	
	//että kontrolleri voi saada tietoja palvelupisteiltä
	/*
	 * public ArrayList<String> getTulokset() {
		ArrayList<String> tulokset = new ArrayList<String>();
		for(Palvelupiste p : palvelupisteet) {
			tulokset.add(Integer.toString(p.getPalvellutAsiakkaat()));
			tulokset.add(String.format("%.2f", p.getKayttoaste()*100));
			tulokset.add(String.format("%.2f", p.getAktiiviaika()));
		}
		return tulokset;
	}
	 */
	public ArrayList<Double> getTulokset() {
		ArrayList<Double> tulokset = new ArrayList<Double>();
		for(Palvelupiste p : palvelupisteet) {
			tulokset.add((double) p.getPalvellutAsiakkaat());
			tulokset.add(p.getKayttoaste()*100);
			tulokset.add( p.getAktiiviaika());
		}
		return tulokset;
	}
	
	public Palvelupiste[] getPalvelupisteet() {
		return this.palvelupisteet;
	}

	@Override
	protected void tulokset() {
		for (Palvelupiste p : palvelupisteet) {
			System.out.println("Palvelupiste " + p.getId() + " Aktiiviaika " + p.getAktiiviaika());
			System.out
					.println("Lyhin palveluaika " + p.getLyhinPalvelu() + " Pisin palveluaika " + p.getPisinPalvelu());
			System.out.println("Käyttöaste " + p.getKayttoaste());
		}

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
		kontrolleri.otaLopputiedot();
	}
	
	public void setNoutoAsiakasProsentti(int numero) {
		this.noutoAsiakkaidenOsuus = numero;
	}
	
	public void setPalveluajat(double aika) {
		for (int i = 2; i < palvelupisteet.length; i++)  {
			palvelupisteet[i].setPalveluaika(aika);
		}
	}
	
	
	public void setNoutoPalveluaika(double aika) {
		palvelupisteet[1].setPalveluaika(aika);
	}
	
	public double laskeKassojenKeskiarvoAsiointiAika() {
		double aika = 0;
		for(int i = 2; i < palvelupisteet.length; i++) {
			double lisattava = palvelupisteet[i].getKeskimAsiointiAika();
			if(lisattava > 0) {
				aika += lisattava;
			}
			
		}
		System.out.println("OMAMOOTTORIN laskema keskivertoaika " + aika/kassojenMaara);
		return aika/kassojenMaara;
	}
	
	public double laskeNoutoPisteenKeskiarvoAsiointiaika() {
		if(palvelupisteet[1].getPalvellutAsiakkaat() == 0) {return 0;}
		return palvelupisteet[1].getKeskimAsiointiAika();
	}
	
	public void setAsiakastiheys(double tiheys) {
		saapumisprosessi.setTiheys(tiheys);
	}
	
	public int getPalvellutAsiakkaat() {
		int asiakkaat=0;
		for(int i = 1; i < palvelupisteet.length; i++) {
			asiakkaat += palvelupisteet[i].getPalvellutAsiakkaat();
		}
		return asiakkaat;
	}

}
