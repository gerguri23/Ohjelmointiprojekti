package simu.model;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum TapahtumanTyyppi {
	SAAP,//kauppaan saapuminen
	KASSJON,//kassajonoon saapuminen
	//KASSA1, KASSA2, KASSA3, KASSA4, KASSA5, // TODO poista tämä rivi kokonaan kaikkien kassojen ENUMIT
	POIS1, POIS2, POIS3, POIS4, POIS5,	//	poistuminen kaupasta(kassapalvelupisteeltä
	NOUTO,	//saapuminen noutopistejonoon
	POISNOUT	//poistuminen noutopalvelupisteeltä
	
	//eka enum on 1
	
	//jokaisell kassalle oma enum, sillä on indeksi ordinal.TapahtumanTyyppi

}
