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

      // if(this.firstName.length() < 1){
      //   swal("Error", "First name to short", "error")  
      // }
      // if(this.lastName.length() < 1){
      //   swal("Error", "Last name to short", "error")  
      // }

      if(this.password.length < 5){
        swal("Error", "Password must be 6 characters long", "error")  
      }

      if(this.email.length == ""){
        swal("Error", "Please add an email", "error")  
      }

      if( !this.email.includes("@" && ".com")) {
        
        swal("Error", "Typo in email", "error")  
      }else{
        if(this.email.length > 6 ){
        
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

        });}else{
          swal("Error", "Email adress not supported", "error")
        }
      
      }
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
