import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayRollServices_Test4 {
	static PayRollService4 employeePayrollService;

	@BeforeClass
	public static void initializeConstructor()
	{
		employeePayrollService = new PayRollService4();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		PayRollData4[] arrayOfEmps = {
				new PayRollData4(1, "Jeff Bezos", 100000.0),
				new PayRollData4(2, "Bill Gates", 200000.0),
				new PayRollData4(3, "Mark Zuckerberg", 300000.0)
		};
		employeePayrollService = new PayRollService4(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(PayRollService4.IOService.FILE_IO);
		employeePayrollService.printData(PayRollService4.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(PayRollService4.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		PayRollService4 employeePayrollService = new PayRollService4();
		List<PayRollData4> entries = employeePayrollService.readPayrollData(PayRollService4.IOService.FILE_IO);
	}

	@Test
	public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayRollExceptions4 {
		List<PayRollData4> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService4.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayRollExceptions4 {
		List<PayRollData4> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService4.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Teresa",3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Teresa");
		Assert.assertTrue(result);
	}
}
