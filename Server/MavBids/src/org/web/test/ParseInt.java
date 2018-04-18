package org.web.test;

public class ParseInt {

	public static void main(String[] args) {
		new ParseInt();
	}

	public ParseInt() {
		//System.out.println(toDoubleWithRoundOff("34.5455"));

		String s = "";

		if(s == null)
			System.out.println("Yes");
	}
	double toDoubleWithRoundOff(String s){
        double d = Double.parseDouble(s);
        double rounded = (double) Math.round(d * 100) / 100;
        return rounded;
    }
}
