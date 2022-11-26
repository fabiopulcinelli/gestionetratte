package it.prova.gestionetratte.web.api.exception;

public class TrattaNotAnnullataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TrattaNotAnnullataException(String message) {
		super(message);
	}

}