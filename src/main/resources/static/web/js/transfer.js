var appTransfer = new Vue({
    el: "#appTransfer",
    data: {
      account: {},
      amount: 0,
      sendFrom:"",
      sendTo: "",
      description: "",
      transferForm: "self"
    },
  
    created() {
      this.loadData();
    },
  
    methods: {
      loadData: function(){
      
        axios
        .get(`/api/clients/current/accounts`)
        .then((response) => {
          console.log(response);
          this.account = response.data;
        });
      },
      logout(){
        axios.post('/api/logout')
        .then(response => {console.log('signed out!!!') 
        return window.location.href = "/web/index.html"})
    },
    makeTransfer(){
      axios.post("/api/transactions",
       `amount=${this.amount}&description=${this.description}&numberSend=${this.sendFrom}&numberReceiver=${this.sendTo}`,
       { headers: { "content-type": "application/x-www-form-urlencoded" } })
       .then(response => {
         console.log(response + "transaction complete")
         swal("Success", "Transaction complete", "success");
       })
       .catch((response) => {  Swal.fire({ 
          text: 'Transaction invalid', icon: 'error',showConfirmButton: false,timer: 3000 })
            console.log(response);
           });

    },
    applyForLoan(){
      return window.location.href = "/web/loan-application.html";
    }
    },
  });
  