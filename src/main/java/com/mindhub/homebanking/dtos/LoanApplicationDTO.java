package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {


   private String loanName;
   private double amount;
   private int payments;
   private String numberAccount;

   public LoanApplicationDTO() {
   }


   public LoanApplicationDTO(String loanName, double amount, int payments, String numberAccount) {
      this.loanName = loanName;
      this.amount = amount;
      this.payments = payments;
      this.numberAccount = numberAccount;
   }

   public String getLoanName() {
      return loanName;
   }

   public void setLoanName(String loanName) {
      this.loanName = loanName;
   }

   public double getAmount() {
      return amount;
   }

   public void setAmount(double amount) {
      this.amount = amount;
   }

   public int getPayments() {
      return payments;
   }

   public void setPayments(int payments) {
      this.payments = payments;
   }

   public String getNumberAccount() {
      return numberAccount;
   }

   public void setNumberAccount(String numberAccount) {
      this.numberAccount = numberAccount;
   }
}
