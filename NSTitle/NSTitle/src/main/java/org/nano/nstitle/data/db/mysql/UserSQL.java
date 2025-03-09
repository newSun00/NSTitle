package org.nano.nstitle.data.db.mysql;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.user.UserRegistry;
import org.nano.nstitle.data.registy.user.dto.TitleInventory;
import org.nano.nstitle.manager.DatabaseManager;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class UserSQL {
    private final Logger LOGGER = Logger.getLogger(UserSQL.class.getName());
    private final UserRegistry userRegistry = RegistryCore.getInstance().getUserRegistry();
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final Gson gson = new Gson();

    public UserSQL() {
        db.connect();
        createTable();
    }

    private void createTable() {
        if (!db.isEnabled()) return;
        String sql = "CREATE TABLE IF NOT EXISTS user_titles (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "equip_titles JSON NOT NULL, " +
                "have_titles JSON NOT NULL)";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            LOGGER.info("Table 'user_titles' has been created or already exists.");
        } catch (SQLException e) {
            LOGGER.severe("Error creating database table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(UUID uuid) {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            TitleInventory ti = userRegistry.gettInv(uuid);
            String equipTitlesJson = gson.toJson(ti.getEquipTitle());
            String haveTitlesJson = gson.toJson(ti.getHaveTitles());

            String sql = "REPLACE INTO user_titles (uuid, equip_titles, have_titles) VALUES (?, ?, ?)";
            try (Connection conn = db.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, uuid.toString());
                pstmt.setString(2, equipTitlesJson);
                pstmt.setString(3, haveTitlesJson);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.severe("Error saving user titles: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void load(UUID uuid) {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            String sql = "SELECT equip_titles, have_titles FROM user_titles WHERE uuid = ?";
            try (Connection conn = db.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, uuid.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Type mapType = new TypeToken<Map<Integer, String>>() {}.getType();
                        Type listType = new TypeToken<List<String>>() {}.getType();

                        Map<Integer, String> equipTitles = gson.fromJson(rs.getString("equip_titles"), mapType);
                        List<String> haveTitles = gson.fromJson(rs.getString("have_titles"), listType);

                        TitleInventory ti = userRegistry.gettInv(uuid);
                        ti.setEquipTitle(equipTitles);
                        ti.setHaveTitles(haveTitles);
                    }
                }
            } catch (SQLException e) {
                LOGGER.severe("Error loading user titles: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void remove(String equip) {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            // 1. 장착 칭호에서 해당 칭호 제거
            String removeEquipSql = "UPDATE user_titles SET equip_titles = JSON_REMOVE(equip_titles, " +
                    "(SELECT JSON_SEARCH(equip_titles, 'one', ?))) WHERE JSON_SEARCH(equip_titles, 'one', ?) IS NOT NULL";

            // 2. 보유 칭호에서 해당 칭호 제거
            String removeHaveSql = "UPDATE user_titles SET have_titles = JSON_REMOVE(have_titles, " +
                    "(SELECT JSON_SEARCH(have_titles, 'one', ?))) WHERE JSON_SEARCH(have_titles, 'one', ?) IS NOT NULL";

            try (Connection conn = db.getConnection()) {
                try (PreparedStatement equipStmt = conn.prepareStatement(removeEquipSql);
                     PreparedStatement haveStmt = conn.prepareStatement(removeHaveSql)) {

                    equipStmt.setString(1, equip);
                    equipStmt.setString(2, equip);
                    haveStmt.setString(1, equip);
                    haveStmt.setString(2, equip);

                    equipStmt.executeUpdate();
                    haveStmt.executeUpdate();
                }
            } catch (SQLException e) {
                LOGGER.severe("Error removing title from all users: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
