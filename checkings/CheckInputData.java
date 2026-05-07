package checkings;

public interface CheckInputData {
	public boolean checkCoords(double coord);
	
	public boolean checkCode(String code);
	
	public boolean checkStartTime(int h, int m);
	
	public boolean checkDuration(int m);
}
