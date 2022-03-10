let date = new Date();
let output = String(date.getDate()).padStart(2, '0') + '/' + String(date.getMonth() + 1).padStart(2, '0') + '/' + date.getFullYear();
console.log(output)


var app4 = new Vue({
  el: "#app4",
  data: {
    client: {},
    cards: [],
    goodCards:[],
    badCards:[],
    goodCardsLength:0,
    badCardsLength:0,
    cardType:"",
    cardColor:"",
    date: new Date(),
    currentDate: date.getDate(),
    currentYear: date.getFullYear(),
    cardDate: 0,
    cardYear: 0,
    good: true
  },
  created() {
    this.loadData();
    this.dates(this.cards);
  },

  

  methods: {
    loadData() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
        })
        .then(() => {
          this.cards = this.client.cards;
          this.badCards = this.cards.filter(card => card.good == false)
          this.goodCards = this.cards.filter(card => card.good == true)
          this.goodCardsLength = this.goodCards.length;
          this.badCardsLength = this.badCards.length;
        });

      

        
    },
    dates(cardThruDate) {
      let day = parseInt(cardThruDate.slice(5, 7));
      let year = parseInt(cardThruDate.slice(0, 4));
      this.cardDate = day
      this.cardYear = year
      let monthYear = day + "/" + year;
      if((day < this.currentDate && year < this.currentYear)){
        this.good = true
        return monthYear
      }
      return monthYear;
    },
    console() {
      console.log("hola");
    },
    logout(){
      axios.post('/api/logout')
      .then(response => {console.log('signed out!!!') 
      return window.location.href = "/web/index.html"})
  },
  add(arg) {
    this.arg = this.arg + 1;
  },
  deleteCard(id){
    axios.patch(`/api/clients/current/cards/delete/`+id)
    .then(response =>{
      return window.location.reload()
    })
  }
  },
});
