package pathimpact;

public class Test {
	public static void m1() {
		m3();
	}
	public static void m2() {
		System.out.println("Bonjour");
	}
	public static void m3() {
		m2();
	}
}
