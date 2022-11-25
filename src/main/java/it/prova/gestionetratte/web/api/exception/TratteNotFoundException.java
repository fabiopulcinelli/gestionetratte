package it.prova.gestionetratte.web.api.exception;

public class TratteNotFoundException  extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TratteNotFoundException(String message) {
		super(message);
	}
}