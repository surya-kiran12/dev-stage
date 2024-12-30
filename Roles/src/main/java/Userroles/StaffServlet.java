package Userroles;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
 
@WebServlet("/stfserv")
public class StaffServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://192.168.1.152:3306/curetrack_test";
    private static final String DB_USER = "mohana21";
    private static final String DB_PASSWORD = "Mohana21@123";
 
    
    private Connection getConnection() throws SQLException {
	    try {
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    }catch(ClassNotFoundException e) {
	    	e.printStackTrace();
	    	throw new SQLException("JDBC Driver not found");
	    }
		return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
	    }
 
 
    
    private void writeResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        try (PrintWriter out = response.getWriter()) {
            out.println(message);
      }
          
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");

	        
	        if (action == null) {
	            handleFetch(request, response);  // Default action is fetch
	        } else {
	            switch (action) {
	                case "update":
	                    handleUpdate(request, response);
	                    break;
	                case "delete":
	                    handleDelete(request, response);
	                    break;
	                default:
	                    writeResponse(response, "Invalid action type.", HttpServletResponse.SC_BAD_REQUEST);
	                    break;
	            }
	        }
	    }

	    
	    private void handleFetch(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.setContentType("application/json");

	        List<Staff> staffList = new ArrayList<>();
	        String fetchQuery = "call curetrack_test.staffmngt();";

	        try (Connection connection = getConnection();
	             Statement stmt = connection.createStatement();
	             ResultSet rs = stmt.executeQuery(fetchQuery)) {

	         
	            System.out.println("Database connection successful.");

	            while (rs.next()) {
	                Staff staff = new Staff();
	                staff.setStaff_id(rs.getInt("staff_id"));
	                staff.setName(rs.getString("name"));
	                staff.setRole(rs.getString("role"));
	                staff.setDepartment(rs.getString("department"));
	                staff.setDesignation(rs.getString("designation"));
	                staffList.add(staff);
	            }

	           
	            Gson gson = new Gson();
	            String jsonResponse = gson.toJson(staffList);
	            try (PrintWriter out = response.getWriter()) {
	                out.println(jsonResponse);
	            }

	        } catch (SQLException e) {
	           
	            System.err.println("Error executing query: " + e.getMessage());
	            e.printStackTrace();
	            writeResponse(response, "Error retrieving staff data: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }
    
	    
	    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        String idParam = request.getParameter("id");
	        if (idParam == null || idParam.isEmpty()) {
	            writeResponse(response, "Staff ID is required for update.", HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        int id;
	        try {
	            id = Integer.parseInt(idParam);
	        } catch (NumberFormatException e) {
	            writeResponse(response, "Invalid Staff ID format. It must be a number.", HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }
	        
	        ObjectMapper mapper = new ObjectMapper();
	        Staff staff;
	        try {
	            staff = mapper.readValue(request.getInputStream(), Staff.class); // Read once
	        } catch (Exception e) {
	            writeResponse(response, "Invalid JSON format.", HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        String name = staff.getName();
	        String department = staff.getDepartment();
	        String designation = staff.getDesignation();

	       
	        System.out.println("Received Values: ");
	        System.out.println("ID: " + id);
	        System.out.println("Name: " + name);
	        System.out.println("Department: " + department);
	        System.out.println("Designation: " + designation);

	        
	        if (name == null || name.trim().isEmpty()) {
	            writeResponse(response, "Name is required for update.", HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	       
	        String updateQuery = "UPDATE staff SET name=?, department=?, designation=? WHERE staff_id=?";
	        try (Connection conn = getConnection();
	             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

	            stmt.setString(1, name);
	            stmt.setString(2, department);
	            stmt.setString(3, designation);
	            stmt.setInt(4, id);

	            int rowsUpdated = stmt.executeUpdate();

	            System.out.println("Rows updated: " + rowsUpdated);

	            if (rowsUpdated > 0) {
	                writeResponse(response, "Staff member updated successfully.", HttpServletResponse.SC_OK);
	            } else {
	                writeResponse(response, "Staff member not found.", HttpServletResponse.SC_NOT_FOUND);
	            }
	        } catch (SQLException e) {
	            System.err.println("SQLException: " + e.getMessage());
	            e.printStackTrace();
	            writeResponse(response, "Error updating staff member: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }

	   
	    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        String idParam = request.getParameter("id");
	        if (idParam == null || idParam.isEmpty()) {
	            writeResponse(response, "Staff ID is required for deletion.", HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        int id = Integer.parseInt(idParam);
	        String deleteQuery1 = "DELETE FROM user_register WHERE user_id";
	        String deleteQuery2 = "DELETE FROM staff WHERE staff_id=?";

	        try (Connection connection = getConnection();
	        		PreparedStatement stmt1 = connection.prepareStatement(deleteQuery1);
	             PreparedStatement stmt2 = connection.prepareStatement(deleteQuery2)) {
	        	stmt1.setInt(1, id);
	            stmt2.setInt(2, id);

	            int rowsDeleted = stmt2.executeUpdate();
	            if (rowsDeleted > 0) {
	                writeResponse(response, "Staff member deleted successfully.", HttpServletResponse.SC_NO_CONTENT);
	            } else {
	                writeResponse(response, "Staff member not found.", HttpServletResponse.SC_NOT_FOUND);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            writeResponse(response, "Error deleting staff member.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

