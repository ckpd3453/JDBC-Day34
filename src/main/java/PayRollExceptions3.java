
public class PayRollExceptions3 extends Exception {
	enum ExceptionType{
		CONNECTION_PROBLEM, RETRIEVAL_PROBLEM,UPDATE_PROBLEM; 
	}

	ExceptionType type;
	PayRollExceptions3(String message, ExceptionType type){
		super(message);
		this.type = type;
	}
}
