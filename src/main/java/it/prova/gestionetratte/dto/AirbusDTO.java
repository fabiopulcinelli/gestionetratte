package it.prova.gestionetratte.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.gestionetratte.model.Airbus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirbusDTO {
	private Long id;

	@NotBlank(message = "{airbus.codice.notblank}")
	private String codice;

	@NotBlank(message = "{airbus.descrizione.notblank}")
	private String descrizione;

	@NotNull(message = "{airbus.dataInizioServizio.notnull}")
	private LocalDate dataInizioServizio;

	@NotNull(message = "{airbus.numeroPasseggeri.notnull}")
	private int numeroPasseggeri;

	@JsonIgnoreProperties(value = { "airbus" })
	private Set<TrattaDTO> tratte = new HashSet<TrattaDTO>(0);
	
	
	private Boolean conSovrapposizioni;
	
	public AirbusDTO() {	
	}

	public AirbusDTO(Long id, String codice, String descrizione, LocalDate dataInizioServizio, int numeroPasseggeri) {
		super();
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
		this.dataInizioServizio = dataInizioServizio;
		this.numeroPasseggeri = numeroPasseggeri;
	}

	public AirbusDTO(String codice, String descrizione, LocalDate dataInizioServizio, int numeroPasseggeri) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
		this.dataInizioServizio = dataInizioServizio;
		this.numeroPasseggeri = numeroPasseggeri;
	}

	public AirbusDTO(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDate getDataInizioServizio() {
		return dataInizioServizio;
	}

	public void setDataInizioServizio(LocalDate dataInizioServizio) {
		this.dataInizioServizio = dataInizioServizio;
	}

	public int getNumeroPasseggeri() {
		return numeroPasseggeri;
	}

	public void setNumeroPasseggeri(int numeroPasseggeri) {
		this.numeroPasseggeri = numeroPasseggeri;
	}

	public Set<TrattaDTO> getTratte() {
		return tratte;
	}

	public void setTratte(Set<TrattaDTO> tratte) {
		this.tratte = tratte;
	}
	
	public Boolean isConSovrapposizioni() {
		return conSovrapposizioni;
	}

	public void setConSovrapposizioni(Boolean conSovrapposizioni) {
		this.conSovrapposizioni = conSovrapposizioni;
	}

	public Airbus buildAirbusModel() {
		return new Airbus(this.id, this.codice, this.descrizione, this.dataInizioServizio, this.numeroPasseggeri);
	}
	
	public static AirbusDTO buildAirbusDTOFromModel(Airbus airbusModel, boolean includeTratte, Boolean haSovrapposizioni) {
		AirbusDTO result = new AirbusDTO(airbusModel.getId(), airbusModel.getCodice(), airbusModel.getDescrizione(),
				airbusModel.getDataInizioServizio(), airbusModel.getNumeroPasseggeri());
		if(includeTratte)
			result.setTratte(TrattaDTO.createTrattaDTOSetFromModelSet(airbusModel.getTratte(), false));
		if(haSovrapposizioni)
			result.setConSovrapposizioni(true);
		return result;
	}
	
	public static List<AirbusDTO> createAirbusDTOListFromModelList(List<Airbus> modelListInput, boolean includeTratte, Boolean haSovrapposizioni) {
		return modelListInput.stream().map(airbusEntity -> {
			AirbusDTO result = AirbusDTO.buildAirbusDTOFromModel(airbusEntity,includeTratte,haSovrapposizioni);
			if(includeTratte)
				result.setTratte(TrattaDTO.createTrattaDTOSetFromModelSet(airbusEntity.getTratte(), false));
			if(haSovrapposizioni)
				result.setConSovrapposizioni(true);
			return result;
		}).collect(Collectors.toList());
	}
}
