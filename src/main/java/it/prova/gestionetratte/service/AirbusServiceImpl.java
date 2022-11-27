package it.prova.gestionetratte.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionetratte.dto.AirbusDTO;
import it.prova.gestionetratte.dto.TrattaDTO;
import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.repository.airbus.AirbusRepository;
import it.prova.gestionetratte.web.api.exception.AirbusNotFoundException;

@Service
public class AirbusServiceImpl implements AirbusService{

	@Autowired
	private AirbusRepository repository;
	
	public List<Airbus> listAllElements() {
		return (List<Airbus>) repository.findAll();
	}

	public Airbus caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	public Airbus caricaSingoloElementoConTratte(Long id) {
		return repository.findByIdEager(id);
	}

	@Transactional
	public Airbus aggiorna(Airbus airbusInstance) {
		return repository.save(airbusInstance);
	}

	@Transactional
	public Airbus inserisciNuovo(Airbus airbusInstance) {
		return repository.save(airbusInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.findById(idToRemove)
				.orElseThrow(() -> new AirbusNotFoundException("Airbus not found con id: " + idToRemove));
		repository.deleteById(idToRemove);
	}

	public List<Airbus> findByExample(Airbus example) {
		return repository.findByExample(example);
	}

	@Override
	public List<Airbus> listAllElementsEager() {
		return (List<Airbus>) repository.findAllEager();
	}

	@Override
	public Airbus findByCodiceAndDescrizione(String codice, String descrizione) {
		return repository.findByCodiceAndDescrizione(codice, descrizione);
	}

	@Override
	public Set<AirbusDTO> listaAirbusEvidenziandoSovrapposizioni() {
		
		List<AirbusDTO> airbusConTratte = AirbusDTO.createAirbusDTOListFromModelList(repository.findAllEager(), true, false);
		for (AirbusDTO airbusItem : airbusConTratte) {
			for (TrattaDTO trattaItem : airbusItem.getTratte()) {
				for (TrattaDTO trattaItem2 : airbusItem.getTratte()) {
					if(trattaItem.getData().isEqual(trattaItem2.getData())) {
						if ((trattaItem2.getOraDecollo().isAfter(trattaItem.getOraDecollo()) && trattaItem2.getOraDecollo().isBefore(trattaItem.getOraAtterraggio())) || 
								trattaItem2.getOraAtterraggio().isAfter(trattaItem.getOraDecollo()) && trattaItem2.getOraAtterraggio().isBefore(trattaItem.getOraAtterraggio())) {
							airbusItem.setConSovrapposizioni(true);
						}
					}
				}
			}
		}
		airbusConTratte.stream().map(airbusEntity -> {
			airbusEntity.setTratte(null);
			return airbusEntity;
		}).collect(Collectors.toList());
		 
		 Set<AirbusDTO> airbusConTratteSet = new HashSet<>(airbusConTratte);
		 return airbusConTratteSet;
	}
}
