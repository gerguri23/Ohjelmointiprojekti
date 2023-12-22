package simu.model;

import java.util.ArrayList;

public interface ITiedotDAO {

	public boolean createTiedot(ArrayList<Double> info);

	Tiedot1 readTiedote();

	Tiedot1[] readTiedot();

	public boolean updateTiedot(Tiedot1 tiedot1);

}
