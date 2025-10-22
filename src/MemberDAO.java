import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    
    public boolean addMember(Member member) {
        // Get the next available consecutive ID
        int nextId = getNextAvailableId();
        if (nextId == -1) return false;
        
        String sql = "INSERT INTO members (id, name, email, phone, membership_type, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (conn == null || pstmt == null) {
                System.err.println("Database connection failed!");
                return false;
            }
            
            pstmt.setInt(1, nextId);
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getMembershipType());
            pstmt.setDate(6, Date.valueOf(member.getStartDate()));
            pstmt.setDate(7, Date.valueOf(member.getEndDate()));
            pstmt.setString(8, member.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
            return false;
        }
    }
    
    private int getNextAvailableId() {
        String sql = "SELECT COALESCE(MIN(m1.id + 1), 1) AS next_id " +
                     "FROM members m1 " +
                     "WHERE NOT EXISTS (SELECT 1 FROM members m2 WHERE m2.id = m1.id + 1)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            
            if (conn == null || stmt == null || rs == null) return 1;
            
            if (rs.next()) {
                return rs.getInt("next_id");
            }
            
            return 1;
            
        } catch (SQLException e) {
            System.err.println("Error getting next available ID: " + e.getMessage());
            return 1;
        }
    }
    
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            
            if (conn == null) {
                System.err.println("Database connection failed!");
                return members;
            }
            
            while (rs != null && rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipType(rs.getString("membership_type"));
                member.setStartDate(rs.getDate("start_date").toLocalDate());
                member.setEndDate(rs.getDate("end_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                
                members.add(member);
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        
        return members;
    }
    
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET name=?, email=?, phone=?, membership_type=?, start_date=?, end_date=?, status=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (conn == null || pstmt == null) {
                System.err.println("Database connection failed!");
                return false;
            }
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getMembershipType());
            pstmt.setDate(5, Date.valueOf(member.getStartDate()));
            pstmt.setDate(6, Date.valueOf(member.getEndDate()));
            pstmt.setString(7, member.getStatus());
            pstmt.setInt(8, member.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (conn == null || pstmt == null) {
                System.err.println("Database connection failed!");
                return false;
            }
            
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }
    
    public List<Member> searchMembersByName(String name) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE name LIKE ? ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (conn == null || pstmt == null) {
                return members;
            }
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipType(rs.getString("membership_type"));
                member.setStartDate(rs.getDate("start_date").toLocalDate());
                member.setEndDate(rs.getDate("end_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                
                members.add(member);
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching members: " + e.getMessage());
        }
        
        return members;
    }
    
    public List<Member> getExpiringMembers(int days) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) AND status = 'Active' ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(sql) : null) {
            
            if (conn == null || pstmt == null) {
                return members;
            }
            
            pstmt.setInt(1, days);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipType(rs.getString("membership_type"));
                member.setStartDate(rs.getDate("start_date").toLocalDate());
                member.setEndDate(rs.getDate("end_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                
                members.add(member);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expiring members: " + e.getMessage());
        }
        
        return members;
    }
}