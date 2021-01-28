package com.reimb.repo;


import java.sql.CallableStatement;
//import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
//import java.sql.ResultSet;
import java.util.List;

import com.reimb.config.EnvironmentConnectionUtil;
import com.reimb.model.Reimbursement;


public class ReimbursementDao implements DaoContract <Reimbursement, Integer> {
	
	EnvironmentConnectionUtil ev = new EnvironmentConnectionUtil();

	@Override
	public List<Reimbursement> findAll() {
		return null;
	}

	@Override
	public Reimbursement findById(Integer i) {
		return null;
	}

	@Override
	public int update(Reimbursement t) {
		return 0;
	}

	@Override
	public boolean create(Reimbursement t) {
		Connection conn = ev.getConnection();
		String sql = "insert into reimbursement.ers_reimbursement (reimb_amount, reimb_description, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)  values (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, t.getAmount());
			ps.setString(2, t.getDescription());
			ps.setInt(3, t.getAuthor());
			ps.setInt(4, t.getResolver());
			ps.setInt(5, t.getStatus());
			ps.setInt(6, t.getType());
			ps.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(String name) {
		return false;
	}

	@Override
	public Reimbursement findByName(String name) {
		return null;
	}
	
	// Use callable statements to calculate total amount of reimbursements
	public Float viewTotalReimbursements() {
			try {
				Connection conn = ev.getConnection();
				CallableStatement cs = conn.prepareCall("{ ? = call reimbursement.reimbursementsOutstanding() }");
				cs.registerOutParameter(1, Types.REAL);
				cs.execute();
				Float result = cs.getFloat(1);
				return result;

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
		
	
