package uub.staticlayer;

public class TransactionUtils {

    public static String generateUniqueId(int accNO, int transactionAccNo) {
        long currentTimeMillis = System.currentTimeMillis();

        String uniqueIdString = String.format("%014d",currentTimeMillis) + String.format("%04d",accNO%10000) + String.format("%04d",transactionAccNo%10000) ;

        return uniqueIdString;
    }
    

}

