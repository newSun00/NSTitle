package org.nano.nstitle.data.db.mysql;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.manager.DatabaseManager;
import org.nano.nstitle.util.key.ColorKeys;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class DataSQL {
    private final Logger LOGGER = Logger.getLogger(DataSQL.class.getName());
    private final TitleRegistry registry = RegistryCore.getInstance().getTitleRegistry();
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final Gson gson = new Gson();

    public DataSQL(){
        createTable();
    }

    private void createTable() {
        if (!db.isEnabled()) return;
        String sql =
                "CREATE TABLE IF NOT EXISTS titles (" +
                        "unique_id VARCHAR(255) PRIMARY KEY, " +
                        "display TEXT NOT NULL, " +
                        "lore JSON NOT NULL, " +
                        "col_num INT NOT NULL, " +
                        "color_key VARCHAR(50) NOT NULL, " +
                        "color_display VARCHAR(255) NOT NULL, " +
                        "hex JSON NOT NULL)";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            LOGGER.info("Table 'titles' has been created or already exists.");
        } catch (SQLException e) {
            LOGGER.severe("Error creating database table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(Title title) {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            String sql = "REPLACE INTO titles (unique_id, display, lore, col_num, color_key, color_display, hex) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = db.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, title.getUnique());
                pstmt.setString(2, title.getDisplay());
                pstmt.setString(3, gson.toJson(title.getLore())); // JSON 저장
                pstmt.setInt(4, title.getColNum());
                pstmt.setString(5, title.getColor().getColorKeys().name());
                pstmt.setString(6, title.getColor().getDisplay());
                pstmt.setString(7, gson.toJson(title.getColor().getHex())); // JSON 저장

                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.severe("Error saving title: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void load() {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            String sql = "SELECT * FROM titles";
            try (Connection conn = db.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String unique = rs.getString("unique_id");
                    String display = rs.getString("display");

                    Type listType = new TypeToken<List<String>>() {}.getType();
                    List<String> lore = gson.fromJson(rs.getString("lore"), listType);
                    int colNum = rs.getInt("col_num");
                    ColorKeys colorKey = ColorKeys.valueOf(rs.getString("color_key"));
                    String colorDisplay = rs.getString("color_display");
                    List<String> hex = gson.fromJson(rs.getString("hex"), listType);

                    Color color = new Color(colorDisplay, colorKey, hex);
                    Title title = new Title(unique, display, lore, colNum, color);

                    registry.addTitle(title);
                }
            } catch (SQLException e) {
                LOGGER.severe("Error loading titles: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void remove(String unique) {
        if (!db.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            db.connect();
            String sql = "DELETE FROM titles WHERE unique_id = ?";
            try (Connection conn = db.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, unique);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.severe("Error removing title: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
