import java.util.List;
import java.util.Scanner;

public class PayRollService4 {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	
	private DatabaseConnection4 employeePayrollDBService;
	
	/* Welcome Message */
	public void printWelcomeMessage() {
		System.out.println("Welcome to the Employee PayRoll Service Program");
	}

	private static List<PayRollData4> employeePayrollList;

	public PayRollService4(List<PayRollData4> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public PayRollService4() {
		employeePayrollDBService = DatabaseConnection4.getInstance();
	}

	public static void main(String[] args) {
		PayRollService4 employeePayrollService = new PayRollService4(employeePayrollList);
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
		employeePayrollList.add(new PayRollData4(id, name, salary));
	}

	/* Write Employee Payroll data to console */
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new PayRollFileIO4().writeData(employeePayrollList);
		}
	}

	/* Print Employee Payroll */
	public void printData(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			new PayRollFileIO4().printData();
		}

	}

	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			return new PayRollFileIO4().countEntries();
		}
		return 0;
	}

	public List<PayRollData4> readPayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new PayRollFileIO4().readData();
		return employeePayrollList;
	}

	public List<PayRollData4> readEmployeePayrollData(IOService ioService) throws PayRollExceptions4 {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return employeePayrollList;
	}
	
	public void updateEmployeeSalary(String name, double salary) throws PayRollExceptions4 {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		PayRollData4 employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);

	}

	private PayRollData4 getEmployeePayrollData(String name) {
		PayRollData4 employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
				.findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<PayRollData4> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));

	}
}
