package com.instrumentation.writer;


import com.instrumentation.process.IntrumentationGenerator;
import com.instrumentation.valueobjects.WriteObject;
import org.hsqldb.persist.HsqlProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.hsqldb.Server;
/*
 * Manages HSQLDB related stuff like starting the embedded DB server and creating the table if
 * it doesnt exist and writing to it the records
 *
 * TODO the inserts are currently not batch inserts. This may (or may not be an issue). So
 * ideally would be good to see if this needs changing based on performance of the writes.
 *
 */
public class HsqldbWriter {
    private Server server = null;
    private static final Logger LOGGER = Logger.getLogger(HsqldbWriter.class.getName());
    private static HsqldbWriter hsqldbWriter = null;
    private Connection con = null;
    public static HsqldbWriter getInstance() throws Exception {
        if (hsqldbWriter == null) {
            hsqldbWriter = new HsqldbWriter();
        }
        return hsqldbWriter;
    }

    public void cleanUp() {
    	LOGGER.info("Shutting down writer");
    	server.shutdown();
    }

    private HsqldbWriter() throws Exception {
        HsqlProperties props = new HsqlProperties();
        props.setProperty("server.database.0", "file:embedded_db/db;shutdown=true;");
        props.setProperty("server.dbname.0", "instrument_cs");
        
        server = new org.hsqldb.Server();
        server.setTrace(true);
        try {
            server.setProperties(props);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to start HSQLDB server");
            return;
        }

        server.start();
        Connection con = getConnection();
        Statement statement = con.createStatement();  
        statement.executeQuery("CREATE TABLE IF NOT EXISTS instrumentation (event_id varchar(250) not null, event_duration int not null, " +
                "type varchar(250) null, alert varchar(10) null)");
        
    }

    public Connection getConnection() {
    	if (con == null) {
		    try {
		            Class.forName("org.hsqldb.jdbc.JDBCDriver");
		            con = DriverManager.getConnection(
		                    "jdbc:hsqldb:hsql://localhost/instrument_cs", "SA", "");
		    } catch (ClassNotFoundException e) {
		            e.printStackTrace();
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
    	}
        return con;
    }

    public boolean insertInto(WriteObject writeObject) throws SQLException{
//        try {
//            Connection con = getConnection();
//            Statement statement = con.createStatement();
//            String insertString = String.format("INSERT INTO instrumentation (event_id, event_duration, type, alert) values ('%s', '%s', '%s', '%s')", writeObject.getEvent(), writeObject.getEventDuration(), writeObject.getType(), writeObject.getAlert());
//            statement.executeQuery(insertString);
//
//           ;
//        } catch (SQLException e1) {
//            LOGGER.log(Level.SEVERE, "Unable to get connection from HSQLDB");
//            e1.printStackTrace();
//            return false;
//        }

        try (Connection con = getConnection(); Statement statement = con.createStatement();) {
            String insertString = String.format("INSERT INTO instrumentation (event_id, event_duration, type, alert) values ('%s', '%s', '%s', '%s')",
                    writeObject.getEvent(), writeObject.getEventDuration(), writeObject.getType(), writeObject.getAlert());
            statement.executeQuery(insertString);
        }
        return true;

    }

    /*
     * Not used in actual code currently but introduced for helping with validating the inserted records
     *
     */
    public List<WriteObject> getInstrumentationData() throws SQLException{
        List<WriteObject> writeObjects = new ArrayList<>();
        Connection con = getConnection();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM instrumentation");
        while (rs.next()) {
            WriteObject writeObject = new WriteObject();
            writeObject.setEvent(rs.getString("event_id"));
            writeObject.setEventDuration(rs.getInt("event_duration"));
            writeObject.setType(rs.getString("type"));
            writeObject.setAlert(Boolean.valueOf(rs.getString("alert")));
            writeObjects.add(writeObject);
        }
        return writeObjects;
    }


}
