import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayRollServices_Test {
	static PayRollService employeePayrollService;

	@BeforeClass
	public static void initializeConstructor()
	{
		employeePayrollService = new PayRollService();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		PayRollData[] arrayOfEmps = {
				new PayRollData(1, "Jeff Bezos", 100000.0),
				new PayRollData(2, "Bill Gates", 200000.0),
				new PayRollData(3, "Mark Zuckerberg", 300000.0)
		};
		employeePayrollService = new PayRollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(PayRollService.IOService.FILE_IO);
		employeePayrollService.printData(PayRollService.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(PayRollService.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		PayRollService employeePayrollService = new PayRollService();
		List<PayRollData> entries = employeePayrollService.readPayrollData(PayRollService.IOService.FILE_IO);
	}

	
}
