package it.prova.gestionetratte.web.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.gestionetratte.dto.TrattaDTO;
import it.prova.gestionetratte.model.Stato;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.service.TrattaService;
import it.prova.gestionetratte.web.api.exception.TratteNotFoundException;
import it.prova.gestionetratte.web.api.exception.IdNotNullForInsertException;
import it.prova.gestionetratte.web.api.exception.TrattaNotAnnullataException;

@RestController
@RequestMapping("api/tratta")
public class TrattaController {
	@Autowired
	private TrattaService trattaService;

	@GetMapping
	public Set<TrattaDTO> getAll() {
		List<Tratta> trattaLista = trattaService.listAllElements(true);
		Set<Tratta> trattaSet = new HashSet<>(trattaLista);
		
		return TrattaDTO.createTrattaDTOSetFromModelSet(trattaSet, true);
	}

	@PostMapping
	public TrattaDTO createNew(@Valid @RequestBody TrattaDTO trattaInput) {
		if (trattaInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		Tratta trattaInserito = trattaService.inserisciNuovo(trattaInput.buildTrattaModel());
		return TrattaDTO.buildTrattaDTOFromModel(trattaInserito, true);
	}

	@GetMapping("/{id}")
	public TrattaDTO findById(@PathVariable(value = "id", required = true) long id) {
		Tratta tratta = trattaService.caricaSingoloElementoEager(id);

		if (tratta == null)
			throw new TratteNotFoundException("Tratta not found con id: " + id);

		return TrattaDTO.buildTrattaDTOFromModel(tratta, true);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		
		if(trattaService.caricaSingoloElemento(id).getStato() != Stato.ANNULLATA)
			throw new TrattaNotAnnullataException("Tratta non annullata not deletable con id: " + id);
		
		trattaService.rimuovi(id);
	}
	
	@PutMapping("/{id}")
	public TrattaDTO update(@Valid @RequestBody TrattaDTO trattaInput, @PathVariable(required = true) Long id) {
		Tratta tratta = trattaService.caricaSingoloElemento(id);

		if (tratta == null)
			throw new TratteNotFoundException("Tratta not found con id: " + id);

		trattaInput.setId(id);
		Tratta trattaAggiornato = trattaService.aggiorna(trattaInput.buildTrattaModel());
		return TrattaDTO.buildTrattaDTOFromModel(trattaAggiornato, false);
	}
	
	@PostMapping("/search")
	public Set<TrattaDTO> search(@RequestBody TrattaDTO example) {
		List<Tratta> trattaLista = trattaService.findByExample(example.buildTrattaModel());
		Set<Tratta> trattaSet = new HashSet<>(trattaLista);
		
		return TrattaDTO.createTrattaDTOSetFromModelSet(trattaSet, false);
	}
	
	// metodo concludi tratte
	@GetMapping("/concludiTratte")
	public Set<TrattaDTO> concludiTratte() {
		
		trattaService.concludiTratte();
		
		// stampo tutto dopo aver concluso le tratte
		List<Tratta> trattaLista = trattaService.listAllElements(true);
		Set<Tratta> trattaSet = new HashSet<>(trattaLista);
		
		return TrattaDTO.createTrattaDTOSetFromModelSet(trattaSet, true);
	}
}
