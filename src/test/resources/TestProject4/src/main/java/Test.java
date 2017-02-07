package pathimpact;

public class Test {
	public static void main(String args[]) {
		m1();
		m();
	}
	public static void m() {
		m2();
		m4();
	}
	public static void m1() {
		m1();
	}
	public static void m2() {
		m3();
	}
	public static void m3() {
		m();
	}
	public static void m4() {
		
	}
}
