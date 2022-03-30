import java.util.List;
import java.util.Scanner;

public class PayRollService2 {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	
	private DatabaseConnection2 employeePayrollDBService;
	
	/* Welcome Message */
	public void printWelcomeMessage() {
		System.out.println("Welcome to the Employee PayRoll Service Program");
	}

	private static List<PayRollData2> employeePayrollList;

	public PayRollService2(List<PayRollData2> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public PayRollService2() {
		employeePayrollDBService = DatabaseConnection2.getInstance();
	}

	public static void main(String[] args) {
		PayRollService2 employeePayrollService = new PayRollService2(employeePayrollList);
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
		employeePayrollList.add(new PayRollData2(id, name, salary));
	}

	/* Write Employee Payroll data to console */
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new PayRollFileIO2().writeData(employeePayrollList);
		}
	}

	/* Print Employee Payroll */
	public void printData(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			new PayRollFileIO2().printData();
		}

	}

	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			return new PayRollFileIO2().countEntries();
		}
		return 0;
	}

	public List<PayRollData2> readPayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new PayRollFileIO2().readData();
		return employeePayrollList;
	}

	public List<PayRollData2> readEmployeePayrollData(IOService ioService) throws PayRollExceptions2 {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return employeePayrollList;
	}
	
	public void updateEmployeeSalary(String name, double salary) throws PayRollExceptions2 {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		PayRollData2 employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);

	}

	private PayRollData2 getEmployeePayrollData(String name) {
		PayRollData2 employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
				.findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<PayRollData2> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));

	}
}
