import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import PayRollService5.IOService;




public class PayRollServices_Test5 {
	static PayRollService5 employeePayrollService;

	@BeforeClass
	public static void initializeConstructor()
	{
		employeePayrollService = new PayRollService5();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		PayRollData5[] arrayOfEmps = {
				new PayRollData5(1, "Jeff Bezos", 100000.0),
				new PayRollData5(2, "Bill Gates", 200000.0),
				new PayRollData5(3, "Mark Zuckerberg", 300000.0)
		};
		employeePayrollService = new PayRollService5(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(PayRollService5.IOService.FILE_IO);
		employeePayrollService.printData(PayRollService5.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(PayRollService5.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		PayRollService5 employeePayrollService = new PayRollService5();
		List<PayRollData5> entries = employeePayrollService.readPayrollData(PayRollService5.IOService.FILE_IO);
	}

	@Test
	public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayRollExceptions5 {
		List<PayRollData5> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService5.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayRollExceptions5 {
		List<PayRollData5> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService5.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Teresa",3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Teresa");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() throws PayRollExceptions5 {
		PayRollService5 employeePayrollService = new PayRollService5();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<PayRollData5> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue()
			throws PayRollExceptions5 {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) && averageSalaryByGender.get("F").equals(3000000.00));
	}

//	@Test
//	public void givnNewEmployee_WhenAdded_ShouldSyncEithDB() {
//		
//	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperCountValue()
			throws PayRollExceptions5 {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> countByGender = employeePayrollService.readCountByGender(IOService.DB_IO);
		Assert.assertTrue(countByGender.get("M").equals(2.0) && countByGender.get("F").equals(1.0));
	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMinimumValue()
			throws PayRollExceptions5 {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> countByGender = employeePayrollService.readMinumumSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(countByGender.get("M").equals(1000000.00) && countByGender.get("F").equals(3000000.00));
	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMaximumValue()
			throws PayRollExceptions5 {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> countByGender = employeePayrollService.readMaximumSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(countByGender.get("M").equals(3000000.00) && countByGender.get("F").equals(3000000.00));
	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperSumValue()
			throws PayRollExceptions5 {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> sumSalaryByGender = employeePayrollService.readSumSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(sumSalaryByGender.get("M").equals(4000000.00) && sumSalaryByGender.get("F").equals(3000000.00));
	}
}
