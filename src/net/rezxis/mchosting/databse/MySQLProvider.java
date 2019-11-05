package net.rezxis.mchosting.databse;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class MySQLProvider
{
  private static MySQLProvider instance;
  private HashMap<String, BoneCP> connectionPools = new HashMap<>();

  public static void init()
  {
    instance = new MySQLProvider();
  }

  public void addPool(String configName)
    throws Exception
  {
    String hostname = Database.getProps().DB_HOST;
    String port = Database.getProps().DB_PORT;
    String dbname = Database.getProps().DB_NAME;
    String username = Database.getProps().DB_USER;
    String password = Database.getProps().DB_PASS;
    Properties properties = new Properties();
    properties.setProperty("characterEncoding", "UTF-8");
    properties.setProperty("useUnicode", "true");
    BoneCPConfig boneCPConfig = new BoneCPConfig(properties);
    boneCPConfig.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false");
    boneCPConfig.setUsername(username);
    boneCPConfig.setPassword(password);
    boneCPConfig.setDefaultAutoCommit(true);
    boneCPConfig.setMinConnectionsPerPartition(5);
    boneCPConfig.setMaxConnectionsPerPartition(40);
    boneCPConfig.setPartitionCount(1);
    try
    {
      instance.connectionPools.put(configName, new BoneCP(boneCPConfig));
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  public static Connection getConnection(String configName)
    throws Exception
  {
    if (!instance.connectionPools.containsKey(configName)) {
      instance.addPool(configName);
    }
    return ((BoneCP)instance.connectionPools.get(configName)).getConnection();
  }

  public static void closeAllConnections()
  {
    for (BoneCP pool : instance.connectionPools.values()) {
      pool.shutdown();
    }
  }
}
