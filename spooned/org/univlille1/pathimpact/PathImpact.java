

package org.univlille1.pathimpact;


public class PathImpact {
    private java.lang.String s;

    private java.lang.String[] stacktrace;

    private java.lang.String last;

    private java.util.Map<java.lang.String, java.lang.String> reglesKeyNum;

    private java.util.Map<java.lang.String, java.lang.String> reglesKeyProd;

    public PathImpact(java.lang.String stacktrace) {
        this.reglesKeyNum = new java.util.HashMap<java.lang.String, java.lang.String>();
        this.reglesKeyProd = new java.util.HashMap<java.lang.String, java.lang.String>();
        this.stacktrace = stacktrace.split(";");
        this.s = this.stacktrace[0];
        this.last = s;
    }

    public void run() {
        java.lang.String regle;
        java.lang.String bigramme;
        int numRegle = 1;
        int position;
        java.lang.String str = null;
        for (int i = 1; i < (stacktrace.length); i++) {
            str = stacktrace[i];
            regle = null;
            if ((last) != null) {
                bigramme = new java.lang.StringBuilder().append(last).append(";").append(str).toString();
                regle = reglesKeyProd.get(bigramme);
                if (regle == null) {
                    position = s.indexOf(bigramme);
                    if ((position != (-1)) && (position != ((s.length()) - 2))) {
                        regle = java.lang.String.valueOf(numRegle);
                        numRegle = numRegle + 1;
                        reglesKeyNum.put(regle, bigramme);
                        reglesKeyProd.put(bigramme, regle);
                    }
                }
                if (regle != null) {
                    s = s.replaceAll(bigramme, regle);
                    boolean ok = false;
                    while (!ok) {
                        bigramme = s.substring(((s.length()) - 2), ((s.length()) - 1));
                        if (reglesKeyProd.containsKey(bigramme)) {
                            s = s.replaceAll(bigramme, reglesKeyProd.get(bigramme));
                        }else {
                            position = s.indexOf(bigramme);
                            if ((position != (-1)) && (position != ((s.length()) - 2))) {
                                regle = java.lang.String.valueOf(numRegle);
                                numRegle = numRegle + 1;
                                reglesKeyNum.put(regle, bigramme);
                                reglesKeyProd.put(bigramme, regle);
                                s = s.replaceAll(bigramme, regle);
                            }else {
                                ok = true;
                            }
                        }
                    } 
                }
            }
            last = java.lang.String.valueOf(s.charAt(((s.length()) - 1)));
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(s);
            sb.append(";");
            sb.append(str);
            s = sb.toString();
        }
        java.lang.System.out.println(s);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> e : reglesKeyNum.entrySet()) {
            java.lang.System.out.print(e.getKey());
            java.lang.System.out.print(" => ");
            java.lang.System.out.println(e.getValue());
        }
    }

    public static void main(java.lang.String[] args) {
        org.univlille1.pathimpact.PathImpact pi = new org.univlille1.pathimpact.PathImpact("M;B;G;r;G;r;r;A;C;E;r;r;r;x;M;B;C;r;G;r;r;;x;M;A;C;E;r;D;r;r;r;x;M;B;C;E;r;F;r;D;r;r;C;E;r;F;r;D;r;r;r;x;");
        pi.run();
    }
}

