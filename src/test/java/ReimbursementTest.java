import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import org.junit.Test;

import com.reimb.config.EnvironmentConnectionUtil;
import com.reimb.controller.LoginController;
import com.reimb.controller.NewEmployeeController;
import com.reimb.controller.ReimbursementController;
import com.reimb.model.Reimbursement;
import com.reimb.model.User;
import com.reimb.repo.ReimbursementDao;
import com.reimb.repo.UserDao;

public class ReimbursementTest {
	
	@Test
	public void getConnectionTest() {
		EnvironmentConnectionUtil a = new EnvironmentConnectionUtil();
		Connection c =  a.getConnection();
		assertNotNull(c);
	}
	
	@Test
	public void addEmployeeTest() throws IOException {
		NewEmployeeController a = new NewEmployeeController(null, null);
		User u                  = new User(0, "nath22", "A", "Nathan", "Jones","njones@gmail.com", 2, "");
		new UserDao().delete(u.getUsername());
		boolean add             = a.addEmployee(u);
		assert (add);
		assertEquals(NewEmployeeController.result, "Successfully added employee " + u.getUsername());
		new UserDao().delete(u.getUsername());
	}
	
	@Test
	public void addReimbTest() throws IOException {
		Reimbursement r = new Reimbursement( 2.00, "I need money", 2, 4, 1, 1);
		boolean add = new ReimbursementDao().create(r);
		assert (add);
		
	}
	
	@Test
	public void allReimbursementsTest() throws IOException {
		List<Integer> l =  new ReimbursementController(null, null).retrieveAllReimbursements(); 
		assert (l.size() > 0);
	}
	
	@Test
	public void countReimbursementsByIdTest() throws IOException {
		int count =  new ReimbursementController(null, null).retrieveReimbursementsByUserId(2); 
		assert (count > 0);
	}
	@Test
	public void loginUserNameAndPasswordTest() throws IOException {
		assert  ( new LoginController(null, null).loginUserNameAndPassword("emp1", "A"));
		assert  ( new LoginController(null, null).loginUserNameAndPassword("man1", "A"));
		assert  (!new LoginController(null, null).loginUserNameAndPassword("emp0", "A"));
	}
	

	
}

