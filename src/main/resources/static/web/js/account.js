
var app3 = new Vue({
  el: "#app3",
  data: {
    account: {},
    transaction: {},
    bgColor: "",
    classes: ["debito", "credito", "white"],
  },

  created() {
    this.loadData();
  },

  methods: {
    loadData: function () {
      const urlParams = new URLSearchParams(window.location.search);
      const myParam = urlParams.get("id");
      console.log(urlParams)
      axios
        .get(`/api/clients/current/accounts/${myParam}`)
        .then((response) => {
          console.log(response);
          this.account = response.data;
          this.transaction = this.account[0].transactions;
          //this.transaction.sort((a,b) => a.id - b.id);
          //var filtered = this.account.find(account => account.id = myParam);
          //this.account = filtered;
          //this.transaction = filtered.transaction
        });
    },
    logout(){
      axios.post('/api/logout')
      .then(response => {console.log('signed out!!!') 
      return window.location.href = "/web/index.html"})
  }
  },
});
