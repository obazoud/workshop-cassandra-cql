package fr.xebia.xke.cassandra;

import java.util.List;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import org.junit.After;
import org.junit.BeforeClass;

public class AbstractTest {

    private static Cluster cluster;
    private static Session session;

    private static final String KEYSPACE = "workshop";
    private static final String HOST = "localhost";
    private static final int PORT = 9160;


    @BeforeClass
    public static void init() {
        cluster = com.datastax.driver.core.Cluster.builder() //
                    .addContactPoints(HOST)//
                    .build();
        session = cluster.connect(KEYSPACE);
    }

    @After
    public void cleanUp()
    {
        String listAllTables = "select columnfamily_name from system.schema_columnfamilies where keyspace_name='"+ KEYSPACE +"'";
        List<Row> rows = session.execute(listAllTables).all();

        for (Row row : rows)
        {
            session.execute(new SimpleStatement("truncate " + row.getString("columnfamily_name")));
        }
    }

    public static Session session() {
        return session;
    }
}
