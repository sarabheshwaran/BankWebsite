package uub.staticlayer;

public class CustomBankException extends Exception{
	private static final long serialVersionUID = 1L;
	public CustomBankException(String message){
		super(message);
	}
	public CustomBankException(String message,Exception cause){
		super(message,cause);
	}
	
	public <T> CustomBankException(T message ,Exception cause){
		super(message.toString(),cause);
	}

	public CustomBankException() {
		super();
	}
}
