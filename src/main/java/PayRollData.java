import java.time.LocalDate;

public class PayRollData {
	private int id;
	private String name;
	private double salary;
	private LocalDate start;

	/* Constructor */
	public PayRollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	/* Constructor */
	public PayRollData(int id, String name, double salary, LocalDate start) {
		this(id,name,salary);
		this.start = start;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [ID=" + id + ", Name=" + name + ", Salary=" + salary + ", Start=" + start + "]";
	}
}
