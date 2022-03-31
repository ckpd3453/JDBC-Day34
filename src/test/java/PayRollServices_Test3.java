import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayRollServices_Test3 {
	static PayRollService3 employeePayrollService;

	@BeforeClass
	public static void initializeConstructor()
	{
		employeePayrollService = new PayRollService3();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		PayRollData3[] arrayOfEmps = {
				new PayRollData3(1, "Jeff Bezos", 100000.0),
				new PayRollData3(2, "Bill Gates", 200000.0),
				new PayRollData3(3, "Mark Zuckerberg", 300000.0)
		};
		employeePayrollService = new PayRollService3(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(PayRollService3.IOService.FILE_IO);
		employeePayrollService.printData(PayRollService3.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(PayRollService3.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		PayRollService3 employeePayrollService = new PayRollService3();
		List<PayRollData3> entries = employeePayrollService.readPayrollData(PayRollService3.IOService.FILE_IO);
	}

	@Test
	public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayRollExceptions3 {
		List<PayRollData3> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService3.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayRollExceptions3 {
		List<PayRollData3> employeePayrollData = employeePayrollService.readEmployeePayrollData(PayRollService3.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Teresa",3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Teresa");
		Assert.assertTrue(result);
	}
}
