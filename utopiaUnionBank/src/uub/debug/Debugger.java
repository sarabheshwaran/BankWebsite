package uub.debug;

import java.util.ArrayList;

import uub.enums.TransferType;
import uub.logicallayer.CustomerHelper;
import uub.model.Transaction;
import uub.staticlayer.CustomBankException;

class Checker implements Runnable{

	

	@Override
	public void run() {
		
		Transaction transaction = new Transaction();
		
		transaction.setId(""+Math.random()*100);
		transaction.setAccNo(3);
		transaction.setAmount(100);
		transaction.setUserId(1);
		transaction.setType(TransferType.DEPOSIT);
		transaction.setDesc("dsfd");
		transaction.setTime(0);
		transaction.setStatus(0);
		
		
		try {
			CustomerHelper a = new CustomerHelper();
			a.makeTransaction(transaction, "sara");
		} catch (CustomBankException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

public class Debugger {
	
	

	public static void main(String[] args) throws CustomBankException {
		
//		TransactionHelper a = new TransactionHelper();
		
		Checker c = new Checker();
		
		ArrayList<Thread> threadPool = new ArrayList<Thread>();
		
		for(int i=0; i<10; i++ ) {
			
			 threadPool.add(new Thread(c));
			
		}
		
		for(Thread t: threadPool) {

			t.start();
		}
	}
	
}
