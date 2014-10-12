package EarthSim;

public class GridCell
{
	private GridCell west;
    private GridCell east;
    private GridCell north;
    private GridCell south;
    double temp = 0.00;
    double prevTemp = 0.00;
    private  double latitude, longtitude;
    private double lv,lb, lt, h, perimeter, area;
    private double proportion;
    private int xCoordinate, yCoordinate;
    
    
    /** Add constructor getters and setters **/
    
    public  GridCell()
    {
               
          
    }


	public GridCell getWest() {
		return west;
	}


	public void setWest(GridCell west) {
		this.west = west;
	}


	public GridCell getEast() {
		return east;
	}


	public void setEast(GridCell east) {
		this.east = east;
	}


	public GridCell getNorth() {
		return north;
	}


	public void setNorth(GridCell north) {
		this.north = north;
	}


	public GridCell getSouth() {
		return south;
	}


	public void setSouth(GridCell south) {
		this.south = south;
	}


	public double getTemp() {
		return temp;
	}


	public void setTemp(double temp) {
		this.temp = temp;
	}


	public double getPrevTemp() {
		return prevTemp;
	}


	public void setPrevTemp(double prevTemp) {
		this.prevTemp = prevTemp;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongtitude() {
		return longtitude;
	}


	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}


	public double getLv() {
		return lv;
	}


	public void setLv(double lv) {
		this.lv = lv;
	}


	public double getLb() {
		return lb;
	}


	public void setLb(double lb) {
		this.lb = lb;
	}


	public double getLt() {
		return lt;
	}


	public void setLt(double lt) {
		this.lt = lt;
	}


	public double getH() {
		return h;
	}


	public void setH(double h) {
		this.h = h;
	}


	public double getPerimeter() {
		return perimeter;
	}


	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}


	public double getArea() {
		return area;
	}


	public void setArea(double area) {
		this.area = area;
	}


	public double getProportion() {
		return proportion;
	}


	public void setProportion(double proportion) {
		this.proportion = proportion;
	}


	public int getxCoordinate() {
		return xCoordinate;
	}


	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}


	public int getyCoordinate() {
		return yCoordinate;
	}


	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
    
    
    
    
}