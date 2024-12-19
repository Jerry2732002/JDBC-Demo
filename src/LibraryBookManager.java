package src;

import java.sql.*;

public class LibraryBookManager {
    private static final String URL = "jdbc:mysql://localhost:3306/fdc_training";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static void listAllBooks() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT * FROM Books");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                System.out.println("Table empty");
                return;
            }
            int bookIdWidth = 10;
            int titleWidth = 30;
            int copiesAvailableWidth = 20;

            System.out.printf("%-" + bookIdWidth + "s%-" + titleWidth + "s%-" + copiesAvailableWidth + "s%n", "Book Id", "Title", "Copies Available");
            while (resultSet.next()) {
                int bookId = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                int copiesAvailable = resultSet.getInt("CopiesAvailable");

                System.out.printf("%-" + bookIdWidth + "d%-" + titleWidth + "s%-" + copiesAvailableWidth + "d%n", bookId, title, copiesAvailable);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void listAllBorrowedBooks() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT Books.Title, DATEDIFF(CURDATE(), BorrowedBooks.BorrowDate) AS DaysBorrowed FROM Books JOIN BorrowedBooks ON BorrowedBooks.BookID = Books.BookID ORDER BY DaysBorrowed DESC; ");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                System.out.println("Table empty");
                return;
            }

            while (resultSet.next()) {
                System.out.print("Book Title :" + resultSet.getString("Title") + "\t\t\t");
                System.out.println("Days Borrowed :" + resultSet.getInt("DaysBorrowed"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void booksAvailable() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT Books.Title, Books.CopiesAvailable FROM Books WHERE Books.CopiesAvailable > 0");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                System.out.println("Table empty");
                return;
            }

            while (resultSet.next()) {
                System.out.print("Book Title :" + resultSet.getString("Title") + "\t\t\t");
                System.out.println("No Of Copies Available :" + resultSet.getInt("CopiesAvailable"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void findBookByTitle(String title) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT Books.* FROM Books WHERE Books.Title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            int bookIdWidth = 10;
            int titleWidth = 30;
            int copiesAvailableWidth = 20;
            System.out.printf("%-" + bookIdWidth + "s%-" + titleWidth + "s%-" + copiesAvailableWidth + "s%n", "Book Id", "Title", "Copies Available");
            while (resultSet.next()) {
                int bookId = resultSet.getInt("BookID");
                int copiesAvailable = resultSet.getInt("CopiesAvailable");
                System.out.printf("%-" + bookIdWidth + "d%-" + titleWidth + "s%-" + copiesAvailableWidth + "d%n", bookId, title, copiesAvailable);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkBookByTitle(String title) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT Books.* FROM Books WHERE Books.Title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void borrow(String title) {
        if (!checkBookByTitle(title)) {
            System.out.println("Book Cannot Be Borrowed");
            return;
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT Books.CopiesAvailable FROM Books WHERE Books.Title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            int booksAvailable = 0;
            if (resultSet.next()){
                booksAvailable = resultSet.getInt("CopiesAvailable");
            }

            PreparedStatement statement2 = con.prepareStatement("UPDATE Books SET CopiesAvailable = ? WHERE Books.Title = ?");
            statement2.setInt(1, booksAvailable - 1);
            statement2.setString(2, title);
            statement2.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
