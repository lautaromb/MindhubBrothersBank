var appIndex = new Vue({
  el: "#appIndex",
  data: {
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    signupState: false,
    dolarToday: "",
  },
  created() {
    
  },
  methods: {
    login() {
      axios
        .post("/api/login", `email=${this.email}&password=${this.password}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((response) => {
          console.log("signed in!!!");
          return (window.location.href = "/web/accounts.html");
        })
        .catch((error) => swal("Error", "Wrong email or password", "error"));
      this.email = "";
      this.password = "";
      console.log(`Hola ${this.email} ${this.password}`);
    },
    signup() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
          { headers: { "content-type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {
          console.log("registered");
          swal("Good job!", "User registered", "success");
          this.login();

        });
    },
    logout(){
      axios.post('/api/logout')
      .then(response => {console.log('signed out!!!') 
      return window.location.href = "/web/index.html"})
  }
    // dolarAPI() {
    //     axios(`https://api-dolar-argentina.herokuapp.com/api/dolaroficial`)
    //     .then((response) => {
    //         this.dolarToday = response;
    //     })
    //     .catch(e => {
    //         console.log(e)
    //     })
    // },
  },
});
