package fr.xebia.xke.cassandra;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class CassandraWriter {

    private final Session session;

    public CassandraWriter(Session session) {
        this.session = session;
    }

    public void writeUserWithBoundStatement(UUID id, String name, String email, String password, Integer age) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO user(uuid,name,email,password, age) VALUES (?,?,?,?)");
        BoundStatement boundStatement = preparedStatement.bind(id, name, email, password, age);
        boundStatement.setConsistencyLevel(ConsistencyLevel.ANY);

        session.execute(boundStatement);
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

    public void writeUsersWithBatches() {
        String insert1 = QueryBuilder.insertInto("cql3_user") //
                .value("id", 100001L) //
                .value("firstname", "FN1") //
                .value("lastname", "LN1") //
                .value("age", 31) //
                .toString();

        String insert2 = QueryBuilder.insertInto("cql3_user") //
                .value("id", 100002L) //
                .value("firstname", "FN2") //
                .value("lastname", "LN2") //
                .value("age", 32)//
                .toString();

        String insert3 = QueryBuilder.insertInto("cql3_user") //
                .value("id", 100003L) //
                .value("firstname", "FN3") //
                .value("lastname", "LN3") //
                .value("age", 33)//
                .toString();

        Query batch = QueryBuilder //
                .batch(new SimpleStatement(insert1)) //
                .add(new SimpleStatement(insert2))//
                .add(new SimpleStatement(insert3))//
                .setConsistencyLevel(ConsistencyLevel.ALL)//
                .enableTracing();

        session.execute(batch);
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
