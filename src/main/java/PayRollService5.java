import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PayRollService5 {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	
	private DatabaseConnection5 employeePayrollDBService;
	
	/* Welcome Message */
	public void printWelcomeMessage() {
		System.out.println("Welcome to the Employee PayRoll Service Program");
	}

	private static List<PayRollData5> employeePayrollList;

	public PayRollService5(List<PayRollData5> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public PayRollService5() {
		employeePayrollDBService = DatabaseConnection5.getInstance();
	}

	public static void main(String[] args) {
		PayRollService5 employeePayrollService = new PayRollService5(employeePayrollList);
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
		employeePayrollList.add(new PayRollData5(id, name, salary));
	}

	/* Write Employee Payroll data to console */
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new PayRollFileIO5().writeData(employeePayrollList);
		}
	}

	/* Print Employee Payroll */
	public void printData(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			new PayRollFileIO5().printData();
		}

	}

	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			return new PayRollFileIO5().countEntries();
		}
		return 0;
	}

	public List<PayRollData5> readPayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new PayRollFileIO5().readData();
		return employeePayrollList;
	}

	public List<PayRollData5> readEmployeePayrollData(IOService ioService) throws PayRollExceptions5 {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return employeePayrollList;
	}
	
	public void updateEmployeeSalary(String name, double salary) throws PayRollExceptions5 {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		PayRollData5 employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);

	}

	private PayRollData5 getEmployeePayrollData(String name) {
		PayRollData5 employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
				.findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<PayRollData5> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));

	}
	public List<PayRollData5> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) throws PayRollExceptions5 {
		if(ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getEmployeeForDateRange(startDate, endDate);

		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService ioService) throws PayRollExceptions5 {
		if (ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getAverageSalaryByGender();
		return null;
	}

	public Map<String, Double> readCountByGender(IOService ioService) throws PayRollExceptions5 {
		if (ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getCountByGender();
		return null;
	}

	public Map<String, Double> readMinumumSalaryByGender(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getMinimumByGender();
		return null;
	}

	public Map<String, Double> readMaximumSalaryByGender(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getMaximumByGender();
		return null;
	}

	public Map<String, Double> readSumSalaryByGender(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			return employeePayrollDBService.getSalarySumByGender();
		return null;
	}
}
