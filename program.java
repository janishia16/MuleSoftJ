package movies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class moviedb {

	public static void main(String[] args) {
		
		String jdbcURL = "jdbc:sqlite:movie.db";				//creating a database movie.db
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(jdbcURL);				//connecting to the database movie.db
			System.out.println("Connection established");
			
			try {
				deleteTable(conn);
			}
			catch(Exception ignored) {
				System.out.println("\nDatabase does not exists");
			}
			
			System.out.println("\nDatabase exists");
			
			createTable(conn);				//creating new table Movies
			
			System.out.println("\nMovies Table Created");
			
			System.out.println("\nInserting data");
											//inserting data to Movies tables
			insertMovie(conn, "Inception","Leonardo DiCaprio","Marion Cotillard","Christopher Nolan",2016);
			insertMovie(conn, "Joker","Joaquin Phoenix","Zazie Beetz","Todd Phillips",2019);
			insertMovie(conn, "Avatar","Sam Worthington","Zoe Salda�a","James Cameron",2009);
			insertMovie(conn, "The Conjuring","Patrick Wilson","Vera Farmiga","James Wan",2013);
			insertMovie(conn, "Thor","Chris Hemsworth","Natalie Portman","Kenneth Branagh",2011);
			insertMovie(conn, "Iron Man","Robert Downey Jr.","Gwyneth Paltrow","Jon Favreau",2008);
			insertMovie(conn, "Doctor Strange","Benedict Cumberbatch","Rachel McAdams","Scott Derrickson",2016);
			insertMovie(conn, "Deadpool","Ryan Reynolds","Morena Baccarin","Tim Miller",2016);
			insertMovie(conn, "Ant-Man","Paul Rudd","Evangeline Lilly","Peyton Reed",2015);
			insertMovie(conn, "Free Guy","Ryan Reynolds","Jodie Comer","Shawn Levy",2021);
			
			System.out.println("\nDisplaying Movies table data");
			displayDatabase(conn, "Movies");					//displaying the Movies table data
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
		finally {
			if(conn != null) {
				try {
					conn.close();
				}
				catch(SQLException e){
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	private static void displayDatabase(Connection conn, String table) throws SQLException{
		String select = "SELECT rowid,* from " + table;
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(select);
		
		System.out.println("\n------------------------- "+ table + " -------------------------");
		System.out.println("SINo. | Movie | Actor | Actress | Director | Year of release\n");
		while(result.next())
		{
			Integer id = result.getInt("rowid");
			String name = result.getString("Name");
			String actor = result.getString("Actor");
			String actress = result.getString("Actress");
			String director = result.getString("Director");
			Integer year = result.getInt("Year");
		
			System.out.println(id + " | " + name + " | " + actor + " | " + actress + " | " + director + " | " + year);
		}
		System.out.println("\n------------------------- END -------------------------\n");
		
		String actorname;
		boolean flag = false;
		System.out.print("\n\nEnter the actor name to search : ");
		Scanner in = new Scanner(System.in);
		actorname = in.nextLine();
		in.close();
	
		ResultSet res = stmt.executeQuery(select);
		while(res.next())
		{
			Integer id = res.getInt("rowid");
			String name = res.getString("Name");
			String actor = res.getString("Actor");
			
			if(actor.equals(actorname)) {
				System.out.println("\nSINo. = " + id);
				System.out.println("Movie = " + name);
				flag = true;
			}
		}
		if(!flag)
			System.out.println("No record found for actor "+actorname);
			
	}
	
	private static void insertMovie(Connection conn, String name, String actor, String actress, String director, int year) throws SQLException{
		String insert = "INSERT INTO Movies(name,actor,actress,director,year) VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(insert);
		pstmt.setString(1, name);
		pstmt.setString(2, actor);
		pstmt.setString(3, actress);
		pstmt.setString(4, director);
		pstmt.setInt(5, year);
		pstmt.executeUpdate();
	}
	
	private static void createTable(Connection conn) throws SQLException{
		String create = "" + 
				"CREATE TABLE Movies " + 
				"( " + 
				"name varchar(50), " + 
				"actor varchar(50), " + 
				"actress varchar(50), " + 
				"director varchar(50), " + 
				"year integer " + 
				"); " + 
				"";
		Statement stmt = conn.createStatement();
		stmt.execute(create);
	}
	
	private static void deleteTable(Connection conn) throws SQLException{
		String delete = "DROP TABLE Movies";
		Statement stmt = conn.createStatement();
		stmt.execute(delete);
	}

}
