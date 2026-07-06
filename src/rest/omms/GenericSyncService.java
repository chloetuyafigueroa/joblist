package rest.omms;

import java.sql.*;
import java.util.*;

public class GenericSyncService {

    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASS = System.getenv("DB_PASSWORD");

    private static final String REMOTE_DB_URL = System.getenv("REMOTE_DATABASE_URL");
    private static final String REMOTE_DB_USER = System.getenv("REMOTE_DB_USER");
    private static final String REMOTE_DB_PASS = System.getenv("REMOTE_DB_PASSWORD");

    public static void syncAll() {
        try (
            Connection localConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Connection supabaseConn = DriverManager.getConnection(REMOTE_DB_URL, REMOTE_DB_USER, REMOTE_DB_PASS)
        ) {
            localConn.setAutoCommit(false);
            supabaseConn.setAutoCommit(false);

            syncDirection(supabaseConn, localConn, "supabase");
            syncDirection(localConn, supabaseConn, "local");

            supabaseConn.commit();
            localConn.commit();

            System.out.println("Generic sync completed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void syncDirection(
            Connection sourceConn,
            Connection targetConn,
            String sourceName
    ) throws SQLException {

        List<SyncChange> changes = getPendingChanges(sourceConn);
 /**
        for (SyncChange change : changes) {
            try {
                setSyncApply(targetConn, true);

                if ("DELETE".equalsIgnoreCase(change.operation())) {
                    applyDelete(targetConn, change);
                } else {
                    applyUpsert(targetConn, change);
                }

                setSyncApply(targetConn, false);

                markDone(sourceConn, change.id());

            } catch (Exception e) {
                setSyncApply(targetConn, false);
                markError(sourceConn, change.id(), e.getMessage());
            }
        }/**/
    }

    private static List<SyncChange> getPendingChanges(Connection conn)
            throws SQLException {

        long start = System.currentTimeMillis();

        String sql = """
            SELECT id,
                   table_name,
                   operation,
                   pk_data::text,
                   row_data::text
            FROM sync_monitor
            WHERE status = 'pending'
            ORDER BY created_at
            LIMIT 100
            """;

        List<SyncChange> list = new ArrayList<>();

        System.out.println("==================================================");
        System.out.println("Checking sync_monitor for pending changes...");
        System.out.println("Database : " + conn.getMetaData().getURL());
        System.out.println("Time     : " + new java.util.Date());
        System.out.println("==================================================");

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                SyncChange change = new SyncChange(
                        rs.getLong("id"),
                        rs.getString("table_name"),
                        rs.getString("operation"),
                        rs.getString("pk_data"),
                        rs.getString("row_data")
                );

                list.add(change);

                System.out.printf(
                        "[PENDING] ID=%d | TABLE=%s | OP=%s | PK=%s%n",
                        change.id(),
                        change.tableName(),
                        change.operation(),
                        change.pkDataJson()
                );
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("-----------------------------------------------");
        System.out.println("Pending Changes : " + list.size());
        System.out.println("Elapsed Time    : " + (end - start) + " ms");
        System.out.println("-----------------------------------------------");

        return list;
    }

    @SuppressWarnings("unused")
	private static void applyUpsert(Connection conn, SyncChange change)
            throws SQLException {

        String sql = """
            SELECT apply_sync_upsert(
                ?::text,
                ?::jsonb,
                ?::jsonb
            )
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, change.tableName());
            ps.setString(2, change.pkDataJson());
            ps.setString(3, change.rowDataJson());
            ps.execute();
        }
    }

    private static void applyDelete(Connection conn, SyncChange change)
            throws SQLException {

        String sql = """
            SELECT apply_sync_delete(
                ?::text,
                ?::jsonb
            )
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, change.tableName());
            ps.setString(2, change.pkDataJson());
            ps.execute();
        }
    }

    @SuppressWarnings("unused")
	private static void markDone(Connection conn, long id)
            throws SQLException {

        String sql = """
            UPDATE sync_monitor
            SET status = 'done',
                processed_at = now()
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private static void markError(Connection conn, long id, String error)
            throws SQLException {

        String sql = """
            UPDATE sync_monitor
            SET status = 'error',
                error_message = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, error);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private static void setSyncApply(Connection conn, boolean value)
            throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT set_config('app.sync_apply', ?, true)"
        )) {
            ps.setString(1, value ? "true" : "false");
            ps.execute();
        }
    }

    private record SyncChange(
        long id,
        String tableName,
        String operation,
        String pkDataJson,
        String rowDataJson
    ) {}
}