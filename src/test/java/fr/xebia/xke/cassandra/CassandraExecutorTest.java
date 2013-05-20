package fr.xebia.xke.cassandra;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import fr.xebia.xke.cassandra.model.Track;
import fr.xebia.xke.cassandra.model.User;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CassandraExecutorTest extends AbstractTest {

    Logger logger = Logger.getLogger(CassandraExecutorTest.class);

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

    @Test
    public void should_read_user() throws Exception {
        User user = loadUser(1);
        writeUser(user);
        ResultSet rows = writer.readUserWithQueryBuilder(user.getId());
        assertEquals(user.getId(), rows.one().getUUID("id"));
    }

    @Test
    public void should_write_track() throws Exception {

        Set<Track> tracks = loadTracks();
        for (Track track : tracks) {
            writer.writeTrackWithQueryBuilder(track.getId(), track.getName(),
                    track.getRelease(), track.getDuration(), track.getTags());
            Clause filterById = QueryBuilder.eq("id", track.getId());
            String select = QueryBuilder.select("title", "release", "duration", "tags")
                    .from("tracks").where(filterById)
                    .toString();
            Row row = session().execute(select).all().get(0);
            assertThat(row.getString("title")).isEqualTo(track.getName());
        }
    }



    @Test
    public void should_write_likes_asynchronously() throws Exception {
        ResultSet usersQuery = session().execute(QueryBuilder.select().column("id").from("user").limit(100));
        ResultSet tracksQuery = session().execute(QueryBuilder.select().column("id").from("tracks").limit(100));
        List<Row> all = tracksQuery.all();
        List<UUID> trackIds = new ArrayList<UUID>();
        for (Row row : all) {
            trackIds.add(row.getUUID("id"));
        }
        for (Row row : usersQuery) {
            ResultSetFuture id = writer.writeAndReadLikesAsynchronously(row.getUUID("id"), trackIds);
            ResultSet rows = id.get();
            logger.info(rows.all().size());
        }

    }

    @Ignore
    @Test
    public void should_batch_write_users() throws Exception {
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


    ///////// UTILITY METHODS ///////////

    private Set<Track> loadTracks() throws Exception {
        Set<Track> tracks = new HashSet<Track>();
        URL resourceAsStream = getClass().getClassLoader().getResource("data/tracks");
        File directory = new File(resourceAsStream.toURI());
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        for (File file : files) {
            Track e = JacksonReader.readJsonFile(Track.class, file);
            e.setId(UUIDs.timeBased());
            e.setRelease(new Date(System.nanoTime()));
            tracks.add(e);
        }
        return tracks;
    }

    private User loadUser(Integer i) throws Exception {
        URL resourceAsStream = getClass().getClassLoader().getResource("data/user"+i+".json");
        User user = JacksonReader.readJsonFile(User.class, new File(resourceAsStream.toURI()));
        UUID uuid = UUIDs.random();
        user.setId(uuid);
        return user;
    }

    private void writeUser(User user) {
        writer.writeUserWithBoundStatement(user.getId(), user.getName(),
                user.getEmail(), RandomUtils.nextInt(100));
    }

}
