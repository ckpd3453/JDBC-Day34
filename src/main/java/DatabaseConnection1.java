import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.List;


/**
 * 1.creating connection with dataBase payroll Service
 */
public class DatabaseConnection1 {

	private Connection getConnection() throws SQLException {
		String connectionUrl = "jdbc:mysql://localhost:3306/payroll_services";
		String userName = "root";
		String password = "Revenge@3453";
		Connection con;
		System.out.println("connecting tothe database:" + connectionUrl);
		con = DriverManager.getConnection(connectionUrl, userName, password);
		System.out.println("connection is successful!!!!" + con);
		return con;
	}
	
	public List<PayRollData> readData() throws PayRollExceptions {
		String sql = "select * from employee_payroll";
		List<PayRollData> employeePayrollList = new ArrayList<>();
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while(result.next()) {
					int id = result.getInt("id");
					String name = result.getString("name");
					Double salary = result.getDouble("salary");
					LocalDate startDate = result.getDate("start").toLocalDate();
					employeePayrollList.add(new PayRollData(id,name,salary,startDate));
				}
			} catch (SQLException e) {
				throw new PayRollExceptions(e.getMessage(),PayRollExceptions.ExceptionType.RETRIEVAL_PROBLEM);
			}
		return employeePayrollList;
	}
}
