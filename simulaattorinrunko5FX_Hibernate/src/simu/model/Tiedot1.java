package simu.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hypermarket")
public class Tiedot1 {

	@Id
	@GeneratedValue
	@Column
	private int id;

	@Column
	private double KassojenMaara;

	@Column
	private double NoutoAsiakkaidenOsuus;

	@Column
	private double KassaPalveluAika;

	@Column
	private double NoutoPalveluAika;

	@Column
	private double Asiakastiheys;

	@Column
	private double laskeKassojenKeskiarvoAsiointiAika;

	@Column
	private double laskeNoutoPisteenKeskiarvoAsiointiaika;

	@Column
	private double PalvellutAsiakkaat;

	@Column
	private int NoutokassaPalvellutAsiakkaat;

	@Column
	private double NoutokassaKäyttöaste;

	@Column
	private double NoutokassaAktiiviaika;

	@Column
	private double kassa1PalvellutAsiakkaat;

	@Column
	private double kassa1Käyttöaste;

	@Column
	private double kassa1Aktiiviaika;

	@Column
	private double kassa2PalvellutAsiakkaat;

	@Column
	private double kassa2Käyttöaste;

	@Column
	private double kassa2Aktiiviaika;

	@Column
	private double kassa3PalvellutAsiakkaat;

	@Column
	private double kassa3Käyttöaste;

	@Column
	private double kassa3Aktiiviaika;

	@Column
	private double kassa4PalvellutAsiakkaat;

	@Column
	private double kassa4Käyttöaste;

	@Column
	private double kassa4Aktiiviaika;

	@Column
	private double kassa5PalvellutAsiakkaat;

	@Column
	private double kassa5Käyttöaste;

	@Column
	private double kassa5Aktiiviaika;

	@Column
	private String getTime;

	public Tiedot1(ArrayList<Double> info) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println("Time: " + dtf.format(now));
		this.getTime = dtf.format(now);

		try {
			this.KassojenMaara = info.get(0).intValue();
			this.NoutoAsiakkaidenOsuus = info.get(1).intValue();
			this.KassaPalveluAika = info.get(2);
			this.NoutoPalveluAika = info.get(3);
			this.Asiakastiheys = info.get(4).doubleValue();
			this.laskeKassojenKeskiarvoAsiointiAika = info.get(5);
			this.laskeNoutoPisteenKeskiarvoAsiointiaika = info.get(6);
			this.PalvellutAsiakkaat = info.get(7).intValue();
			this.NoutokassaPalvellutAsiakkaat = info.get(8).intValue();
			this.NoutokassaKäyttöaste = info.get(9);
			this.NoutokassaAktiiviaika = info.get(10);

			this.kassa1PalvellutAsiakkaat = info.get(11).intValue();
			this.kassa1Käyttöaste = info.get(12);
			this.kassa1Aktiiviaika = info.get(13).doubleValue();

			this.kassa2PalvellutAsiakkaat = info.get(14).intValue();
			this.kassa2Käyttöaste = info.get(15);
			this.kassa2Aktiiviaika = info.get(16);

			this.kassa3PalvellutAsiakkaat = info.get(17).intValue();
			this.kassa3Käyttöaste = info.get(18);
			this.kassa3Aktiiviaika = info.get(19);

			this.kassa4PalvellutAsiakkaat = info.get(20).intValue();
			this.kassa4Käyttöaste = info.get(21);
			this.kassa4Aktiiviaika = info.get(22);

			this.kassa5PalvellutAsiakkaat = info.get(23).intValue();
			this.kassa5Käyttöaste = info.get(24);
			this.kassa5Aktiiviaika = info.get(25);

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void Tulosta() {
		System.out.println("SSS " + KassojenMaara);
		System.out.println("SSS " + NoutoAsiakkaidenOsuus);
		System.out.println("SSS " + KassaPalveluAika);
		System.out.println("SSS " + NoutoPalveluAika);
		System.out.println("SSS " + Asiakastiheys);
		System.out.println("SSS " + laskeKassojenKeskiarvoAsiointiAika);
		System.out.println("SSS " + laskeNoutoPisteenKeskiarvoAsiointiaika);
		System.out.println("SSS " + PalvellutAsiakkaat);
		System.out.println("SSS " + NoutokassaPalvellutAsiakkaat);
		System.out.println("SSS " + NoutokassaKäyttöaste);
		System.out.println("SSS " + NoutokassaAktiiviaika);

		System.out.println("SSS " + kassa1PalvellutAsiakkaat);
		System.out.println("SSS " + kassa1Käyttöaste);
		System.out.println("SSS " + kassa1Aktiiviaika);

		System.out.println("SSS " + kassa2PalvellutAsiakkaat);
		System.out.println("SSS " + kassa2Käyttöaste);
		System.out.println("SSS " + kassa2Aktiiviaika);

		System.out.println("SSS " + kassa3PalvellutAsiakkaat);
		System.out.println("SSS " + kassa3Käyttöaste);
		System.out.println("SSS " + kassa3Aktiiviaika);

		System.out.println("SSS " + kassa4PalvellutAsiakkaat);
		System.out.println("SSS " + kassa4Käyttöaste);
		System.out.println("SSS " + kassa4Aktiiviaika);

		System.out.println("SSS " + kassa5PalvellutAsiakkaat);
		System.out.println("SSS " + kassa5Käyttöaste);
		System.out.println("SSS " + kassa5Aktiiviaika);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println("Time: " + dtf.format(now));
	}

}
