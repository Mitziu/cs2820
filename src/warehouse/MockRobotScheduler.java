package warehouse;

public class MockRobotScheduler implements RobotScheduler, Tickable {
	
  Floor F;
  Robot[] robots; // someday could be two or more robots
	
  /**
   * @author Ted Herman
   * @param Floor object.
   * Floor is needed to find location of charger, etc.
   */
  public MockRobotScheduler(Floor F) {
	this.F = F;
	robots = new Robot[1];
	robots[0] = new Robot(F.getCharger()); // initially at the charger
	Cell t = F.getCell(F.getCharger());    // occupy that cell
	t.setContents(robots[0]);
    }
	
  /**
   * @author Ted Herman
   * 
   * The tick() method is where the real work would be done,
   * see the design README document	
   */
  public void tick(int count) { 
	// Look to see if any Robot should move
	for (Robot e: robots) {
	   if (e.path != null) moveRobot(e);
	   }
    };	
  /**
   * @param r is a Robot to move along its path, and if
   * the Robot reaches the end of the path, then decide
   * on where it should go next (if anywhere).
   */
  private void moveRobot(Robot r) { 
    // some initial assertions say what is the expected precondition
    assert r.path != null;
    assert r.path.size() > 0;
    Cell tempcell = F.getCell(r.location);
    assert tempcell.getContents() == r || tempcell.getShadow() == r;
    tempcell.setContents(null); // Robot will no longer be in this cell
    tempcell.setShadow(null);
	if (r.path.size()>1) {
	   r.location = r.path.get(0);  // move to first point in path
	   r.path.remove(0);  // remove first point in path
	   tempcell = F.getCell(r.location);
	   tempcell.setContents(r);  // robot has moved to new place
	   return;
	   }
	// when path has one Point, we arrive in this tick to target
	Point goal = r.path.get(0);
	r.location = goal;
	tempcell = F.getCell(goal);
	// on arrival to Shelf, validate there is a Shelf there
	if (r.state == Robot.pickershelfbound 
			|| r.state == Robot.dockshelfbound) {
	   assert tempcell.getContents() instanceof Shelf;
	   assert r.shelf == tempcell.getContents();
	   tempcell.setShadow(r);
	   }
	// in any other case, cell should empty
	else { 
	  assert tempcell.getContents() == null; 
	  tempcell.setContents(r);
	  }
	// System.out.println("robot movesto "+goal+" state "+r.state);
	r.path = null;
	switch (r.state) { 
	// these are cases of reaching goal in path
	case Robot.pickershelfbound:
	   r.shelf.pickup();  // robot claims this shelf
	   r.path = F.getPath(r.location,F.getPicker());
	   r.state = Robot.pickerbound;  // now heading to Picker
	   break;
	case Robot.pickerbound:
	   r.state = Robot.atpicker;
	   r.picker.notify(r,r.shelf);
	   break;
	case Robot.afterdockshelfbound:
	case Robot.afterpickershelfbound:
	   r.shelf.putdown();  // Shelf is back home
	   tempcell = F.getCell(r.location);  // change status of this cell
	   tempcell.setContents(r.shelf);
	   tempcell.setShadow(r);
	   r.shelf = null;
	   r.path = F.getPath(goal,F.getCharger());
	   r.state = Robot.chargerbound;
	   break;
	case Robot.dockshelfbound:
	   r.shelf.pickup();  // robot claims this shelf
	   r.path = F.getPath(r.location,F.getReceivingDock());
	   r.state = Robot.dockbound;  // now heading to Dock
	   break;
	case Robot.dockbound:
	   r.state = Robot.atdock;
	   r.dock.notify(r,r.shelf);
	   break;
	case Robot.atpicker:
	case Robot.atdock:
	   break;   // just wait around in these cases
	case Robot.chargerbound:
	   r.state = Robot.idle;
	   break;
	   }
	return;
    }
  
  /**
   * @param s is a Shelf to fetch and bring to the picker
   * location (which the Floor knows)
   * @param p is a Picker interface, implemented by Orders,
   * which invoked this method - p is essentially a 
   * "callback" object to notify Orders at some later tick()
   */
  public void requestShelf(Shelf s, Picker p) { 
	Point target = s.home; // where Shelf sits
	Robot robot = findRobot(); // get some idle robot
	robot.path = F.getPath(robot.location,target);
	robot.state = Robot.pickershelfbound;
	robot.picker = p;
	robot.shelf = s;  // don't have it yet, but will get it
    };
  /**
   * @param s is a Shelf to fetch and bring to the receiving
   * dock location (which the Floor knows)
   * @param d is a Dock interface, implemented by Inventory,
   * which invoked this method. The d parameter is thus a
   * "callback" object to notify Inventory at some later tick()
   */
  public void requestShelf(Shelf s, Dock d) { };
  /**
   * Command to return a robot carrying a shelf back to 
   * a ShelfArea on the Floor and put it down. Then the 
   * @param r is a Robot which is carrying a Shelf
   */
  public void returnShelf(Robot r) { 
	assert r.state == Robot.atpicker;
	r.path = F.getPath(r.location,r.shelf.home);
	r.state = Robot.afterpickershelfbound;
    }
  /**
   * find an available Robot (which is not in use)
   */
  private Robot findRobot() {
	// currently there is only one robot, this is trivial
	Robot r = robots[0];
	assert r.state == Robot.idle;
	assert r.shelf == null;
	return r;
    }
  }
