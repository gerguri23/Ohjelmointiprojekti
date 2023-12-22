package controller;

import java.util.ArrayList;
import javafx.application.Platform;
import simu.framework.IMoottori;
import simu.framework.Kello;
import simu.model.*;
import view.ISimulaattorinUI;

public class Kontrolleri implements IKontrolleri { // UUSI

	private IMoottori moottori;
	private ISimulaattorinUI ui;
	private int kassoja;
	private TiedotDAO tiedotDAO = new TiedotDAO();
	private Tiedot1 teidot1;

	public Kontrolleri(ISimulaattorinUI ui) {
		this.ui = ui;
		this.kassoja = 2;
	}

	// Moottorin ohjausta:

	@Override
	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this, ui.getKassojenMaara()); // luodaan uusi moottorisäie jokaista simulointia
		moottori.setNoutoAsiakasProsentti(ui.getNoutoAsiakkaidenOsuus()); // varten;
		moottori.setSimulointiaika(ui.getAika());
		moottori.setViive(ui.getViive());
		moottori.setPalveluajat(ui.getKassaPalveluAika());
		moottori.setNoutoPalveluaika(ui.getNoutoPalveluAika());
		moottori.setAsiakastiheys(ui.getAsiakastiheys());
		tyhjennaNaytot();
		((Thread) moottori).start();
		// ((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?
	}

	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		moottori.setViive((long) (moottori.getViive() * 1.10));
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		moottori.setViive((long) (moottori.getViive() * 0.9));
	}

	public void setKassojenMaara(int maara) {
		this.kassoja = maara;
	}

	public int getKassojenMaara() {
		return this.kassoja;
	}

	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata
	// JavaFX-säikeeseen:

	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(() -> ui.setLoppuaika(aika));
	}

	public void valmistauduUudelleen() {
		ui.vapautaKNappi();
		Kello.getInstance().setAika(0);
		// luodaan asiakas, jotta voidaan resetoida id numero...
		Asiakas a = new Asiakas();
		a.resetId();
	}

	@Override
	public void visualisoiAsiakas(int palvelupiste) {

		Platform.runLater(new Runnable() {
			public void run() {
				ui.getVisualisointi(palvelupiste).uusiAsiakas();
			}
		});
	}

	public void poistaAsiakas(int palvelupiste) {
		Platform.runLater(new Runnable() {
			public void run() {
				ui.getVisualisointi(palvelupiste).poistaAsiakas();
			}
		});
	}

	public void otaLopputiedot() {
		ArrayList<Double> tiedot = moottori.getTulokset();
		// poistaa kassapalvelupisteen tulokset
		tiedot.remove(0);
		tiedot.remove(0);
		tiedot.remove(0);

		Double[] tulokset = new Double[tiedot.size()];
		for (int i = 0; i < tiedot.size(); i++) {
			tulokset[i] = tiedot.get(i);
		}

		System.out.println("Lopputietoja arraylist");
		for (Double s : tiedot) {
			System.out.println(s);
		}
		System.out.println("Stringitaulukko");
		for (Double t : tulokset) {
			System.out.println(t);
		}
		ui.asetaTekstit(tulokset);

		// DAO:lle lahetettava lista.. toisessa järjestyksessä kuin tiedot-arrayList..
		ArrayList<Double> lahetettavat = new ArrayList<Double>();
		lahetettavat.add(Double.valueOf(ui.getKassojenMaara())); // paikka: 0
		lahetettavat.add(Double.valueOf(ui.getNoutoAsiakkaidenOsuus())); // 1
		lahetettavat.add((double) ui.getKassaPalveluAika()); // 2
		lahetettavat.add((double) ui.getNoutoPalveluAika()); // 3
		lahetettavat.add(ui.getAsiakastiheys()); // 4

		double lisattava = moottori.laskeKassojenKeskiarvoAsiointiAika();
		ui.setKeskimPalveluaika(lisattava);
		lahetettavat.add(lisattava); // 5
		lisattava = moottori.laskeNoutoPisteenKeskiarvoAsiointiaika();
		ui.setKeskimNoutoPalveluaika(lisattava);
		lahetettavat.add(lisattava); // 6
		int asiakkaat = moottori.getPalvellutAsiakkaat();
		ui.setPalvellutAsiakkaat(asiakkaat);
		lahetettavat.add(Double.valueOf(asiakkaat)); // 7 (paikka lahetettavat - listassa

		// laitetaan statistiikat DAO:lle lahetettaviin
		for (Double d : tiedot) {
			lahetettavat.add(d);
		}

		System.out.println("TULOSTETAAN KAIKKI tiedot arraylistista");
		for (Object o : lahetettavat) {
			System.out.println(o);
		}
		teidot1 = new Tiedot1(lahetettavat);
		teidot1.Tulosta();
		tiedotDAO.createTiedot(lahetettavat);

	}

	// käskee UI:ta tyhjentämään jokaisen indeksin näytön
	// getVisualisointi-komennon avulla, joka hakee näytön
	// samalla indeksin numerolla
	private void tyhjennaNaytot() {
		for (int i = 0; i < 7; i++) {
			ui.getVisualisointi(i).tyhjennaNaytto();
		}
		ui.nollaaStatsit();
	}

}