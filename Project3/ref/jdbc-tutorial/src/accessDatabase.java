/***
 * File        : accessDatabase.java
 * Description : Simple program to illustrate concepts in tutorial
 */

import java.sql.* ;

public class accessDatabase{
    public static void main(String[] args) 
        throws SQLException {

        String bar, beer ;
        float price ;
        Connection c = null ;

        try {
			/* load the driver*/
            Class.forName("com.mysql.jdbc.Driver"); 

			/* create an instance of a Connection object */
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/TEST", "cs144", ""); 
			
			/*You can think of a JDBC Statement object as a channel
			sitting on a connection, and passing one or more of your
			SQL statements (which you ask it to execute) to the DBMS*/

            Statement s = c.createStatement() ;

            s.executeUpdate("CREATE TABLE Sells(bar VARCHAR(40), beer VARCHAR(40), price REAL)" ) ;
            s.executeUpdate("INSERT INTO Sells VALUES('Bar Of Foo', 'BudLite', 2.00)") ;

            ResultSet rs = s.executeQuery("SELECT * FROM Sells") ;
            while( rs.next() ){
                 bar = rs.getString("bar");
                 beer = rs.getString("beer");
                 price = rs.getFloat("price");
                 System.out.println(bar + " sells " + beer + " for " + price + " dollars.");
            }
            /* close the resultset, statement and connection */
            rs.close();
            s.close();
            c.close();
       } catch (ClassNotFoundException ex){
            System.out.println(ex);
       } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
       }
   }
}
