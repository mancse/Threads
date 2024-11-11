package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

interface ConnectionPool
{
	Connection getConnection();
	boolean releaseConnection(Connection connection);
}
public class DBConnectionPool implements ConnectionPool
{
    private String url=null;
    private String user=null;
    private String password=null;
    private static int INITIAL_POOL_SIZE =5;
    private static int MAX_POOL_SIZE = 10;
    List<Connection> availableConnections = null;
    List<Connection> usedConnections = new ArrayList<>();
    int MAX_TIMEOUT=10;
    
	@Override
	public synchronized Connection getConnection() 
	{
		Connection connection = null;
		try
		{
			if (availableConnections.isEmpty())
			{
				if (usedConnections.size() < MAX_POOL_SIZE)
				{
                    availableConnections.add(createConnection(url,user,password));
				}
				else
				{
					throw new RuntimeException("Maximum pool size reached. No available connections");
				}
			}
			connection = availableConnections.get(availableConnections.size() - 1);
			
			if (!connection.isValid(MAX_TIMEOUT));
			{
				connection = createConnection(url,user,password);
			}
			usedConnections.add(connection);
		}
		catch (Exception e)
		{
			
		}
		return connection;
	}

	@Override
	public synchronized boolean releaseConnection(Connection connection) 
	{
		availableConnections.add(connection);
		return usedConnections.remove(connection);
	}
    public DBConnectionPool(String url, String user, String password, List<Connection> pool)
    {
    	this.url = url;
    	this.user = user;
    	this.password = password;
    	availableConnections = pool;
    }
    private static Connection createConnection(String url, String user, String password)throws SQLException
    {
    	return DriverManager.getConnection(url,user,password);
    }
	public static DBConnectionPool createPool(String url, String user, String password) throws SQLException
	{
		List<Connection> pool = new ArrayList<>();
		for (int i=0 ; i < INITIAL_POOL_SIZE; i++)
		{
			pool.add(createConnection(url,user,password));
		}
		return new DBConnectionPool(url,user,password,pool);
	}
}
