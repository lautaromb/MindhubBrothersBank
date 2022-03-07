var app2 = new Vue({
  el: "#app2",
  data: {
    client: {},
    accountsLength:0,
    account:{},
    accountType:"",
    loans:0,
    initials: "",
    url: "",
    class:"card shadow p-3 mb-5 bg-secondary rounded"
  },

  created() {
    this.loadData();
  },

  methods: {
    loadData: function(){
      const urlParams = new URLSearchParams(window.location.search);
      var myParam = urlParams.get("id");
    
      axios
      .get(`http://localhost:8080/api/clients/current`,)
      .then((response) => {
        console.log(response);
        this.client = response.data;
        this.loans = response.data.loans.length;
        this.initials = this.client.firstName.slice(0,1) + this.client.lastName.slice(0,1);
        console.log(this.loans)
        
        //this.client.sort((a,b) => a.id - b.id);
        this.getAccounts();
      });

    },
    getAccounts(){
      axios.get("http://localhost:8080/api/clients/current/accounts/")
      .then(response => {
        this.account = response.data;
        this.accountsLength = this.account.length;
        this.account.sort((a,b) => a.id - b.id);
      })
    },
    logout(){
      axios.post('/api/logout')
      .then(response => {console.log('signed out!!!') 
      return window.location.href = "/web/index.html"})
  },
  createNewAccount(){
    axios.post('/api/clients/current/accounts', 
    { headers: { "content-type": "application/x-www-form-urlencoded" } })
    .then((response) => {
      console.log("created" + response);
      return window.location.reload();
    });
  },
  dates(creationDate) {
    let monthYear = creationDate.slice(0, 8);
    return monthYear;
  },
  applyForLoan(){
    return window.location.href = "/web/loan-application.html"
  },
  deleteAccount(id){
    axios.patch(`/api/clients/current/accounts/delete/`+id)
    .then(response => {
      return window.location.reload()
    })
  },
  changeAccountType(id){
    axios.patch('/api/clients/current/accounts/change/' + id)
  }
  },
  
});
