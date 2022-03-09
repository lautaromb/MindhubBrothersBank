var app = new Vue({
  el: "#app",
  data: {
    users: [],
    jsons: "",
    newClient: {
      firstName: "",
      lastName: "",
      email: "",
    },
    admin: false,
    adminPass: "",
  },
  created() {
    this.loadData();
  },
  computed: {
    isDisabled: function () {
      if (
        this.newClient.email.includes("@") &&
        this.newClient.email.includes(".") &&
        this.newClient.email.length > 8 &&
        this.newClient.firstName.length >= 2 &&
        this.newClient.lastName.length >= 2
      ) {
        return false;
      } else {
        return true;
      }
    },
  },
  methods: {
    addClient: function () {
      //location.reload();
      swal("Good job!", "Client added", "success");
      setTimeout(this.postClient(), 10000);
    },
    postClient: function () {
      axios
        .post("/rest/clients", {
          firstName: this.newClient.firstName,
          lastName: this.newClient.lastName,
          email: this.newClient.email,
        })
        .then((response) => {
          //respuesta del servidor
          console.log(response);
          this.loadData();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    loadData: function () {
      axios
        .get("/rest/clients")
        .then((response) => {
          this.jsons = response.data;
          this.users = response.data._embedded.clients;

          console.table(this.users);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    eliminar: function (arg1) {
      axios.delete(arg1).then((response) => {
        console.log(response);
        this.loadData();
      });
    },
    adminLog: function (){
      if (this.adminPass == "1234"){
        this.admin = true;
      }else{
        swal("Incorrect", "Email or password", "error");

      }
    }
  },
});
