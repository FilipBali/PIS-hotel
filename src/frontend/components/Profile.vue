<template>
  <v-card>
    <v-card-title> Váš profil </v-card-title>
    <v-card-text>
      <v-form
        ref="form"
        v-model="validForm"
        @submit.prevent="save()"
        lazy-validation
      >
        <v-row>
          <v-col cols="12" sm="6" md="4">
            <v-text-field v-model="user.firstName" label="Meno"></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <v-text-field
              v-model="user.lastName"
              label="Priezvisko"
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <date-picker
              :date="dateOfBirth"
              label="Dátum narodenia"
              @updated-date="updateDate"
            />
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <v-text-field v-model="user.address" label="Adresa"></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <v-text-field
              v-model="user.phoneNumber"
              label="Telefónne číslo"
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <v-text-field
              v-model="user.email"
              :rules="emailRules"
              label="Email"
              required
            ></v-text-field>
          </v-col>
          <!-- <v-col v-if="isNewUser" cols="12" sm="6" md="4">
            <v-text-field
              v-model="user.password"
              :rules="passwordRule"
              type="password"
              label="Heslo"
              required
            ></v-text-field>
          </v-col> -->
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="secondary" text @click="save"> Uložiť </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import { mapState } from "vuex";
import DatePicker from "./DatePicker.vue";
import moment from "moment";

export default {
  components: { DatePicker },
  data() {
    return {
      user: {
        firstName: "",
        lastName: "",
        address: "",
        email: "",
        phoneNumber: "",
        // password: "",
      },

      dateOfBirth: moment().format("yyyy-MM-DD"),

      validForm: true,
      emailRules: [
        (v) => !!v.trim() || "Zadajte email",
        (v) =>
          /^\w+([.-]?[!#$%&'*+/=?^_`{|}~\w]+)*@\w+([.-]?\w+)*(\.\w{2,})+$/.test(
            v
          ) || "Zadajte platný email",
      ],
      // passwordRule: [(v) => !!v.trim() || "Zadajte heslo"],
    };
  },
  computed: {
    ...mapState({
      myUser: (state) => state.users.items,
    }),
    userId() {
      let jwt = this.$auth.$storage.getUniversal("user");
      return jwt.userId;
    },
  },
  created() {
    this.getData();
  },
  methods: {
    async getData() {
      await this.getUserApi();
      this.user = {
        firstName: this.myUser.firstName,
        lastName: this.myUser.lastName,
        address: this.myUser.address,
        email: this.myUser.email,
        phoneNumber: this.myUser.phoneNumber,
      };

      this.dateOfBirth = moment(this.myUser.dateOfBirth).format("yyyy-MM-DD");

      this.isLoading = false;
    },
    async getUserApi() {
      try {
        await this.$store.dispatch("users/getById", this.userId);
      } catch (error) {
        console.error(error);
      }
    },

    async updateUser() {
      try {
        await this.$store.dispatch("users/update", {
          id: this.userId,
          data: this.user,
        });
      } catch (error) {
        console.error(error);
      }
    },

    updateDate(val) {
      this.dateOfBirth = val;
    },

    async save() {
      await this.$refs.form.validate();

      if (this.validForm) {
        this.user.dateOfBirth = moment(this.dateOfBirth).toArray().slice(0, 3);
        console.log("p-onsave-before update");
        console.log(this.user.dateOfBirth);
        await this.updateUser();
        await this.getUserApi();
      }
    },
  },
};
</script>
