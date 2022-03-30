import java.util.List;
import java.util.Scanner;

public class PayRollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	/* Welcome Message */
	public void printWelcomeMessage() {
		System.out.println("Welcome to the Employee PayRoll Service Program");
	}

	private static List<PayRollData> employeePayrollList;

	public PayRollService(List<PayRollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	public PayRollService() {
	}

	public static void main(String[] args) {
		PayRollService employeePayrollService = new PayRollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
	}

	/* Read Employee Payroll data from console */
	public void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new PayRollData(id, name, salary));
	}

	/* Write Employee Payroll data to console */
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new PayRollFileIO().writeData(employeePayrollList);
		}
	}

	/* Print Employee Payroll */
	public void printData(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			new PayRollFileIO().printData();
		}

	}

	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			return new PayRollFileIO().countEntries();
		}
		return 0;
	}

	public List<PayRollData> readPayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new PayRollFileIO().readData();
		return employeePayrollList;
	}

	public List<PayRollData> readEmployeePayrollData(IOService ioService) throws PayRollExceptions {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = new DatabaseConnection1().readData();
		return employeePayrollList;
	}

}
