<template>
  <v-dialog v-model="dialogController" max-width="800px">
    <v-card>
      <v-card-title>
        {{ isNewUser ? "Pridať užívateľa" : "Editácia užívateľa" }}
      </v-card-title>
      <v-card-text>
        <v-container>
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
                :date="user.dateOfBirth"
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
            <v-col>
              <!-- <v-select
                  v-model="selectedRole"
                  :items="roleTypes"
                  label="Rola"
                  :rules="[rules.required]"
                ></v-select> -->
            </v-col>
          </v-row>
        </v-container>
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

export default {
  components: { DatePicker },
  props: ["dialog", "user", "isNewUser"],
  data() {
    return {
      dialogController: this.dialog,
      // selectedRole: '',
      // roleTypes: roles.forEach(role => role[0].name),
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
  },
  methods: {
    async createUser() {
      try {
        await this.$store.dispatch("users/create", this.user);
      } catch (error) {
        console.error(error);
      }
    },
    async updateUser() {
      try {
        await this.$store.dispatch("users/update", {
          id: this.user.id,
          data: this.user,
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
      if (this.isNewUser) {
        await this.createUser();
      } else {
        await this.updateUser();
      }
      await this.getAllUsers();
      this.close();
    },
    close() {
      this.dialogController = false;
      this.$emit("close-dialog", this.dialogController);
    },
  },
};
</script>
