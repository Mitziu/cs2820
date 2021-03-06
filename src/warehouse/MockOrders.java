package warehouse;

import java.util.*;

public class MockOrders implements Orders, Tickable, Picker {
	
  private Inventory I;
  private RobotScheduler R;
  private LinkedList<Order> orderqueue;
  private SimRandom randomsource;
  
  /**
   * @author Ted Herman
   * @param Inventory component, needed to create sensible
   * new Order to add to queue of work to do (for testing,
   * it just creates a few initial orders)
   * @param rand is a SimRandom, for predictable randomness
   */
  public MockOrders(Inventory I, 
		  RobotScheduler R, SimRandom rand) {
	this.I = I; // so we can later call upon Inventory methods
	this.R = R; // so that later, we can call RobotScheduler
	randomsource = rand;
	orderqueue = new LinkedList<Order>();
	for (int i=0;i<3;i++) {
	  orderqueue.addLast(getRandomOrder());
	  }
    }
  
  /**
   * @author Ted Herman
   * 
   * This tick method would be where Orders does the 
   * real work (see design README)
   * 
   */
  public void tick(int count) {
	/**
	 * What tick should do (vague description)
	 * 1. if there is no Order in orderqueue, then
	 *    maybe it's time to generate a random new
	 *    Order and put it in the queue? Another idea
	 *    would be to delay putting a new Order in the 
	 *    queue (postpone that to a later tick)
	 * 2. if no Order being worked on, and
	 *    there is an Order in orderqueue, ask Belt 
	 *    if a new Bin is available - a "no" from the 
	 *    Belt would mean Orders has to wait - so ignore
	 *    this tick. 
	 * 3. if Belt says a new Bin can be started, and 
	 *    there's an Order in the orderqueue, time to 
	 *    start working (go to next step in this same tick).
	 *    Probably some variables/fields will be changed
	 *    to indicate that an Order is in progress, being
	 *    worked on by the Picker.
	 * 4. if an Order is being worked on (need additional
	 *    variable/fields to know this), then check if 
	 *    it has any Items that need fetching.
	 * 5. if an Item needs fetching, do pretty much what
	 *    is done in TestRobotScheduler, but when notify()
	 *    of the Picker interface is called (see below), then
	 *    get take the desired OrderItem from the Shelf that
	 *    the Robot brought, mark that OrderItem as being
	 *    in the Bin.
	 * 6. if all OrderItems are filled from the current 
	 *    Order being worked on, then tell the Belt that 
	 *    the Bin at the Picker is ready to go. 
	 *    
	 * Special Note on Step 5. In MockRobotScheduler, it's 
	 * assumed that whatever an Order needs, that OrderItem
	 * will be on some Shelf. A more realistic idea is to 
	 * attempt to find the OrderItem on a Shelf, but if it 
	 * cannot be found, tell Inventory (so Inventory would need
	 * to have a new method for that). Then Orders is stuck, 
	 * waiting for the desired OrderItem to be present, checking
	 * that in each tick(). Eventually, after Inventory has done
	 * its job, Orders will find a Shelf with the needed OrderItem
	 * and continue.
	 */
    }

  /**
   * @author Ted Herman
   * Picker event notify(robot), not finished.
   */
  public void notify(Robot r, Shelf s) { 
	// should be code here to remove desired Item OrderItem from Shelf s
	// (Inventory has a method to do that)
	// mark that Item as being checked off in the Order
	// and if this is the last Item needed, tell Belt 
	// that a Bin is done.
	R.returnShelf(r);  // tell Robot to return Shelf back to its home
    };
  
  /**
   * @author Ted Herman
   * creates a random Order
   */
  public Order getRandomOrder() {
	String addr = new Address(randomsource).randomAddress();
	OrderItem[] orderitems = new OrderItem[1+randomsource.nextInt(2)];
	for (int i=0;i<orderitems.length;i++) {
	  orderitems[i] = new OrderItem(I.randomItem());
	  }
	return new Order(addr,orderitems);
    }
  }

/**
 * 
 * @author Ted Herman
 * A local class just to supply addresses
 * for orders in the Orders component
 *
 */
class Address {
	
  SimRandom SR;
  
  /**
   * @author Ted Herman
   * @param SR is SimRandom object, so that all the random
   * choices by methods of Address will be predictably random
   */
  public Address(SimRandom SR) {
	this.SR = SR;
    }

  /**
   * @author Ted Herman
   * @param R is a SimRandom for predictability in randomness
   * @return String containing a random address for an order
   */
  public String randomAddress() {
	String FirstName = randomFirstName();
	String LastName = randomLastName();
	String StreetNumber = new Integer(randomStreetNumber()).toString();
	String StreetName = randomStreetName();
	String City = randomCity();
	String State = randomState();
	String ZipCode = randomZip();
	String Address = FirstName + " " +
	  LastName + "\n" + StreetNumber + " " +
	  StreetName + "\n" + City + " " + State + ZipCode;
	return Address;
    }
  /**
   * @author Ted Herman
   * @return a string containing a random street name
   */
  private String randomStreetName() {
	final String[] baseNames = {"Park Street",
			"Main Street", "Washington Boulevard",
			"Third Street", "Park Road",
			"Maple Street", "Hill Road"};
	return baseNames[SR.nextInt(baseNames.length)];
    }
  /**
   * @author Ted Herman
   * @return an integer in the range [1,999] for street address
   */
  private int randomStreetNumber () {
	return 1+SR.nextInt(998);
    }
  /**
   * @author Ted Herman
   * @return a random first name for an address
   */
  private String randomFirstName() {
	final String[] baseFirstNames = {"Dakota", "Emma",
			"Julian", "Nigella", "Will", "Asti", "Lee",
			"Pat", "Mavis", "Jerome", "Lilly", "Tess"};
	return baseFirstNames[SR.nextInt(baseFirstNames.length)];
	}
  /**
   * @author Ted Herman
   * @return a random last name for an address
   */
  private String randomLastName() {
	final String[] baseLastNames = {"Parker","Mason",
				"Smith","Wright","Jefferson","Iqbal",
				"Owens","Lafleur","Metselen","Vinceroy",
				"Saville","Troitski","Andrews"};
	return baseLastNames[SR.nextInt(baseLastNames.length)];
    }
  /**
   * @author Ted Herman
   * @return a random city name
   */
  private String randomState() {
	final String[] baseState = {"IA","NE","MO",
				"IL","KS","MN","SD","AR","OK","TX"};
	return baseState[SR.nextInt(baseState.length)];
    }
  /**
   * @author Ted Herman
   * @return a random state code (two letters)
   */
  private String randomCity() {
	final String[] baseCity = {"Springfield","Clinton",
				"Madison","Franklin","Chester","Marion",
				"Greenville","Salem","Anytown","Hope"};
	return baseCity[SR.nextInt(baseCity.length)];
    }
  /**
   * @author Ted Herman
   * @return a random state code (two letters)
   */
  private String randomZip() {
    String ZipCode = "";
    for (int i=0; i<6; i++) 
      ZipCode += "0123456789".charAt(SR.nextInt(10));
    return ZipCode;
  }
}
