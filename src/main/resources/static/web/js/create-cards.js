var app4 = new Vue({
  el: "#app4",
  data: {
    client: {},
    cards: [],
    cardType: "selectType",
    cardColor: "",
    creditCards: [],
    debitCards: [],
  },
  created() {
    this.loadData();
    
  },
  methods: {
    loadData() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.cards = this.client.cards;
          this.cardFilter();
        })
        .catch(error =>{
          return (window.location.href = "/web/home.html");
        })
        
    },

    logout() {
      axios.post("/api/logout").then((response) => {
        console.log("signed out!!!");
        return (window.location.href = "/web/index.html");
      });
    },
    createNewCard() {
      axios
        .post(
          "/api/clients/current/cards",
          `cardType=${this.cardType}&cardColor=${this.cardColor}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {
          console.log("created" + response);
          return (window.location.href = "/web/cards.html");
        });
    },
    cardFilter() {
      this.creditCards = this.cards.filter(card =>  { return card.typeCard == "CREDIT" && card.good == true});
      this.debitCards = this.cards.filter(card => {return card.typeCard == "DEBIT" && card.good == true});
    },
  },
});
