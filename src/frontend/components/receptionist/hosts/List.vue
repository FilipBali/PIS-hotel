
<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam pobytov <v-spacer></v-spacer>

      <v-autocomplete
        dense
        auto-select-first
        filled
        rounded
        single-line
        solo
      ></v-autocomplete>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Hľadať"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>

    <v-card-text>
      <v-data-table :headers="headers" :items="hosts" :search="search" flat>
        <template v-slot:item.dateOfBirth="{ item }">
          {{ formatDate(item.dateOfBirth) }}</template>

      </v-data-table>
    </v-card-text>
  </v-card>
</template>
<script>
import { mapState } from "vuex";
import ReceptionistLayout from "../../../layouts/bowling";
import moment from "moment";

export default {
  components: {ReceptionistLayout},
  data() {
    return{
      isLoading: true,
      search: "",
      dialogController: false,
      dialogRoom: {},
      newRoomDialog: false,
      headers: [
        {
          text: "Meno",
          align: "start",
          value: "firstName",
        },
        {
          text: "Priezvisko",
          align: "start",
          value: "lastName",
        },
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
          text: "Číslo",
          align: "start",
          value: "phoneNumber",
        },
        {
          text: "Email",
          align: "start",
          value: "email",
        },
        {
          text: "Pobyt",
          align: "start",
          value: "stayID",
        },
        { text: "Číslo Izby",
          align: "start",
          value: "roomNumber" },
        {
          text: "Stav pobytu",
          align: "start",
          value: "staysState",
        },
        {
          text: "Služby",
          align: "start",
          value: "services",
        },
      ],
    };
  },
  computed: {
    ...mapState({
      hosts: (state) => state.hosts.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {

    async getData() {
      await Promise.all([await this.getAllHostsApi()]);
      this.isLoading = false;
    },

    async getAllHostsApi() {
      try {
        await console.log(this.$store.dispatch("hosts/getAll"));
        await this.$store.dispatch("hosts/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    async deleteHostApi(id) {
      try {
        await this.$store.dispatch("hosts/delete", id);
      } catch (error) {
        console.error(error);
      }
    },

    async deleteHost(id) {
      await this.deleteHostApi(id);
      this.getAllHostsApi();
    },

    formatDate(date) {
      return moment(date).format("DD. MM. YYYY");
    },

  }
};
</script>
