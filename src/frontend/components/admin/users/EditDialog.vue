<template>
  <v-dialog v-model="dialogController" max-width="800px" @click:outside="close">
    <v-card>
      <v-card-title>
        {{ isNewUser ? "Pridať užívateľa" : "Editácia užívateľa" }}
      </v-card-title>
      <v-card-text>
        <v-form
          ref="form"
          v-model="validForm"
          @submit.prevent="save()"
          lazy-validation
        >
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <v-text-field
                v-model="user.firstName"
                label="Meno"
              ></v-text-field>
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
              <v-text-field
                v-model="user.address"
                label="Adresa"
              ></v-text-field>
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
            <v-col v-if="isNewUser" cols="12" sm="6" md="4">
              <v-text-field
                v-model="user.password"
                :rules="passwordRule"
                type="password"
                label="Heslo"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select
                v-if="isNewUser"
                v-model="newSelectedRole"
                :items="roles"
                :item-text="(item) => this.userRole(item.name)"
                label="Rola"
                return-object
              ></v-select>
              <v-select
                v-else
                v-model="editSelectedRole"
                :items="roles"
                :item-text="(item) => this.userRole(item.name)"
                label="Rola"
                return-object
              ></v-select>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="secondary" text @click="close"> Zrušiť </v-btn>
        <v-btn color="secondary" text @click="save"> Uložiť </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";
import DatePicker from "../../DatePicker.vue";
import moment from "moment";

export default {
  components: { DatePicker },
  props: ["dialog", "user", "isNewUser"],
  data() {
    return {
      dialogController: this.dialog,
      dateOfBirth: moment(this.user.dateOfBirth).format("yyyy-MM-DD"),
      newSelectedRole: "užívateľ",
      editSelectedRole:
        this.user.roles && this.user.roles.length > 0
          ? this.user.roles[0]
          : "užívateľ",

      validForm: true,
      emailRules: [
        (v) => !!v.trim() || "Zadajte email",
        (v) =>
          /^\w+([.-]?[!#$%&'*+/=?^_`{|}~\w]+)*@\w+([.-]?\w+)*(\.\w{2,})+$/.test(
            v
          ) || "Zadajte platný email",
      ],
      passwordRule: [(v) => !!v.trim() || "Zadajte heslo"],
    };
  },
  computed: {
    ...mapState({
      roles: (state) => state.roles.items,
    }),
  },
  watch: {
    dialog() {
      this.dialogController = this.dialog;
    },
    user() {
      this.editSelectedRole =
        this.user.roles && this.user.roles.length > 0 ? this.user.roles[0] : "";
    },
  },
  methods: {
    async createUser() {
      try {
        await this.$store.dispatch("users/create", this.user);
      } catch (error) {
        console.error(error);
      }
    },
    // TODO: update user fix after be fix
    async updateUser() {
      let user = {
        id: this.user.id,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        address: this.user.address,
        email: this.user.email,
        phoneNumber: this.user.phoneNumber,
      };
      try {
        await this.$store.dispatch("users/update", {
          id: this.user.id,
          data: user,
        });
      } catch (error) {
        console.error(error);
      }
    },
    async getAllUsers() {
      try {
        await this.$store.dispatch("users/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    updateDate(val) {
      this.user.dateOfBirth = val;
    },

    async save() {
      await this.$refs.form.validate();

      if (this.validForm) {
        this.user.dateOfBirth = this.dateOfBirth;

        if (this.isNewUser) {
          this.user.roles.push(this.newSelectedRole);
          await this.createUser();
        } else {
          if (this.user.roles.length > 0) {
            this.user.roles[0] = this.editSelectedRole;
          } else {
            this.user.roles.push(this.editSelectedRole);
          }
          console.log(this.user);
          await this.updateUser();
        }
        await this.getAllUsers();
        this.close();
      }
    },
    close() {
      this.dialogController = false;
      this.selectedRole = "";
      this.$refs.form.resetValidation();
      this.$emit("close-dialog", this.dialogController);
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
