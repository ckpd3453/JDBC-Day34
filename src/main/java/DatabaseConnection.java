import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * 1.creating connection with dataBase payroll Service
 */
public class DatabaseConnection {

	public static void main(String[] args) {
		String connectionUrl = "jdbc:mysql://localhost:3306/payroll_services";
		String userName = "root";
		String password = "Revenge@3453";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Loaded Driver");
		}catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath");
		}
		
		listDrivers();
		try {
			System.out.println("connecting to the database:" + connectionUrl);
			connection = DriverManager.getConnection(connectionUrl,userName,password);
			System.out.println("connection is successful..."+connection);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println(driverClass.getClass().getName());
		}
		
	}
}
