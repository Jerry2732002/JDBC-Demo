import src.LibraryBookManager;
import src.LibraryMemberManager;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        LibraryMemberManager.checkMemberById(122);
    }
}
