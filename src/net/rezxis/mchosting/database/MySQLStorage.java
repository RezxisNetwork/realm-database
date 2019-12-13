package net.rezxis.mchosting.database;

import java.sql.*;
import java.util.*;

public abstract class MySQLStorage {
    protected String prefix = "";
    protected String tablename = "";
    private String configName;

    public MySQLStorage(String configName)
    {
        this.configName = configName;
    }

    protected void log(String message) {
        System.out.println("MySQL :" + message);
    }

    protected String foreignKey(String columnName,String targetTable,String targetColumn)
    {
        return "FOREIGN KEY("+columnName+") REFERENCES "+targetTable+"("+targetColumn+") ON DELETE CASCADE";
    }

    protected String foreignKeyDeleteSetNull(String columnName,String targetTable,String targetColumn)
    {
        return "FOREIGN KEY("+columnName+") REFERENCES "+targetTable+"("+targetColumn+") ON DELETE SET NULL";
    }

    protected boolean createTable(LinkedHashMap<String, String> ColumnsAndDatatypes) {
        return createTable(ColumnsAndDatatypes,(String[])null);
    }

    protected boolean createTable(final LinkedHashMap<String, String> ColumnsAndDatatypes, final String... definitions) {
        executeQuery(new Query(selectFromTable("*") + " LIMIT 1") {
            @Override
            protected void onResult(ResultSet paramResultSet) {
                try {
                    if (paramResultSet.next()){
                        log(getTable() + " exists already, proceeding.");
                    }
                } catch (SQLException e) {

                }
            }

            @Override
            public void onFail(Exception x) {
                try {
                    final Connection connection = getConnection();
                    StringBuilder spec = new StringBuilder();
                    for (String column : ColumnsAndDatatypes.keySet()) {
                        String type = ColumnsAndDatatypes.get(column);
                        spec.append("`" + column + "` " + type);
                        if (!type.endsWith(","))
                            spec.append(",");
                    }
                    spec.deleteCharAt(spec.length() - 1);
                    PreparedStatement pstatement;
                    if (definitions == null || definitions.length == 0)
                        pstatement = connection.prepareStatement("CREATE TABLE " + prefix + tablename + " ( " + spec.toString() + " )");
                    else {
                        StringBuilder sb = new StringBuilder();
                        for (String string : definitions) {
                            sb.append(",").append(string);
                        }
                        pstatement = connection.prepareStatement("CREATE TABLE " + prefix + tablename + " ( " + spec.toString() + sb.toString() + " )");
                    }
                    System.out.println(pstatement.toString());
                    pstatement.execute();
                    pstatement.close();
                    connection.close();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
            return true;
    }

    protected Connection getConnection() {
        try {
            return MySQLProvider.getConnection(configName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object runQuery(QueryRunner queryRunnable)
    {
        queryRunnable.prepareQuery();
        queryRunnable.run();
        queryRunnable.close();
        return queryRunnable.getReturnValue();
    }

    protected void execute(String query,Object... params)
    {
        execute(new QueryExecutor(query,params));
    }

    protected Object executeQuery(QueryExecutor query)
    {
        query.prepareQuery();
        query.execute();
        query.close();
        return query.getReturnValue();
    }


    protected void execute(QueryExecutor queryRunnable)
    {
        queryRunnable.prepareQuery();
        queryRunnable.execute();
        queryRunnable.close();
    }

    protected int countRecords()
    {
        return count(null,null);
    }

    protected int count(String columns,String where) {
        if(columns == null)
            columns = "*";
        if(where == null)
            where = "1 = 1";
        return (int) runQuery(new QueryRunner("SELECT " + columns + " FROM " + getTable() + " WHERE " + where) {
            @Override
            public void run() {
                final ResultSet result = executeQuery();
                try {
                    if (result.next()) {
                        setReturnValue(result.getInt(1));
                    }else{
                        setReturnValue(0);
                    }
                } catch (SQLException e) {
                    setReturnValue(0);
                    e.printStackTrace();
                }
            }
        });
    }

    protected void delete(String where,Object... params)
    {
        execute("DELETE " + fromTable() + " WHERE " + where,params);
    }

    protected String insertIntoTable()
    {
        return "INSERT INTO " + getTable();
    }

    protected String selectFromTable(String what)
    {
        if(what == null)
            what = "*";
        return "SELECT " + what + fromTable();
    }

    protected String selectFromTable(String what, String where)
    {
        if(what == null)
            what = "*";
        return "SELECT " + what + fromTable() + " WHERE " + where;
    }

    protected String fromTable()
    {
        return " FROM " + getTable() + " ";
    }

    protected String getTable()
    {
        return prefix+tablename;
    }

    public Timestamp getTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    protected class BatchExecutor extends MySQLStorage.QueryExecutor
    {
        private int numArgsPerEntry;

        public BatchExecutor(String query, int numArgsPerEntry, Object[] params)
        {
            super(query, params);
            this.numArgsPerEntry = numArgsPerEntry;
            if (params.length % numArgsPerEntry != 0)
            {
                System.out.println("params must be evenly divisible by numArgsPerEntry, groups of "+numArgsPerEntry+" were expected, but "+ params.length +" is not evenly divisible, batch creation failed.");
                throw new IllegalArgumentException(new StringBuilder().append("params must be evenly divisible by numArgsPerEntry, groups of ").append(numArgsPerEntry).append(" were expected, but ").append(params.length).append(" is not evenly divisible, batch creation failed.").toString());
            }
        }

        public void prepareQuery()
        {
            this.connection = MySQLStorage.this.getConnection();
            try {
                this.pStatement = this.connection.prepareStatement(this.query);
                Iterator<?> iterator;
                int i = 1;
                StringBuilder sb = new StringBuilder();
                for (iterator = this.params.iterator(); iterator.hasNext(); ) {
                    Object param = iterator.next();
                    this.pStatement.setObject(i, param);
                    i++;
                    if (i > this.numArgsPerEntry)
                    {
                        sb.delete(0, sb.length());
                        this.pStatement.addBatch();
                        this.pStatement.clearParameters();
                        i = 1;
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        public void execute()
        {
            try {
                this.pStatement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    protected abstract class QueryRunner extends MySQLStorage.QueryExecutor
            implements Runnable
    {
        public QueryRunner(String query)
        {
            super(query);
        }

        public QueryRunner(String query, Object... params) {
            super(query, params);
        }
    }

    protected abstract class Query extends MySQLStorage.QueryExecutor
    {
        public Query(String query)
        {
            super(query);
        }

        public Query(String query, Object... params) {
            super(query, params);
        }

        public void execute()
        {
            try {
                ResultSet results = this.pStatement.executeQuery();
                onResult(results);
            } catch (SQLException e) {
                onFail(e);
            }
        }

        public void onFail(Exception x) {

        }

        protected abstract void onResult(ResultSet paramResultSet);
    }

    protected abstract class Insert extends MySQLStorage.QueryExecutor
    {
        public Insert(String query)
        {
            super(query);
        }

        public Insert(String query, Object... params) {
            super(query, params);
        }

        public void prepareQuery()
        {
            this.connection = MySQLStorage.this.getConnection();
            try {
                this.pStatement = this.connection.prepareStatement(this.query, 1);
                Iterator<?> iterator;
                int i = 1;
                for (iterator = this.params.iterator(); iterator.hasNext(); ) {
                    Object param = iterator.next();
                    this.pStatement.setObject(i, param);
                    i++;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        public void execute()
        {
            try {
                this.pStatement.executeUpdate();
                onInsert(getGeneratedKeys());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public abstract void onInsert(List<Integer> keys);

        private List<Integer> getGeneratedKeys()
        {
            List<Integer> keys = new ArrayList<Integer>();
            try {
                ResultSet rs = this.pStatement.getGeneratedKeys();
                while (rs.next())
                    keys.add(Integer.valueOf(rs.getInt(1)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return keys;
        }
    }

    protected class QueryExecutor {

        protected final List<Object> params = new LinkedList<>();
        protected Connection connection;
        protected PreparedStatement pStatement;
        ResultSet resultSet;
        String query;
        private Object returnValue = null;

        public QueryExecutor(String query) {
            this.query = query;
        }

        public QueryExecutor(String query, Object... params) {
            this.query = query;
            for (Object param : params) {
                this.params.add(param);
            }
        }


        public void prepareQuery()
        {
            connection = getConnection();
            try {
                pStatement = connection.prepareStatement(query);
                int i = 1;
                for (Object param : params) {
                    //Assume is null string, otherwise you are SOL
                    if(param == null)
                        pStatement.setString(i,null);
                    else pStatement.setObject(i,param);
                    i++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ResultSet executeQuery()
        {
            try {
                resultSet = pStatement.executeQuery();
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public int executeUpdate()
        {
            try {
                return pStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }


        public void execute()
        {
            try {
                pStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void close(){
            try {
                if(resultSet != null)
                    resultSet.close();
                pStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public Object getReturnValue() {
            return returnValue;
        }

        public void setReturnValue(Object returnValue) {
            this.returnValue = returnValue;
        }
    }
}
