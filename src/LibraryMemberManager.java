package src;

import java.sql.*;

public class LibraryMemberManager {
    private static final String URL = "jdbc:mysql://localhost:3306/fdc_training";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    public static void listAllMembers() {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT * FROM Members");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                System.out.println("Table empty");
                return;
            }
            while (resultSet.next()) {
                int memberID =  resultSet.getInt("MemberID");
                System.out.println("Member ID :" + memberID);
                System.out.println("First Name :" + resultSet.getString("FirstName"));
                System.out.println("Last Name :" + resultSet.getString("LastName"));
                PreparedStatement statement2 = con.prepareStatement("SELECT Books.Title FROM BorrowedBooks JOIN Books ON Books.BookID = BorrowedBooks.BorrowID WHERE BorrowedBooks.MemberID = ?");
                statement2.setInt(1,memberID);
                ResultSet resultSet2 = statement2.executeQuery();
                System.out.print("Books borrowed :");
                while (resultSet2.next()) {
                    System.out.print(resultSet2.getString("Title")+"\t");
                }
                System.out.println("\n************************************");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
