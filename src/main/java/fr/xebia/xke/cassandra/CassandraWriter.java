package fr.xebia.xke.cassandra;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class CassandraWriter {

    private final Session session;

    public CassandraWriter(Session session) {
        this.session = session;
    }

    public void writeUserWithBoundStatement(UUID id, String name, String email, Integer age) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO user(uuid,name,email, age) VALUES (?,?,?,?)");
        BoundStatement boundStatement = preparedStatement.bind(id, name, email, age);
        boundStatement.setConsistencyLevel(ConsistencyLevel.ANY);

        session.execute(boundStatement);
    }

    public ResultSet readUsersWithQueryBuilder() {
        return session.execute(QueryBuilder.select().all().from("user"));
    }

    public void batchWriteUsers(List<String> insertQueries) {
        Batch batch = QueryBuilder.batch();
        for (String insertQuery : insertQueries) {
            batch.add(new SimpleStatement(insertQuery));
        }
        batch.setConsistencyLevel(ConsistencyLevel.ALL)
        .enableTracing();

        session.execute(batch);
    }

    public void writeTrackWithQueryBuilder(UUID id, String title, Date release,
                                           Float duration, Set<String> tags) {
        Query insert = QueryBuilder.insertInto("tracks") //
                .value("id", id) //
                .value("title", title) //
                .value("release", release) //
                .value("duration", duration)//
                .value("tags", tags);

        session.execute(insert);
    }

    public void writeToClickStreamWithTTL(UUID userId, Date when, String url, String referer) {
        session.execute(QueryBuilder.insertInto("user_click_stream")
                .value("user_id", userId)
                .value("when", when)
                .value("url", url)
                .value("referer", referer)
                .using(QueryBuilder.ttl(3600)));
    }

    public ResultSet readClickStreamByTimeframe(UUID userId, Date start, Date end) {
        return session.execute(QueryBuilder.select().from("user_click_stream")
                .where(QueryBuilder.eq("user_id", userId))
                .and(QueryBuilder.gt("when", start))
                .and(QueryBuilder.lt("when", end)));
    }

    public ResultSetFuture writeAndReadLikesAsynchronously(UUID id, List<UUID> tracks) {
        for (UUID track : tracks) {
            session.executeAsync(
                QueryBuilder.insertInto("track_likes")
                    .value("id", id)
                    .value("track_id", track)
            );
        }

        return session.executeAsync(
                QueryBuilder.select()
                        .all()
                        .from("track_likes")
                        .limit(1000)
        );
    }
}
