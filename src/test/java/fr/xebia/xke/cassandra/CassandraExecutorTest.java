package fr.xebia.xke.cassandra;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import fr.xebia.xke.cassandra.model.User;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;

public class CassandraExecutorTest extends AbstractTest {

    private CassandraExecutor writer;

    @Before
    public void setUp() throws Exception {
        writer = new CassandraExecutor(session());
    }

    @Test
    public void should_write_user_with_statement() throws Exception {
        User user = loadUser(1);
        writeUser(user);
    }

    private User loadUser(Integer i) throws URISyntaxException {
        URL resourceAsStream = getClass().getClassLoader().getResource("data/user"+i+".json");
        User user = JacksonReader.readJsonFile(User.class, new File(resourceAsStream.toURI()));
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        return user;
    }

    private void writeUser(User user) {
        writer.writeUserWithBoundStatement(user.getId(), user.getName(),
                user.getEmail(), RandomUtils.nextInt(100));
    }

    @Test
    public void should_read_user() throws Exception {
        User user = loadUser(1);
        writeUser(user);
        ResultSet rows = writer.readUserWithQueryBuilder(user.getId());
        assertEquals(user.getId(), rows.one().getUUID("id"));
    }

    @Test
    public void should_write_tracks() {

        for(int i = 0; i < 10; i++) {
            UUID value = UUID.randomUUID();
            String title = "title"+RandomUtils.nextLong();
            Clause filterById = QueryBuilder.eq("id", value);
            String select = QueryBuilder.select("title", "release", "duration", "tags")
                    .from("tracks").where(filterById)
                    .toString();
            Row row = session().execute(select).all().get(0);
            assertThat(row.getString("title")).isEqualTo(title);
        }
    }

    @Test
    public void should_write_likes_asynchronously() {

    }

    @Test
    public void should_batch_write_likes() throws Exception {
        List<UUID> writtenIds = new ArrayList<UUID>();
        List<String> queries = new ArrayList<String>();
        for(int i = 0; i < 20; i++) {
            User user = loadUser((i % 4)+1);
            writtenIds.add(user.getId());
            queries.add(QueryBuilder.insertInto("user") //
                    .value("id", user.getId()) //
                    .value("name", user.getName()) //
                    .value("email", user.getEmail()) //
                    .value("age", RandomUtils.nextInt(100))//
                    .toString());
        }
        writer.batchWriteUsers(queries);
    }
}
