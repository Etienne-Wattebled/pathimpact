
public class Test { 
	public static void main(String args[]) {
		a();
		b();
		c();
		d();
		e();
		f();
		g();	
	}
	public static void a() {
		b();
		d();
		e();
	}
	public static void b() {
		e();
		f();
		g();
	}
	public static void c() {
		h();
		i();
		j();
	}
	public static void d() {
	}
	public static void e() {
		k();
	}
	public static void f() {
		e();
	}
	public static void g() {
		f();
	}
	public static void h() {
		g();
	}
	public static void i() {
		l();
		k(); h();
	}
	public static void j() {
	}
	public static void k() {
		
	}
	public static void l() {
		
	}
}
