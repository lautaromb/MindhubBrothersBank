var app2 = new Vue({
  el: "#app2",
  data: {
    client: {},
    accounts: {},
    loans: 0,
    initials: "",
    loansInfo: {},
    loanInterest:0,
    mortgageLoanPayments:[],
    personalLoanPayments:[],
    automotiveLoanPayments:[],
    url: "",
    class: "card shadow p-3 mb-5 bg-secondary rounded",
    application: {
      loanName: "",
      amount: 0,
      payments: 0,
      numberAccount: "",
    },
    interesTotal:0,
    cuotas:0
  },

  created() {
    this.loadData();
  },

  updated(){
    this.total();
  },

  methods: {
    loadData: function () {
      const urlParams = new URLSearchParams(window.location.search);
      var myParam = urlParams.get("id");

      axios
        .get(`/api/clients/current`)
        .then((response) => {
          console.log(response);
          this.client = response.data;
          //this.loans = response.data.loans.length;
          this.seeLoansData();
          this.loadAccount();
          //this.client.sort((a,b) => a.id - b.id);
        });
    },
    
    loadAccount:function(){
      axios.get(`/api/clients/current/accounts/`)
      .then(response => {
        this.accounts = response.data
      })
    },
    seeLoansData() {
      axios.get("/api/loans").then((response) => {
        this.loansInfo = response.data;
        this.mortgageLoanPayments = this.loansInfo[0];
        this.personalLoanPayments = this.loansInfo[1];
        this.automotiveLoanPayments = this.loansInfo[2];
      });
    },

    logout() {
      axios.post("/api/logout").then((response) => {
        console.log("signed out!!!");
        return (window.location.href = "/web/index.html");
      });
    },
    dates(creationDate) {
      let monthYear = creationDate.slice(0, 8);
      return monthYear;
    },
    sendLoanApplication() {
      axios
        .post(`/api/loans`, {
          loanName: this.application.loanName,
          amount: this.application.amount,
          payments: this.application.payments,
          numberAccount: this.application.numberAccount,
        })
        .then((response) => {
          console.log("success");
          return (window.location.href = "/web/accounts.html");
        })
        .catch((response) => {
          alert("Amount can't be negative or zero" )
        })
    },
    total(){

      var auxLoanInterest = '';
      if(this.application.loanName == "Automotive"){
        auxLoanInterest = this.automotiveLoanPayments.interest;
      }
      if(this.application.loanName == "Mortgage"){
        auxLoanInterest = this.mortgageLoanPayments.interest;
      }
      if(this.application.loanName == "Personal"){
        auxLoanInterest = this.personalLoanPayments.interest;
      }

      this.loanInterest = auxLoanInterest;
      var total = parseFloat(this.application.amount  * (auxLoanInterest)) + parseFloat(this.application.amount);
      var cuotas = parseFloat(total / this.application.payments);
      this.interesTotal = total;
      this.cuotas = cuotas;
    },

  },
});
