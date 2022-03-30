import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayRollServices_Test2 {
	static PayRollService2 employeePayrollService;

	@BeforeClass
	public static void initializeConstructor()
	{
		employeePayrollService = new PayRollService2();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		PayRollData2[] arrayOfEmps = {
				new PayRollData2(1, "Jeff Bezos", 100000.0),
				new PayRollData2(2, "Bill Gates", 200000.0),
				new PayRollData2(3, "Mark Zuckerberg", 300000.0)
		};
		employeePayrollService = new PayRollService2(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(PayRollService2.IOService.FILE_IO);
		employeePayrollService.printData(PayRollService2.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(PayRollService2.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		PayRollService2 employeePayrollService = new PayRollService2();
		List<PayRollData2> entries = employeePayrollService.readPayrollData(PayRollService2.IOService.FILE_IO);
	}

	@Test
	public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayRollExceptions2 {
		List<PayRollData2> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService2.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayRollExceptions2 {
		List<PayRollData2> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService2.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Teresa",3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Teresa");
		Assert.assertTrue(result);
	}
}
