<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam užívateľov <v-spacer></v-spacer
      ><v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Hľadať"
        single-line
        hide-details
      ></v-text-field>
      <v-btn class="ml-8" color="secondary" @click="newUser">
        Pridať užívateľa
      </v-btn>
    </v-card-title>
    <v-card-text>
      <v-data-table :headers="headers" :items="users" :search="search" flat>
        <template v-slot:item.dateOfBirth="{ item }">
          {{ formatDate(item) }}</template
        >
        <template v-slot:item.roles="{ item }">
          {{ userRole(item.roles[0].name) }}</template
        >
        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="editUser(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteUser(item.id)"> mdi-delete </v-icon>
        </template>
      </v-data-table>
      <admin-users-edit-dialog
        :isNewUser="newUserDialog"
        :dialog="dialogController"
        :user="dialogUser"
        @close-dialog="closeDialog"
      ></admin-users-edit-dialog>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapState } from "vuex";
import moment from "moment";

export default {
  data() {
    return {
      isLoading: true,
      search: "",
      dialogController: false,
      dialogUser: {},
      newUserDialog: false,
      headers: [
        {
          text: "Meno",
          align: "start",
          value: "firstName",
        },
        { text: "Priezvisko", align: "start", value: "lastName" },
        {
          text: "Dátum narodenia",
          align: "start",
          value: "dateOfBirth",
        },
        {
          text: "Adresa",
          align: "start",
          value: "address",
        },
        {
          text: "Číslo občianského preukazu",
          align: "start",
          value: "idNumber",
        },
        {
          text: "Telefónne číslo",
          align: "start",
          value: "phoneNumber",
        },
        {
          text: "Email",
          align: "start",
          value: "email",
        },
        {
          text: "Rola",
          align: "start",
          value: "roles",
        },
        {
          text: "Akcie",
          align: "start",
          value: "actions",
        },
      ],
    };
  },
  computed: {
    ...mapState({
      users: (state) => state.users.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {
    async getData() {
      // TODO: add getAllRoles()
      await Promise.all([this.getAllUsers(), this.getAllRoles()]);
      this.isLoading = false;
    },

    async getAllUsers() {
      try {
        await this.$store.dispatch("users/getAll");
      } catch (error) {
        console.error(error);
      }
    },
    async deleteUser(id) {
      try {
        await this.$store.dispatch("users/delete", id);
      } catch (error) {
        console.error(error);
      }
    },
    async getAllRoles() {
      try {
        await this.$store.dispatch("roles/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    newUser() {
      this.dialogController = true;
      this.newUserDialog = true;
      this.dialogUser = {
        firstName: "",
        lastName: "",
        address: "",
        dateOfBirth: moment().format("yyyy-MM-DD"),
        email: "",
        idNumber: "",
        phoneNumber: "",
      };
    },
    editUser(user) {
      this.dialogController = true;
      this.newUserDialog = false;
      this.dialogUser = JSON.parse(JSON.stringify(user)); // deepcopy
    },
    closeDialog(val) {
      this.dialogController = val;
    },

    formatDate(date) {
      return moment(date).format("DD. MM. YYYY");
    },
    userRole(roleId) {
      switch (roleId) {
        case "ROLE_ADMIN":
          return "administrátor";
        case "ROLE_RECEPTIONIST":
          return "recepčný";
        case "ROLE_COOK":
          return "kuchár";
        case "ROLE_MASSEUR":
          return "masér";
        case "ROLE_BOWLING":
          return "bowling";
        case "ROLE_USER":
          return "užívateľ";
        default:
          return "";
      }
    },
  },
};
</script>
