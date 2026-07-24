package rest.omms;

import com.google.api.client.http.javanet.ConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DataSourceConfig implements ServletContextListener, ConnectionFactory {
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
        //config.setJdbcUrl("jdbc:postgresql://localhost:5432/joblist");//172.17.100.6
        /**config.setJdbcUrl("jdbc:postgresql://172.17.100.6:5432/joblist");
        config.setUsername("postgres");
        config.setPassword("03_0431A");/**/
        String url=System.getenv("DB_URL");
        url=String.format("jdbc:postgresql:///%s", "joblist");
        String username = System.getenv("DB_USER");
   	 	String password = System.getenv("DB_PASSWORD");
   	 	String INSTANCE_CONNECTION_NAME = System.getenv("	");
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("DATABASE_URL is not set");
        }
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);/**/
        config.setMaximumPoolSize(10); // Set the maximum pool size
        config.setMinimumIdle(5); // Set the minimum idle connections
        config.setIdleTimeout(10000);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(30000);
        
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", INSTANCE_CONNECTION_NAME);


        // The ipTypes argument can be used to specify a comma delimited list of preferred IP types
        // for connecting to a Cloud SQL instance. The argument ipTypes=PRIVATE will force the
        // SocketFactory to connect with an instance's associated private IP.
        config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");

        // cloudSqlRefreshStrategy set to "lazy" is used to perform a
        // refresh when needed, rather than on a scheduled interval.
        // This is recommended for serverless environments to
        // avoid background refreshes from throttling CPU.
        config.addDataSourceProperty("cloudSqlRefreshStrategy", "lazy");

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

	@Override
	public HttpURLConnection openConnection(URL arg0) throws IOException, ClassCastException {
		// TODO Auto-generated method stub
		return null;
	}
}

