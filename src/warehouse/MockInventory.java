package warehouse;
import java.util.*;

/**
 * 
 * @author Ted Herman
 *
 */

public class MockInventory implements Inventory, Tickable, Dock {
  List<Item> stock;  // all the in-stock items of warehouse
  SimRandom randomsource;  // supply of random numbers
  Floor floor;
  /**
   * @author Ted Herman
   * @param floor 
   * @param rand is a SimRandom for predictable randomness
   * Constructor of Mock Inventory, puts lots of catalog items
   * on random shelves in the warehouse
   */
  public MockInventory(Floor floor, SimRandom rand)  {
	this.floor = floor;
	stock = new ArrayList<Item>();
	randomsource = rand;
	for (int i=0;i<CatItem.catalog.length;i++) {
	  int times = 1+randomsource.nextInt(4);
	  for (int j=0;j<times;j++) {
		// up to five instances of this particular item
		Item n = new Item(CatItem.catalog[i].id,CatItem.catalog[i].description);
		Point p = floor.randomInShelfArea();
		Cell c = floor.getCell(p);
		n.setPlace((Shelf)c.getContents());
		stock.add(n);
	    }
	  }
    }
  
  /**
   * the tick() does nothing in this Mock class,
   * but in a full coding, it would check whether the stock
   * seems too low, and whether a truck should be asked to 
   * arrive at the receiving dock, bringing more items - therefore
   * there might be certain tick() calls which means that a truck
   * has arrived, then tick() has some work to do. When a truck 
   * arrives, the procedure should be somewhat like how Orders asks
   * a Robot to bring a shelf (but to the dock, not to the Picker),
   * and then put items onto the shelf, after which we tell the 
   * Robot to take the shelf back to its home. Example of code which
   * simulations what Orders might do is found in TestRobotScheduler.
   */
  public void tick(int count) {
	// nothing here right now
    }
  
  /**
   * the Dock interface notify() method is called by 
   * RobotScheduler some ticks after this code has 
   * requested that a Robot bring a shelf to
   * the dock; it's just like what the Picker interface is for,
   * but for the receiving dock instead of the Picker location. 
   */
  public void notify(Robot r, Shelf s) {
	// currently there is no code  
    }
  
  /**
   * @author Ted Herman
   * return a random Item from the catalog, to build a mock order
   */
  public Item randomItem() {
	int k = randomsource.nextInt(CatItem.catalog.length);
	return new Item(CatItem.catalog[k].id,CatItem.catalog[k].description);
	}
  /**
   * @param Shelf 
   * @return array of Item that are on this shelf
   */
  public Item[] onShelf(Shelf s) {
	List<Item> scan = new ArrayList<Item>();
	Item[] returnpattern = new Item[0];
	for (Item e: stock) {
	  if (!(e.getPlace().home).equals(s.home)) continue;
	  scan.add(e);
	  }
	return scan.toArray(returnpattern);
    }
  /**
   * duplicate for polymorphism, items by point containing shelf
   */
  public Item[] onShelf(Point p) {
	Cell c = floor.getCell(p);
	if (c.getContents() == null) return null;
	if (!(c.getContents() instanceof Shelf)) return null;
	return this.onShelf((Shelf)c.getContents());
    }
  public int stockCount(int x) { 
	int r = 0;
	for (Item e: stock) {
	  if (e.id == x) r++;
      }
	return r;
    }
  public int stockCount(Item i) {
	return this.stockCount(i.id);
    }
  /**
   * @author Ted Herman
   * @return null or a Shelf object
   * Just return the first occurrence of this item's Shelf
   * WARNING BUGGY - could fail if looking for multiple units of
   * the same item (they could be on different shelves)
   */
  public Shelf findItem(Item i) {
	for (Item e: stock) {
	  if (!e.place.onFloor()) continue;  // ignore moving shelves
      if (e.equals(i)) return e.getPlace();  
	  }
	return null;
    }
  /**
   * This method is for Orders (the Picker) to remove an item
   * from a Shelf, thus removing it from available inventory
   * @param Item a is the item to be removed from
   * @param Shelf s (only one occurrence of the Item is removed)
   * @return the Item removed
   */
  public Item removeItem(Item a, Shelf s) {
	for (Item e: stock) {
      assert !(e.place.onFloor());  // only remove from carried Shelf
      if (!e.equals(a)) continue;   // look for this item only
      if (!e.place.home.equals(s.home)) continue; // only Shelf s
      stock.remove(e);
      e.setPlace(null);  // not on Shelf anymore
      return e;
	  }
	return null;  // not supposed to happen
    }
  }

/**
 * 
 * @author Ted Herman
 *
 * A local class to be the catalog of available
 * items that Inventory can use: CatItem.catalog
 * is an array of CatItem objects, each with an
 * id member (int) and description (String)
 *
 */
class CatItem {
  int id;
  String description;
  private CatItem(int a, String b) {
    id = a; description = b;
    }
  static final CatItem[] catalog = {
    new CatItem(547840,"1-Cup Coffee and Espresso Maker"),
    new CatItem(150644,"11lb Kitchen Scale"),
    new CatItem(424962,"12V Power Adapter for Cars"),
    new CatItem(490499,"26-Function Bike Tool"),
    new CatItem(573447,"3-Step Stool"),
    new CatItem(533512,"32 GB SD Card"),
    new CatItem(439305,"34oz Insulated Themos"),
    new CatItem(106510,"4-Quart Tupperware"),
    new CatItem(628751,"4-Tier Cooling Rack"),
    new CatItem(379920,"40 Quart Stock Pot"),
    new CatItem(258065,"5-Bike Rack"),
    new CatItem(372758,"5-Port Networking Switch"),
    new CatItem(136217,"61oz Thermos"),
    new CatItem(898073,"A-Male to Mini-B USB Cable"),
    new CatItem(32794,"A-to-B USB Cable"),
    new CatItem(346139,"AA Battery Charger"),
    new CatItem(305186,"AA Rechargeable Batteries"),
    new CatItem(236580,"Adjustable Basketball Hoop"),
    new CatItem(707622,"Adjustable Dumbbells"),
    new CatItem(896039,"Adjustable Gym Bench"),
    new CatItem(973867,"Air Freshener"),
    new CatItem(501806,"Airtight Container"),
    new CatItem(691247,"Anti-Chafe Balm"),
    new CatItem(538672,"Apple Peeler Contraption"),
    new CatItem(319537,"Apron with Pockets"),
    new CatItem(267316,"Audio Cable"),
    new CatItem(338998,"Audio Contact Cleaner Spray"),
    new CatItem(52279,"Audio-Technica Headphones"),
    new CatItem(316191,"Auto Code Reader"),
    new CatItem(152928,"Backpack"),
    new CatItem(324674,"Backpack Full of Water"),
    new CatItem(460868,"Baguette Pan"),
    new CatItem(76872,"Balance Board"),
    new CatItem(371787,"Basketball"),
    new CatItem(84044,"Battery-Powered Lantern"),
    new CatItem(385102,"Beer Mug Set"),
    new CatItem(823226,"Bidet"),
    new CatItem(595025,"Bike Air Horn"),
    new CatItem(206862,"Bike Bungie Net"),
    new CatItem(229467,"Bike Mirror"),
    new CatItem(997474,"Bike Seat"),
    new CatItem(542819,"Bike Stand"),
    new CatItem(970854,"Bike Tail Light"),
    new CatItem(452712,"Bike Trailer"),
    new CatItem(130748,"Blender Bottle"),
    new CatItem(497661,"Bolt Action C02 Pistol"),
    new CatItem(71788,"Boning Knife"),
    new CatItem(19572,"Book Stand"),
    new CatItem(170103,"Bookshelf Speakers"),
    new CatItem(581823,"Bottle Cage"),
    new CatItem(139797,"Bottle Opener"),
    new CatItem(34944,"Bottle and Dish Brush"),
    new CatItem(951434,"Bread Knife"),
    new CatItem(937101,"Brew-In-Mug Device"),
    new CatItem(527503,"Brownie Pan"),
    new CatItem(715104,"Bundt Pan"),
    new CatItem(929939,"Burger Press"),
    new CatItem(29846,"Butter Crock"),
    new CatItem(39063,"Cake Pan"),
    new CatItem(803993,"Can Crusher"),
    new CatItem(931995,"Can Opener"),
    new CatItem(955551,"Cappuccino and Latte Set"),
    new CatItem(815265,"Car Battery Monitor"),
    new CatItem(515235,"Carpet Cleaning Machine"),
    new CatItem(152741,"Carpet Stain Remover"),
    new CatItem(945950,"Cast Iron Pot"),
    new CatItem(15729,"Cast-Iron Dutch Oven"),
    new CatItem(420010,"Ceramic Cooktop Cleaning Kit"),
    new CatItem(143531,"Chef Cleaver Knife"),
    new CatItem(893607,"Chef's Knife"),
    new CatItem(244911,"Cherry  Olive Pitter"),
    new CatItem(877744,"Circuit Tester"),
    new CatItem(954547,"Cleaning Pads"),
    new CatItem(409787,"Collapsible Strainer"),
    new CatItem(281789,"Cookie Dough Scooper"),
    new CatItem(796862,"Cork Yoga Block"),
    new CatItem(831679,"Corkscrew"),
    new CatItem(88257,"Corn Holders"),
    new CatItem(234690,"Cupcake Maker"),
    new CatItem(777411,"Cupcake Storage Container"),
    new CatItem(277501,"Cute Egg Mold"),
    new CatItem(84513,"Cutting Board"),
    new CatItem(595145,"Cycling Helmet Mirror"),
    new CatItem(884938,"Cycling Pedals"),
    new CatItem(878795,"Cycling Shorts"),
    new CatItem(192719,"DVD+R Discs (8.5 GB)"),
    new CatItem(520544,"Danish Dough Whisk"),
    new CatItem(578771,"Digital Bathroom Scale"),
    new CatItem(594980,"Digital Body Fat Scale"),
    new CatItem(748763,"Digital Kitchen Scale"),
    new CatItem(107741,"Digital Radio"),
    new CatItem(20702,"Digital Shower Radio"),
    new CatItem(429280,"Disc"),
    new CatItem(997806,"Donut Pan"),
    new CatItem(512232,"Drifting Machine"),
    new CatItem(796905,"Drinking Glasses"),
    new CatItem(91687,"Dry Bag"),
    new CatItem(259450,"Dryer vent cleaning system"),
    new CatItem(745712,"Drying Rack"),
    new CatItem(891433,"Dumbbell Rack"),
    new CatItem(374709,"Egg / Bacon Microwave Cooker"),
    new CatItem(452859,"Egg Beater"),
    new CatItem(148733,"Electric Duster Vacuum"),
    new CatItem(390402,"Electric Fondue Maker"),
    new CatItem(107781,"Electric Food Slicer"),
    new CatItem(113927,"Electric Griddle"),
    new CatItem(590089,"Electric Meat Grinder"),
    new CatItem(639242,"Electric Rotisserie"),
    new CatItem(932108,"Electric Salad Slicer"),
    new CatItem(582932,"Electric Scooter"),
    new CatItem(241881,"Electric Smoker"),
    new CatItem(828697,"Electric Wok"),
    new CatItem(920858,"Elevation Training Gear"),
    new CatItem(25883,"Energy Chews"),
    new CatItem(24860,"Energy Gel"),
    new CatItem(835870,"Espresso Spoons"),
    new CatItem(555295,"Espresso Tamper"),
    new CatItem(621860,"Ethernet Cable"),
    new CatItem(21796,"Exercise Bike"),
    new CatItem(931117,"Expandable Closet Organizer"),
    new CatItem(279854,"Extension Cord"),
    new CatItem(320815,"F-Pin Audio/Video Cable"),
    new CatItem(668976,"Facial Tissues"),
    new CatItem(916785,"Fake TV Burglar Deterrent"),
    new CatItem(595252,"Fast, Portable Water Boiler"),
    new CatItem(480567,"Fat Separator"),
    new CatItem(99641,"Faucet Extender for Kids"),
    new CatItem(978234,"Fire Starter"),
    new CatItem(876529,"First Aid Kit"),
    new CatItem(641348,"Fish Turner"),
    new CatItem(375111,"Fishing Reel"),
    new CatItem(738632,"Floor Mat"),
    new CatItem(253257,"Floorstanding Speaker"),
    new CatItem(155989,"Foam Roller"),
    new CatItem(977238,"Foldable Laundry Bin"),
    new CatItem(144728,"Folding Laundry Basket"),
    new CatItem(840026,"Food Bag Sealer Clips"),
    new CatItem(97627,"Food Chopper / Pastry Scraper"),
    new CatItem(385373,"Food Coloring Set"),
    new CatItem(374235,"Food Dehydrator and Jerky Maker"),
    new CatItem(890208,"Food Proofer"),
    new CatItem(469345,"Food Storage Set"),
    new CatItem(40291,"Food Thermometer"),
    new CatItem(65900,"Forehead Flashlight"),
    new CatItem(96625,"French Press"),
    new CatItem(6515,"Frisbee Alternative"),
    new CatItem(789877,"Frozen Concoction Maker"),
    new CatItem(465271,"Fruit Corer"),
    new CatItem(827768,"Fruit and Vegetable Wash"),
    new CatItem(222585,"Frying Pan Set"),
    new CatItem(703866,"Garage Door Remote"),
    new CatItem(231805,"Garlic Peeler"),
    new CatItem(281152,"Garlic Press"),
    new CatItem(360957,"Glass Bowls w/ Lids"),
    new CatItem(90501,"Glass Cleaner"),
    new CatItem(615817,"Golf Bag"),
    new CatItem(230105,"Golf Balls"),
    new CatItem(128407,"Golf Push Cart"),
    new CatItem(519573,"Grabber Thing"),
    new CatItem(595351,"Grain Mill"),
    new CatItem(952729,"Granton Edge Slicing Knife"),
    new CatItem(246172,"Grater/Zester"),
    new CatItem(79268,"Griddle Pan"),
    new CatItem(224678,"Grocery Bag Holder"),
    new CatItem(582055,"HD 3D Plasma TV"),
    new CatItem(353705,"HD Camcorder"),
    new CatItem(265642,"HDMI Cable"),
    new CatItem(702891,"HDMI to DVI Cable"),
    new CatItem(297389,"Hand Exerciser"),
    new CatItem(33198,"Handheld Blender"),
    new CatItem(538031,"Hanger Holder"),
    new CatItem(848304,"Hanging Laundry Bag"),
    new CatItem(125361,"Hanging Pot Rack"),
    new CatItem(545211,"Heat-Resistant Utensil Set"),
    new CatItem(690622,"Heated Mattress Pad"),
    new CatItem(160200,"Hidden Book Shelf"),
    new CatItem(143817,"Home Theater Projector"),
    new CatItem(147914,"Home Theater Speakers"),
    new CatItem(370123,"Huge Portable Hammock"),
    new CatItem(173986,"Ice Cream Scooper"),
    new CatItem(444323,"Ice Cube Tray"),
    new CatItem(774614,"In-Drawer Knife Tray"),
    new CatItem(959869,"Indoor Bike Trainer"),
    new CatItem(650713,"Inflatable Boat"),
    new CatItem(89562,"Inflatable Kayak"),
    new CatItem(765403,"Insulated Water Bottle"),
    new CatItem(266717,"Iron"),
    new CatItem(878047,"Ironing Board Cover"),
    new CatItem(734630,"Jar Opener"),
    new CatItem(955880,"Jar Sealer"),
    new CatItem(404969,"Juicer Thing"),
    new CatItem(82413,"Juicing Machine"),
    new CatItem(413166,"Kettle"),
    new CatItem(598269,"Kid's Trike"),
    new CatItem(503284,"Kindle"),
    new CatItem(10741,"Knife Block"),
    new CatItem(281078,"Knife Sharpener"),
    new CatItem(216490,"LED Flashlight"),
    new CatItem(714837,"LED Keychain Light"),
    new CatItem(167426,"Ladybug Night Light"),
    new CatItem(726532,"Laminating Machine Combo Pack"),
    new CatItem(578053,"Lapdesk Laptop Speaker"),
    new CatItem(394760,"Laptop Stand"),
    new CatItem(487950,"Lasagna Trio Pan"),
    new CatItem(432557,"Laundry Detergent"),
    new CatItem(50706,"Laundry Soda"),
    new CatItem(366100,"Laundry Sorter Cart"),
    new CatItem(896533,"Legit Dartboard"),
    new CatItem(927254,"Letter Opener"),
    new CatItem(781913,"Lithium Batteries"),
    new CatItem(887215,"Loaf Ban"),
    new CatItem(155909,"Longboard Skateboard"),
    new CatItem(985632,"Louisville Slugger Baseball Bat"),
    new CatItem(88609,"MacGyver Kitchen Machine"),
    new CatItem(958833,"Machete Knife"),
    new CatItem(254503,"Madeleine Pan"),
    new CatItem(179752,"Magnetic Clips"),
    new CatItem(128553,"Magnetic Door Blinds"),
    new CatItem(467502,"Magnetic Knife Holder"),
    new CatItem(172591,"Magnetic Measuring Spoons"),
    new CatItem(723504,"Magnifying Glass"),
    new CatItem(987711,"Mandoline Slicer"),
    new CatItem(20032,"Massage Stick"),
    new CatItem(856643,"Measuring Cups"),
    new CatItem(168516,"Meat Tenderizer"),
    new CatItem(854597,"Mega Plunger"),
    new CatItem(968263,"Microwavable Steamer"),
    new CatItem(726600,"Microwave Pasta Maker"),
    new CatItem(344332,"Milk Frother"),
    new CatItem(883276,"Milk Frothing Pitcher"),
    new CatItem(310865,"Mini Breadmaker"),
    new CatItem(843350,"Mini Exercise Bike"),
    new CatItem(465497,"Mini MacGyver"),
    new CatItem(737882,"Mirror"),
    new CatItem(241243,"Mixer"),
    new CatItem(133724,"Mixing Bowl Set"),
    new CatItem(357981,"Mortar and Pestle"),
    new CatItem(574051,"Mouse Trap"),
    new CatItem(553574,"Mug Holder"),
    new CatItem(24167,"NFL Official Size Football"),
    new CatItem(838250,"Near  Far range Binoculars"),
    new CatItem(239213,"Neck/Face Mask"),
    new CatItem(259697,"Nonstick Baking Liner"),
    new CatItem(339573,"Nonstick Frying Pan"),
    new CatItem(225910,"Nonstick Skilet"),
    new CatItem(573053,"Oil Pourer"),
    new CatItem(273023,"Outdoor Party Game"),
    new CatItem(968470,"Oven Glove"),
    new CatItem(857734,"Over-the-door Jewelry Rack"),
    new CatItem(359049,"Pain Reliever"),
    new CatItem(743050,"Paintball Feeder"),
    new CatItem(332428,"Paintball Set"),
    new CatItem(727693,"Paper Towel Holder"),
    new CatItem(405134,"Paring Knife"),
    new CatItem(921231,"Pasta Machine"),
    new CatItem(31379,"Pedal-less Bike"),
    new CatItem(339604,"Pedometer"),
    new CatItem(626330,"Peeler"),
    new CatItem(515355,"Pepper Grinder"),
    new CatItem(822949,"Personal Fan"),
    new CatItem(706215,"Pet Food Container"),
    new CatItem(18204,"Phone  GPS Mount"),
    new CatItem(418759,"Pie Crust Maker"),
    new CatItem(740014,"Pineapple Slicer and De-Corer"),
    new CatItem(268979,"Ping Pong Paddle"),
    new CatItem(957109,"Pitching Machine"),
    new CatItem(206518,"Pizza Cutter"),
    new CatItem(885876,"Pizza Oven Thing"),
    new CatItem(26911,"Pizza Pan"),
    new CatItem(758460,"Pizza and Baking Stone"),
    new CatItem(816587,"Pizza and Dough Peel"),
    new CatItem(44742,"Pizzelle Baker"),
    new CatItem(615120,"Pocket Rescusitator"),
    new CatItem(736978,"Pocket Water Microfilter"),
    new CatItem(923348,"Popsicle Maker"),
    new CatItem(591574,"Portable Charcoal Grill"),
    new CatItem(277208,"Portable Folding Chair"),
    new CatItem(405209,"Portable Gas Grill"),
    new CatItem(448222,"Portable Stove"),
    new CatItem(193253,"Potato Ricer"),
    new CatItem(26347,"Power Strip"),
    new CatItem(714476,"Pressure Cooker"),
    new CatItem(585856,"Pressure Cooker  Canner"),
    new CatItem(719603,"Printer / Scanner / Copier / Fax"),
    new CatItem(516855,"Programmable Lego Robot"),
    new CatItem(228091,"Programmable Switch"),
    new CatItem(666364,"Propulsion Scooter"),
    new CatItem(793345,"Pulse Oximeter"),
    new CatItem(902919,"Punching Bag"),
    new CatItem(239370,"Queen Bed Frame and Box Spring"),
    new CatItem(63959,"RCA to RCA Subwoofer Cable"),
    new CatItem(102160,"Razor Scooter"),
    new CatItem(487185,"Rechargable Bike Headlight"),
    new CatItem(311059,"Reusable Grocery Bags"),
    new CatItem(757526,"Reusable Produce Bags"),
    new CatItem(405977,"Reusable Snack Bag"),
    new CatItem(605467,"Rice Cooker"),
    new CatItem(220955,"Roller Cart"),
    new CatItem(607004,"Rolling Pin"),
    new CatItem(492317,"Rolling Step Stool"),
    new CatItem(791326,"Rotating Cake Stand"),
    new CatItem(377631,"Rotating Turntable"),
    new CatItem(336572,"Rowing Machine"),
    new CatItem(261924,"Rust  Stain Remover"),
    new CatItem(128805,"Salad Spinner"),
    new CatItem(924454,"Saucepot and Steamer"),
    new CatItem(688945,"Sausage Stuffer"),
    new CatItem(197417,"Scissors"),
    new CatItem(792364,"Scooter"),
    new CatItem(325426,"Scotch Tape"),
    new CatItem(163632,"Screen Cleaning Kit"),
    new CatItem(596785,"Sheet Gripper"),
    new CatItem(962354,"Shoe Dryer"),
    new CatItem(378676,"Shoe Rack"),
    new CatItem(63285,"Shower Curtain Rings"),
    new CatItem(596792,"Showerhead"),
    new CatItem(227133,"Silverware Tray"),
    new CatItem(398144,"Simple Projector"),
    new CatItem(735053,"Skateboard"),
    new CatItem(64336,"Skateboard Ramp Kit"),
    new CatItem(654136,"Sleeping Bag"),
    new CatItem(614226,"Sling-Style Bag"),
    new CatItem(717652,"Slip-On Spikes"),
    new CatItem(224910,"Slip-free Yoga Towel"),
    new CatItem(4950,"Smoke Detector"),
    new CatItem(214329,"Snorkel Equipment"),
    new CatItem(768860,"Snow Cone Maker"),
    new CatItem(127837,"Solo Raft"),
    new CatItem(190304,"Soup and Drink Thermos"),
    new CatItem(749409,"Soy and Almond Milk Maker"),
    new CatItem(68450,"Spaetzle Maker"),
    new CatItem(738150,"Speaker Wire"),
    new CatItem(46954,"Speedminton Set"),
    new CatItem(28523,"Spice Rack"),
    new CatItem(601964,"Spinning Organizer"),
    new CatItem(784239,"Splitting Axe"),
    new CatItem(905076,"Spring Powered BB Gun"),
    new CatItem(641910,"Stain  Odor Remover"),
    new CatItem(313152,"Stand Mixer Bowl"),
    new CatItem(401558,"Steak Knives"),
    new CatItem(112791,"Storage Bin"),
    new CatItem(922508,"Strainer"),
    new CatItem(240526,"Strawberry Stem Remover"),
    new CatItem(722925,"Super-Soft, Absorbant Towel"),
    new CatItem(165777,"Sushi Maker"),
    new CatItem(286612,"Sushi Rice Press"),
    new CatItem(518590,"Swiffer Sweeping Cloths"),
    new CatItem(933188,"Swim Goggles"),
    new CatItem(957340,"Swively Two-Wheeled Skateboard"),
    new CatItem(919455,"TV Stand"),
    new CatItem(209826,"TV mount"),
    new CatItem(845979,"Table Tennis Set"),
    new CatItem(981927,"Tactile Keyboard"),
    new CatItem(642843,"Teapot"),
    new CatItem(938928,"Telescope Sight"),
    new CatItem(253874,"Thermos Travel Mug"),
    new CatItem(80819,"Tiny Portable Speakers"),
    new CatItem(163764,"Toilet bowl ring remover"),
    new CatItem(936885,"Tomato Slicer Knife"),
    new CatItem(364472,"Tongs"),
    new CatItem(874425,"Toothbrush Holder"),
    new CatItem(828346,"Tortilla Shell Pans"),
    new CatItem(251039,"Toy Hammock"),
    new CatItem(255937,"Trash Can"),
    new CatItem(411422,"USB Wi-Fi Adapter"),
    new CatItem(48067,"Utensil Holder"),
    new CatItem(847815,"Utility Cord"),
    new CatItem(19401,"Vacuum"),
    new CatItem(45002,"Vegetable Brush"),
    new CatItem(139219,"Velcro Cable Ties"),
    new CatItem(711636,"Vibram Running Shoe"),
    new CatItem(791510,"VoIP Phone Adapter"),
    new CatItem(692184,"Volleyball"),
    new CatItem(613341,"Waffle Maker"),
    new CatItem(777182,"Waist Trimmer"),
    new CatItem(13279,"Wall-Mounted Spice Rack"),
    new CatItem(657376,"Waterproof iPod Shuffle"),
    new CatItem(828386,"Weighted Hula Hoop"),
    new CatItem(942054,"Whisk"),
    new CatItem(841499,"Whiskey Glass"),
    new CatItem(243691,"Wine Aerator"),
    new CatItem(879597,"Wine Saver"),
    new CatItem(522223,"Wireless Camera"),
    new CatItem(400368,"Wireless Mouse"),
    new CatItem(758769,"Wok"),
    new CatItem(175094,"Yoga Mat"),
    new CatItem(920567,"iPad Back Cover"),
    new CatItem(305144,"iPad Blender"),
    new CatItem(743421,"iPhone/iPod Stereo"),
    };  
  }
