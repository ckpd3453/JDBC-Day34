
public class PayRollExceptions4 extends Exception {
	enum ExceptionType{
		CONNECTION_PROBLEM, RETRIEVAL_PROBLEM,UPDATE_PROBLEM; 
	}

	ExceptionType type;
	PayRollExceptions4(String message, ExceptionType type){
		super(message);
		this.type = type;
	}
}
