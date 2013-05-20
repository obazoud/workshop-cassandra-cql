package fr.xebia.xke.cassandra;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class CassandraExecutor {

    private final Session session;

    public CassandraExecutor(Session session) {
        this.session = session;
    }

    public void writeUserWithBoundStatement(UUID id, String name, String email, Integer age) {
    }

    public ResultSet readUserWithQueryBuilder(UUID id) {
        return null;
    }

    public void writeTrackWithQueryBuilder(UUID id, String title, Date release,
                                           Float duration, Set<String> tags) {
    }

    public void writeToClickStreamWithTTL(UUID userId, Date when, String url) {
    }

    public ResultSet readClickStreamByTimeframe(UUID userId, Date start, Date end) {
        return null;
    }

    public ResultSetFuture writeAndReadLikesAsynchronously(UUID id, List<UUID> tracks) {
        return null;
    }

    public void batchWriteUsers(List<String> insertQueries) {
    }
}
