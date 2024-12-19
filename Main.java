import src.LibraryBookManager;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        LibraryBookManager.listAllBooks();
        LibraryBookManager.borrow("The Great Gatsby");
        System.out.println("\n\n\n");
        LibraryBookManager.listAllBooks();
    }
}
