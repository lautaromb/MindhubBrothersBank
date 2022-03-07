//package com.mindhub.homebanking.methods;
//
//public class RandomNum {
//
//    static int min = 1000;
//    static int max = 9999;
//
//    public static int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }
//
//    public static String getCardNumber(){
//        int randomNumber = getRandomNumber(min,max);
//        return  String.valueOf(randomNumber);
//    }
//
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
//
//}
