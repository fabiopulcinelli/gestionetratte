package it.prova.gestionetratte.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.gestionetratte.model.Stato;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.repository.tratta.TrattaRepository;
import it.prova.gestionetratte.web.api.exception.TratteNotFoundException;

@Service
public class TrattaServiceImpl implements TrattaService{

	@Autowired
	private TrattaRepository repository;
	
	@Override
	public List<Tratta> listAllElements(boolean eager) {
		if (eager)
			return (List<Tratta>) repository.findAllTratteEager();

		return (List<Tratta>) repository.findAll();
	}

	@Override
	public Tratta caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Tratta caricaSingoloElementoEager(Long id) {
		return repository.findSingleTrattaEager(id);
	}

	@Override
	public Tratta aggiorna(Tratta trattaInstance) {
		return repository.save(trattaInstance);
	}

	@Override
	public Tratta inserisciNuovo(Tratta trattaInstance) {
		return repository.save(trattaInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) {
		repository.findById(idToRemove)
			.orElseThrow(() -> new TratteNotFoundException("Tratta not found con id: " + idToRemove));
		repository.deleteById(idToRemove);
	}

	@Override
	public List<Tratta> findByExample(Tratta example) {
		return repository.findByExample(example);
	}

	@Override
	public Tratta findByCodiceAndDescrizione(String codice, String descrizione) {
		return repository.findByCodiceAndDescrizione(codice, descrizione);
	}

	@Override
	public void concludiTratte() {
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		
		for(Tratta trattaItem: repository.findAll()) {
			if(trattaItem.getStato() == Stato.ATTIVA && date.isAfter(trattaItem.getData())) {
				trattaItem.setStato(Stato.CONCLUSA);
				repository.save(trattaItem);
			}
			if(trattaItem.getStato() == Stato.ATTIVA && date.isEqual(trattaItem.getData())) {
				if(time.isAfter(trattaItem.getOraAtterraggio())) {
					trattaItem.setStato(Stato.CONCLUSA);
					repository.save(trattaItem);
				}
			}
		}
	}

}
