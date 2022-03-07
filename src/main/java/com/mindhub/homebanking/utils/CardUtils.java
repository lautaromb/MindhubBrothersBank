package com.mindhub.homebanking.utils;

public final class CardUtils {

    public CardUtils() {
    }

    static int min = 1000;
    static int max = 9999;

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getCardNumber(){
        String finalNumbCard = "";
        for (int i = 0; i<=3; i++){
            finalNumbCard += "-" + (int) ((Math.random() * (9999 - 1000)) + 1000);
        }
        finalNumbCard =  finalNumbCard.substring(1);
        return finalNumbCard;
    }


    public static int getCVV(){
        int cvv = getRandomNumber(999, 100);
        return cvv;
    }

//    public static String getCVV(){
//        String numbCard = "";
//
//        for (int i = 0; i<=3; i++){
//            String aux = getCardNumber();
//            numbCard += ( "-" +aux );
//        }
//
//        numbCard =  numbCard.substring(1);
//        return numbCard;
//    }

    //public String cardNumber = (int)((Math.random()* (9999-1000)) + 1000)

}
