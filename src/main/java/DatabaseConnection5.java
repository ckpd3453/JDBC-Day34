import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 1.creating connection with dataBase payroll Service
 */
public class DatabaseConnection5 {
	private PreparedStatement employeePayrollDataStatement;
	private static DatabaseConnection5 employeePayrollDBService;

	DatabaseConnection5() {

	}

	private Connection getConnection() throws SQLException {
		String connectionUrl = "jdbc:mysql://localhost:3306/payroll_services";
		String userName = "root";
		String password = "Revenge@3453";
		Connection con;
		System.out.println("connecting tothe database:" + connectionUrl);
		con = DriverManager.getConnection(connectionUrl, userName, password);
		System.out.println("connection is successful..." + con);
		return con;
	}

	public static DatabaseConnection5 getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new DatabaseConnection5();
		return employeePayrollDBService;
	}

	public List<PayRollData5> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate)
			throws PayRollExceptions5 {
		String sql = String.format("select * from employee_payroll where start between '%s' and '%s';",
				Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getEmployeePayrollDataUsingDB(sql);
	}

	public Map<String, Double> getAverageSalaryByGender() throws PayRollExceptions5 {
		String sql = "select gender,avg(salary) as avg_salary from employee_payroll group by gender";
		return getAggregateByGender("gender", "avg_salary", sql);
	}

	public Map<String, Double> getAggregateByGender(String gender, String aggregate, String sql) {
		Map<String, Double> genderCountMap = new HashMap<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String getgender = result.getString(gender);
				Double count = result.getDouble(aggregate);
				genderCountMap.put(getgender, count);
			}
		} catch (SQLException e) {
			e.getMessage();
		}
		return genderCountMap;
	}

	public Map<String, Double> getCountByGender() {
		String sql = "select gender,count(salary) as count_gender from employee_payroll group by gender";
		return getAggregateByGender("gender", "count_gender", sql);
	}

	public Map<String, Double> getMinimumByGender() {
		String sql = "select gender,min(salary) as minSalary_gender from employee_payroll group by gender";
		return getAggregateByGender("gender", "minSalary_gender", sql);
	}

	public Map<String, Double> getMaximumByGender() {
		String sql = "select gender,max(salary) as maxSalary_gender from employee_payroll group by gender";
		return getAggregateByGender("gender", "maxSalary_gender", sql);
	}

	public Map<String, Double> getSalarySumByGender() {
		String sql = "select gender,sum(salary) as sumSalary_gender from employee_payroll group by gender";
		return getAggregateByGender("gender", "sumSalary_gender", sql);
	}

	private List<PayRollData5> getEmployeePayrollDataUsingDB(String sql) throws PayRollExceptions5 {
		List<PayRollData5> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new PayRollData5(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new PayRollExceptions5(e.getMessage(), PayRollExceptions5.ExceptionType.RETRIEVAL_PROBLEM);
		}
		return employeePayrollList;

	}

	public List<PayRollData5> getEmployeePayrollData(String name) {
		List<PayRollData5> employeePayrollList = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<PayRollData5> getEmployeePayrollData(ResultSet result) {
		List<PayRollData5> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new PayRollData5(id, name, salary, startDate));
				return employeePayrollList;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<PayRollData5> readData() throws PayRollExceptions5 {
		String sql = "select * from employee_payroll";
		List<PayRollData5> employeePayrollList = new ArrayList<>();
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new PayRollData5(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new PayRollExceptions5(e.getMessage(), PayRollExceptions5.ExceptionType.RETRIEVAL_PROBLEM);

		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) throws PayRollExceptions5 {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws PayRollExceptions5 {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new PayRollExceptions5(e.getMessage(), PayRollExceptions5.ExceptionType.UPDATE_PROBLEM);
		}
	}

	public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
		try (Connection connection = this.getConnection();) {
			String sql = "update employee_payroll set salary=? where name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int status = preparedStatement.executeUpdate();
			return status;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
