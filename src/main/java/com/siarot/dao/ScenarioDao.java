package com.siarot.dao;

import com.siarot.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public ScenarioDao() {
        DatabaseConnection.initializeDatabase();
    }

    // [1] Add Record
    public boolean insert(ScenarioEntity order) {
        String sql = "INSERT INTO orders (customer_name, drink_item, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.customerName());
            ps.setString(2, order.drinkItem());
            ps.setDouble(3, order.price());
            ps.setString(4, order.status());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert error: " + e.getMessage());
            return false;
        }
    }

    // [2] View Records
    public List<ScenarioEntity> findAll() {
        List<ScenarioEntity> orders = new ArrayList<>();
        String sql = "SELECT id, customer_name, drink_item, price, status FROM orders ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("drink_item"),
                        rs.getDouble("price"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }
        return orders;
    }
}
