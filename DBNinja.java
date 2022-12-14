package cpsc4620;
//By Luke Smith and Dinesh Thakur

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

//Hello DBNinja comment

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery,
	// and sizes and
	// crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "small";
	public final static String size_m = "medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";

	/**
	 * This function will handle the connection to the database
	 * 
	 * @return true if the connection was successfully made
	 * @throws SQLException
	 * @throws IOException
	 */
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 *
	 * @param o order that needs to be saved to the database
	 * @throws SQLException
	 * @throws IOException
	 * @requires o is not NULL. o's ID is -1, as it has not been assigned yet. The
	 *           pizzas do not exist in the database yet, and the topping inventory
	 *           will allow for these pizzas to be made
	 * @ensures o will be assigned an id and added to the database, along with all
	 *          of it's pizzas. Inventory levels will be updated appropriately
	 */
	public static void addOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 */
		//TODO

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts to that bridge table and 
		 * instance of topping usage to that bridge table if you have't accounted
		 * for that somewhere else.
		 */
		String query = "insert into PIZZA VALUES" + "(?,?,?,?,?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,p.getPizzaID());
		ps.setString(2, p.getCrustType());
		ps.setString(3, p.getSize());
		ps.setBoolean(4, true);
		ps.setString(5, p.getPizzaDate());
		ps.setDouble(6,p.getCustPrice());
		ps.setDouble(7,p.getBusPrice());
		ps.setInt(8,p.getOrderID());
		ps.executeUpdate();

		//if (p.getSize() == "small")

		for(int i = 0; i < p.getToppings().size(); ++i){
			//Integer newAmt = p.getToppings().get(i).getCurINVT();
			Integer newAmt = 10;
			query = "insert into PIZZATOP VALUES " + "(?,?,?);" +
					"UPDATE TOPPING SET t_Inv = " + "? where t_ID = ?;";
			ps = conn.prepareStatement(query);
			Integer toppID = p.getToppings().get(i).getTopID();
			ps.setInt(1, toppID);
			ps.setInt(2, p.getPizzaID());
			ps.setBoolean(3, p.getIsDoubleArray()[toppID]);
			ps.setInt(4, newAmt);
			ps.setInt(5, toppID);
			ps.executeUpdate();
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static int getMaxPizzaID() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * A function I needed because I forgot to make my pizzas auto increment in my DB.
		 * It goes and fetches the largest PizzaID in the pizza table.
		 * You wont need this function if you didn't forget to do that
		 */
		String query = "SELECT COUNT(p_ID) FROM PIZZA;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		Integer max = 0;
		while (rset.next()){
			max = rset.getInt(1);
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return max;
	}
	
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this function will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This function should 2 two things.
		 * We need to update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * and we need to add that instance of topping usage to the pizza-topping bridge if we haven't done that elsewhere
		 * Ideally, you should't let toppings go negative. If someone tries to use toppings that you don't have, just print
		 * that you've run out of that topping.
		 */
		
		//TODO
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		if (d.isPercent() == true){
			Double cPrice = p.getCustPrice();
			Double percOff = d.getAmount() / 100;
			Double discount = 1 - percOff;
			p.setCustPrice(cPrice * discount);
		}
		else{
			Double cPrice = p.getCustPrice();
			p.setCustPrice(cPrice - d.getAmount());
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		//connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		if (d.isPercent() == true){
			Double cPrice = o.getCustPrice();
			Double percOff = d.getAmount() / 100;
			Double discount = 1 - percOff;
			o.setCustPrice(cPrice * discount);
		}
		else{
			Double cPrice = o.getCustPrice();
			o.setCustPrice(cPrice - d.getAmount());
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	


	
	public static void addCustomer(Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This should add a customer to the database
		 */
		//insert into CUSTOMER VALUES(1, 'DineIn', 'Customer', '000-000-0000');

		String query = "insert into CUSTOMER VALUES" + "(?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,c.getCustID());
		ps.setString(2, c.getFName());
		ps.setString(3, c.getLName());
		ps.setString(4, c.getPhone());
		ps.executeUpdate();
		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	
	public static void CompleteOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to mark an order as complete in the DB. You may have a boolean field
		 * for this, or maybe a completed time timestamp. However you have it.
		 */

		String query = "Update ORDERS SET o_IsComplete = 1 WHERE o_OrderID = ?;";
		PreparedStatement ps = conn.prepareStatement(query);
		Integer oId = o.getOrderID();
		System.out.println("wabbajack"+oId);
		ps.setInt(1,oId);
		o.setIsComplete(1);
		ps.executeUpdate();

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	
	
	public static void AddToInventory(Topping t, double toAdd) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Adds toAdd amount of topping to topping t.
		 */

		double newAmt = toAdd + t.getCurINVT();
		t.setCurINVT((int)newAmt);
		String query = "Update TOPPING SET t_Inv = ? WHERE t_ID = ?;";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setDouble(1,newAmt);
		ps.setInt(2,t.getTopID());
		ps.executeUpdate();
		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	

	public static void printInventory() throws SQLException, IOException {
		connect_to_db();

		/*
		 * I used this function to PRINT (not return) the inventory list.
		 * When you print the inventory (either here or somewhere else)
		 * be sure that you print it in a way that is readable.
		 * 
		 * 
		 * 
		 * The topping list should also print in alphabetical order
		 */

		ArrayList<Topping> t = DBNinja.getInventory();
		System.out.println("ID\tName\t\t\t\t\t  Current Inv.");
		for (int i = 0; i< t.size(); i++){
			System.out.printf("%-3s %-25s %-30s\n",t.get(i).getTopID(),t.get(i).getTopName(),
					t.get(i).getCurINVT());
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION		
	}
	
	
	public static ArrayList<Topping> getInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		* This function actually returns the toppings. The toppings
		* should be returned in alphabetical order if you don't
		* plan on using a printInventory function
		*/
		
		String query = "Select * from TOPPING ORDER BY t_Name ASC;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		ArrayList<Topping> topping = new ArrayList<Topping>();
		while (rset.next()){
			//create topping object
			Topping t = new Topping(rset.getInt(1), rset.getString(2),
					rset.getDouble(3), rset.getDouble(4),rset.getDouble(5),
					rset.getDouble(6), rset.getDouble(7), rset.getDouble(8),
					rset.getInt(9), rset.getInt(10));
			//add it to the topping array
			topping.add(t);
		}
		
		conn.close();

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return topping;
	}


	public static ArrayList<Order> getCurrentOrders(String filterDate) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */


		ArrayList<Order> oList = new ArrayList<Order>();

		connect_to_db();
		String query = "";
		if (filterDate == "noDate"){
			query = "Select * From ORDERS order by o_Date DESC;";
		}
		else{
			query = "Select * FROM ORDERS where o_Date >= '" + filterDate + "' order by o_Date DESC;";
		}
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while(rset.next())
		{
			Integer tempID = rset.getInt(1);
			String tempType = rset.getString(2);
			Double tempPrice = rset.getDouble(3);
			Double tempCost = rset.getDouble(4);
			String orderTime = rset.getString(5);
			String tempDate = rset.getString(6);
			Integer tempIsComplete = rset.getInt(7);
			Integer tempCID = rset.getInt(8);

			Order o = new Order(tempID, tempCID, tempType, tempDate, tempPrice, tempCost, tempIsComplete);
			oList.add(o);
		}
		conn.close();

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return oList;
	}
	
	public static ArrayList<Order> sortOrders(ArrayList<Order> list)
	{
		/*
		 * This was a function that I used to sort my arraylist based on date.
		 * You may or may not need this function depending on how you fetch
		 * your orders from the DB in the getCurrentOrders function.
		 */
		
		
		//TODO
		//DONT NEED THIS
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;
		
	}
	
	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		//Helper function I used to help sort my dates. You likely wont need these
		
		
		
		
		//TODO
		//I DONT THINK WE NEED THIS
		
		
		
		return false;
	}
	
	
	/*
	 * The next 3 private functions help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base price (for the customer) for that size and crust pizza Depending on how
		// you store size & crust in your database, you may have to do a conversion
		String query = "Select pr_Price from BASEPRICE WHERE pr_Size=" + size + " and pr_Crust=" + crust + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while (rset.next()){
			bp = rset.getDouble(1);
		}
		
		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}
	
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
		/*
		 *This is a helper function I used to fetch the name of a customer
		 *based on a customer ID. It actually gets called in the Order class
		 *so I'll keep the implementation here. You're welcome to change
		 *how the order print statements work so that you don't need this function.
		 */
		connect_to_db();
		String ret = "";
		String query = "Select c_FirstName, c_LastName From Customer WHERE c_CustID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			ret = rset.getString(1) + " " + rset.getString(2);
		}
		conn.close();
		return ret;
	}
	
	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base cost (for the business) for that size and crust pizza Depending on how
		// you store size and crust in your database, you may have to do a conversion
		
		String query = "Select pr_Cost from BASEPRICE WHERE pr_Size=" + size + " and pr_Crust=" + crust + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while (rset.next()){
			bp = rset.getDouble(1);
		}
		
		conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}

	
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		ArrayList<Discount> discs = new ArrayList<Discount>();
		connect_to_db();
		//returns a list of all the discounts.
		String query = "select * from discount;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		while (rset.next()){
			Integer orderId = rset.getInt(1);
			String dName = rset.getString(2);
			Double dAmt = rset.getDouble(3);
			Boolean isPerc = rset.getBoolean(4);

			Discount d = new Discount(orderId, dName, dAmt, isPerc);
			discs.add(d);
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return discs;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		ArrayList<Customer> custs = new ArrayList<Customer>();
		connect_to_db();
		/*
		 * return an arrayList of all the customers. These customers should
		 *print in alphabetical order, so account for that as you see fit.
		*/

		//in progress
		connect_to_db();
		String query = "Select * From Customer;";
		Statement stmt = conn.createStatement();
		ResultSet resSet = stmt.executeQuery(query);

		while(resSet.next())
		{
			Customer c = new Customer(resSet.getInt(1), resSet.getString(2),
					resSet.getString(3), resSet.getString(4));
			custs.add(c);
		}
		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return custs;
	}
	
	public static int getNextOrderID() throws SQLException, IOException
	{
		/*
		 * A helper function I had to use because I forgot to make
		 * my OrderID auto increment...You can remove it if you
		 * did not forget to auto increment your orderID.
		 */
		
		
		//TODO
		//I dont think we need this
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return -1;
	}
	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print (other than that it should
		 * be in alphabetical order by name), just make sure it's readable.
		 */

		String query = "select * from toppingpopularity;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		System.out.println("Topping\t\t\t\tToppingCount");
		while (rset.next()){
			System.out.printf("%-20s", rset.getString(1));
			System.out.printf("%-10s", rset.getString(2));
			System.out.println();
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */

		String query = "select * from profitbypizza;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		System.out.println("Size\t\t\tCrust\t\t\tProfit\t\t\tLast Order Date");
		while (rset.next()){
			System.out.printf("%-16s", rset.getString(1));
			System.out.printf("%-16s", rset.getString(2));
			System.out.printf("%-16s", rset.getString(3));
			System.out.printf("%-15s", rset.getString(4));
			System.out.println();
		}

		conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */


		String query = "select * from profitbyordertype;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		System.out.println("Customer Type       Order Month         Total Order Price   Total Order Cost    Profit");
		while (rset.next()){
			System.out.printf("%-20s", rset.getString(1));
			System.out.printf("%-20s", rset.getString(2));
			System.out.printf("%-20s", rset.getString(3));
			System.out.printf("%-20s", rset.getString(4));
			System.out.printf("%-20s", rset.getString(5));
			System.out.println();
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION	
	}

	public static Topping getTopping(Integer topID) throws SQLException, IOException
	{
		ArrayList<Topping> t = DBNinja.getInventory();
		Topping desiredTopping = t.get(1);

		for (int i = 0; i< t.size(); i++){
			if (topID == t.get(i).getTopID()){
				desiredTopping = t.get(i);
				return desiredTopping;
			}
		}

		return desiredTopping;
	}

	public static Order getOrder(Integer orderID) throws SQLException, IOException
	{
		connect_to_db();
		String query = "Select * from ORDERS where o_OrderID = " + orderID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		String tempType = "";
		String print = "";

		while(rset.next())
		{
			Integer tempID = rset.getInt(1);
			tempType = rset.getString(2);
			Double tempPrice = rset.getDouble(3);
			Double tempCost = rset.getDouble(4);
			String orderTime = rset.getString(5);
			String tempDate = rset.getString(6);
			Integer tempIsComplete = rset.getInt(7);
			Integer tempCID = rset.getInt(8);

			Order o = new Order(tempID, tempCID, tempType, tempDate, tempPrice, tempCost, tempIsComplete);
			conn.close();
			return o;
		}
		return null;
	}

	public static void printAddress(String print, Integer orderID) throws SQLException, IOException{
		connect_to_db();

		String query2 = "Select * from delivery where de_OrderID = " + orderID + ";";
		Statement stmt2 = conn.createStatement();
		ResultSet rset2 = stmt2.executeQuery(query2);

		while(rset2.next()){
			String addr = rset2.getString(2);
			System.out.println(print + " | Delivered to: " + addr);
		}
		conn.close();
	}

	public static Double getBaseCost(String size, String type) throws SQLException, IOException{
		connect_to_db();
		String query = "Select * from BASEPRICE where pr_Size = '" + size + "' and pr_Crust = '" + type + "';";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		Double cost = 0.0;

		while(rset.next())
		{
			cost = rset.getDouble(4);
		}

		conn.close();
		return cost;
	}

	public static Double getBasePrice(String size, String type) throws SQLException, IOException{
		connect_to_db();
		String query = "Select * from BASEPRICE where pr_Size = '" + size + "' and pr_Crust = '" + type + "';";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		Double price = 0.0;

		while(rset.next())
		{
			price = rset.getDouble(3);
		}

		conn.close();
		return price;
	}
}
