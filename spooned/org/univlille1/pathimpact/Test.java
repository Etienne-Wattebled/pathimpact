

package org.univlille1.pathimpact;


public class Test {
    public static void main(java.lang.String[] args) {
        new spoon.Launcher().run(new java.lang.String[]{ "-x" , "-g" , "-i" , "src/main/java" });
        org.univlille1.pathimpact.Test.a(10);
    }

    public static void a(int a) {
        org.univlille1.pathimpact.Test.b(6);
        org.univlille1.pathimpact.Test.c(10);
    }

    public static void b(int a) {
        org.univlille1.pathimpact.Test.c(50);
    }

    public static void c(int a) {
    }
}

