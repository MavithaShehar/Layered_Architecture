package dao;

import db.DBConnection;
import model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomersDAOimpl implements CustomerDAO {

    @Override
    public List<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {

        ArrayList<CustomerDTO> customerDTO = new ArrayList<>();

           Connection connection = DBConnection.getDbConnection().getConnection();
           Statement stm = connection.createStatement();
           ResultSet rst = stm.executeQuery("SELECT * FROM Customer");


            while (rst.next()){
                CustomerDTO customer = new CustomerDTO(
                        rst.getString("id"),
                        rst.getString("name"),
                        rst.getString("address")
                );
                customerDTO.add(customer);
            }

        return customerDTO ;

    }


    public boolean saveAllCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer (id,name, address) VALUES (?,?,?)");
        pstm.setString(1, customerDTO.getId());
        pstm.setString(2, customerDTO.getName());
        pstm.setString(3, customerDTO.getAddress());
        pstm.executeUpdate();

        return 0 < pstm.executeUpdate();
    }

    public boolean updateCustomer(CustomerDTO customerDTO ) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?, address=? WHERE id=?");
        pstm.setString(1, customerDTO.getId());
        pstm.setString(2, customerDTO.getName());
        pstm.setString(3, customerDTO.getAddress());
        pstm.executeUpdate();

        return 0 < pstm.executeUpdate();

    }

    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM Customer WHERE id=?");
        pstm.setString(1, id);

        return pstm.executeQuery().next();

    }

    public boolean customerDelete(String id) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
        pstm.setString(1, id);
        pstm.executeUpdate();

        return pstm.executeQuery().next();
    }

    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {

             Connection connection = DBConnection.getDbConnection().getConnection();
            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1;");
            if (rst.next()) {
                String id = rst.getString("id");
                int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
                return String.format("C00-%03d", newCustomerId);
            } else {
                return "C00-001";
            }
    }


    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
        pstm.setString(1, newValue + "");
        ResultSet rst = pstm.executeQuery();
        rst.next();
        CustomerDTO customerDTO = new CustomerDTO(newValue + "", rst.getString("name"), rst.getString("address"));

        return customerDTO;
    }
}
