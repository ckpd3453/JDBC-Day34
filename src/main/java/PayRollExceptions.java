
public class PayRollExceptions extends Exception {
	enum ExceptionType{
		CONNECTION_PROBLEM, RETRIEVAL_PROBLEM; 
	}

	ExceptionType type;
	PayRollExceptions(String message, ExceptionType type){
		super(message);
		this.type = type;
	}
}
