
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
      <v-btn class="ml-8" color="secondary" @click="newReservation">
        Pridať rezerváciu
      </v-btn>
    </v-card-title>

    <v-card-text>
      <v-data-table :headers="headers" :items="rooms" :search="search" flat>
        <template v-slot:item.state="{ item }">
          {{ roomState(item.state) }}</template>

      </v-data-table>
    </v-card-text>
  </v-card>
</template>
<script>
import { mapState } from "vuex";
import ReceptionistLayout from "../../../layouts/bowling";

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
          text: "Služba",
          align: "start",
          value: "service",
        },
        {
          text: "Spravuje",
          align: "start",
          value: "manages",
        },
        { text: "Čas",
          align: "start",
          value: "time" },
        {
          text: "Rezervácie",
          align: "start",
          value: "reservation",
        },
        {
          text: "Maximálna kapacita na deň",
          align: "start",
          value: "serviceCapacity",
        },
        {
          text: "Cena",
          align: "start",
          value: "serviceCost",
        },
      ],
    };
  },
  methods: {
    newReservation() {
      // todo: doplnit room type
      this.$router.push('new-stay');
    },
  }
};
</script>
