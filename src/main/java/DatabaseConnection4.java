import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.List;

/**
 * 1.creating connection with dataBase payroll Service
 */
public class DatabaseConnection4 {
	private PreparedStatement employeePayrollDataStatement;
	private static DatabaseConnection4 employeePayrollDBService;

	DatabaseConnection4() {

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

	public static DatabaseConnection4 getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new DatabaseConnection4();
		return employeePayrollDBService;
	}

	public List<PayRollData4> getEmployeePayrollData(String name) {
		List<PayRollData4> employeePayrollList = null;
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

	private List<PayRollData4> getEmployeePayrollData(ResultSet result) {
		List<PayRollData4> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new PayRollData4(id, name, salary, startDate));
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

	public List<PayRollData4> readData() throws PayRollExceptions4 {
		String sql = "select * from employee_payroll";
		List<PayRollData4> employeePayrollList = new ArrayList<>();
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new PayRollData4(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new PayRollExceptions4(e.getMessage(), PayRollExceptions4.ExceptionType.RETRIEVAL_PROBLEM);

		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) throws PayRollExceptions4 {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws PayRollExceptions4 {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new PayRollExceptions4(e.getMessage(), PayRollExceptions4.ExceptionType.UPDATE_PROBLEM);
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
