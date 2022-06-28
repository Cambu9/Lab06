package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.CittaDao;
import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private List<Citta> allCitta;
	private List<Citta> best;
	
	int umidita;
	int utot;
	int umedia;
	int i;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		MeteoDAO dao = new MeteoDAO();
		this.allCitta = dao.getAllCitta();

	}

	// of course you can change the String output with what you think works best
	public double getUmiditaMedia(int mese, String localita) {
		MeteoDAO dao = new MeteoDAO();
		return dao.getAvgRilevamentiLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale = new ArrayList<Citta>();
		this.best = null;
		
		MeteoDAO dao = new MeteoDAO();
		
		for(Citta c: allCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		cerca(parziale,0);
		return best;
	}

	private void cerca(List<Citta> parziale, int livello) {
		
		if(livello==NUMERO_GIORNI_TOTALI) {
			double costo = calcolaCosto(parziale);
			
			if(best==null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
		} else {
			for (Citta prova: allCitta) {
				if(aggiuntaValida(parziale, prova)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
		
	}

	private boolean aggiuntaValida(List<Citta> parziale, Citta prova) {
		
		int conta = 0;
		for(Citta precedente: parziale) {
			if(precedente.equals(prova)) {
				conta += 1;
			}
		}
		if(conta >= NUMERO_GIORNI_CITTA_MAX) 
			return false;
		
		if(parziale.size()==0)
			return true;
		
		if(parziale.size()==1 || parziale.size()==2)
			return parziale.get(parziale.size()-1).equals(prova);
		
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
	}

	public List<Citta> getAllCitta() {
		return allCitta;
	}

	public void setAllCitta(List<Citta> allCitta) {
		this.allCitta = allCitta;
	}

	public List<Citta> getBest() {
		return best;
	}

	public void setBest(List<Citta> best) {
		this.best = best;
	}

	private double calcolaCosto(List<Citta> parziale) {
		double costo = 0.0;
		
		for(int giorno = 1; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			Citta c = parziale.get(giorno-1);
			double umidita = c.getRilevamenti().get(giorno-1).getUmidita();
			
			costo += umidita;
		}
		
		for(int giorno = 2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo += COST;
			}
		}
		
		return costo;
	}
	

}
