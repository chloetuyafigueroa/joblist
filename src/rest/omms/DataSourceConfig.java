package rest.omms;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DataSourceConfig implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DriverManager.deregisterDriver(DriverManager.getDriver("jdbc:postgresql://"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  
    public static HikariDataSource dataSource;
    
    static {
        initializeDataSource();
    }
    private static void initializeDataSource()  {
        HikariConfig config = new HikariConfig();
        try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error:"+e);
		}
        config.setJdbcUrl("jdbc:postgresql://172.17.100.6:5432/joblist");//172.17.100.6
        //config.setJdbcUrl("jdbc:postgresql://172.17.100.6:5432/joblist");
        config.setUsername("postgres");
        config.setPassword("03_0431A");
        config.setMaximumPoolSize(30); // Set the maximum pool size
        config.setMinimumIdle(5); // Set the minimum idle connections
        config.setIdleTimeout(10000);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(30000);

        dataSource = new HikariDataSource(config);
    }
    
    public static synchronized void resetDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
        initializeDataSource();
    }
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }
    /**public static void monitorAndResetIfNeeded() {
        HikariPoolMXBean poolMXBean = (HikariPoolMXBean) dataSource.getHikariPoolMXBean();
        if (poolMXBean.getActiveConnections() >= dataSource.getMaximumPoolSize()) {
            System.out.println("Maximum pool size reached. Resetting the pool.");
            resetDataSource();
        }
    }/**/

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

