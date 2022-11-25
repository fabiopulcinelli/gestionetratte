package it.prova.gestionetratte;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.gestionetratte.service.AirbusService;
import it.prova.gestionetratte.service.TrattaService;
import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.model.Stato;

@SpringBootApplication
public class GestionetratteApplication implements CommandLineRunner {

	@Autowired
	private AirbusService airbusService;
	@Autowired
	private TrattaService trattaService;

	public static void main(String[] args) {
		SpringApplication.run(GestionetratteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String Codice = "X1";
		String AirbusA380_800 = "Airbus A380-800";
		Airbus airbusX1 = airbusService.findByCodiceAndDescrizione(Codice, AirbusA380_800);

		LocalDate data1 = LocalDate.parse("2020-01-08");
		if (airbusX1 == null) {
			airbusX1 = new Airbus(Codice, AirbusA380_800, data1, 853);
			airbusService.inserisciNuovo(airbusX1);
		}

		LocalDate data2 = LocalDate.parse("2020-03-31");
		LocalTime tempo1 = LocalTime.parse("10:00");
		LocalTime tempo2 = LocalTime.parse("11:30");
		Tratta Napoli_Milano = new Tratta("HJKGJ678", "Napoli-Milano", data2, tempo1, tempo2, Stato.ATTIVA, airbusX1);
		if (trattaService.findByCodiceAndDescrizione(Napoli_Milano.getDescrizione(), Napoli_Milano.getDescrizione())==null)
			trattaService.inserisciNuovo(Napoli_Milano);
	}
}
