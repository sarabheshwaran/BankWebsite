package uub.logicalLayer;

import java.math.BigDecimal;

import uub.staticLayer.CustomerUtils;
import uub.staticLayer.HelperUtils;

interface Doggable{
	
	public static void g() {
		
		System.out.println("lkj");
	}
	
}

public class Main {



	public static void main(String[] args) {
		
//		double a = 0.30000000000000004;
//		String g = String.format("%.2f", a);
//		
//		a = 2.193943523492;
//		
//		a = Double.parseDouble(g);
		
		long tday = System.currentTimeMillis();
		
		System.out.println(CustomerUtils.formatDate(tday));
		
		long b = tday-(365 * 24L * 60L * 60L * 1000L );
		
		
		System.out.println(0.1+0.2);
		
//		System.out.println(0.30000000000000004+0.2);
//		
//		 BigDecimal num1 = new BigDecimal("0.1");
//	        BigDecimal num2 = new BigDecimal("0.2");
//
//	        BigDecimal sum = num1.add(num2);
//
//	        System.out.println("Sum: " + sum.doubleValue());

	}
}
