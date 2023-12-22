package simu.model;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TiedotDAO implements ITiedotDAO {

	private SessionFactory istuntotehdas = null;

	public TiedotDAO() {
		try {
			istuntotehdas = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			System.err.println("Istuntotehtaan luonti ei onnistunut: " + e.getMessage());
			System.exit(-1);
		}

	}

	@Override
	public boolean createTiedot(ArrayList<Double> info) {
		if (readTiedote() != null) {
			System.out.println("Createpalvelupiste metodi");
			return false;
		}
		Tiedot1 tiedot1 = new Tiedot1(info);
		return updateTiedot(tiedot1);
	}

	@Override
	public Tiedot1 readTiedote() {
		Tiedot1 tiedot1 = null;
		try (Session iSession = istuntotehdas.openSession()) {
			tiedot1 = iSession.get(Tiedot1.class, null);
		} catch (Exception e) {
			System.out.println(e);
		}
		return tiedot1;
	}

	@Override
	public Tiedot1[] readTiedot() {
		List<Tiedot1> va = null;
		try (Session session = istuntotehdas.openSession()) {
			va = session.createQuery("From hypermarket").getResultList();
		} catch (Exception e) {
			System.err.println(e);
		}
		Tiedot1[] tiedot1 = new Tiedot1[va.size()];
		return (Tiedot1[]) va.toArray(tiedot1);

	}

	@Override
	public boolean updateTiedot(Tiedot1 tiedot1) {
		Transaction transaction = null;
		try (Session session = istuntotehdas.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(tiedot1);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.err.println(e);
		}
		return false;
	}

	public void finalize() {
		try {
			if (istuntotehdas != null)
				istuntotehdas.close();
		} catch (Exception e) {
			System.err.println("Istuntotehtaan sulkeminen epäonnistui: " + e.getMessage());
		}
	}

}