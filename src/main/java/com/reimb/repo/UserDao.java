package com.reimb.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.reimb.config.EnvironmentConnectionUtil;
import com.reimb.model.User;

public class UserDao implements DaoContract<User, Integer>{
	
	
EnvironmentConnectionUtil ev = new EnvironmentConnectionUtil();
	

	@Override
	public List<User> findAll() {
		return null;
	}


	@Override
	public User findById(Integer i) {
		return null;
	}


	@Override
	public int update(User t) {
		return 0;
	}


	@Override
	public boolean create(User t) {
		Connection conn = ev.getConnection();
		String sql = "insert into reimbursement.ers_users (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) values(?,?,?,?,?, 2);";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, t.getUsername());
			ps.setString(2, t.getPassword());
			ps.setString(3, t.getFirstName());
			ps.setString(4, t.getLastName());
			ps.setString(5, t.getEmail());
			ps.executeUpdate();
			conn.close();
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	} 
	
	// Only used in test cases so I can reset the DB after adding a user
	@Override
	public boolean delete(String name) {
		try {
			Connection conn = ev.getConnection();
			String sql = "delete from reimbursement.ers_users where ers_username = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public User findByName(String name) {
		return null;
	}

	public UserDao() {
		
	}
	
	public User findByUsernameAndPassword(String username, String password) {
		Connection conn = ev.getConnection();
		String sql = "select * from reimbursement.view2 where ers_username = ? and ers_password = ? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User u = new User(rs.getInt("ers_users_id"), username, password, rs.getString("user_first_name"), rs.getString("user_last_name"), rs.getString("user_email"), 
													  rs.getInt("user_role_id"), rs.getString("user_role"));
				rs.close();
				conn.close();
				return u;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	} 

}
