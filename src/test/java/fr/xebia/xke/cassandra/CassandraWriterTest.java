package fr.xebia.xke.cassandra;


import static org.fest.assertions.Assertions.assertThat;
import java.util.UUID;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;

public class CassandraWriterTest extends AbstractTest {

    private CassandraWriter dao;

    @Before
    public void setUp() throws Exception {
        dao = new CassandraWriter(session());
    }

    @Test
    public void should_write_user() {

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
    public void should_batch_write_likes() {

    }

    @Test
    public void should_write_likes_asynchronously() {

    }
}
