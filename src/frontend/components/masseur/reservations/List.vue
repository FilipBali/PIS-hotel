<template>
  <v-card>
    <v-card-title primary-title>
      Rezervácie masáží <v-spacer></v-spacer>

      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Hľadať"
        single-line
        hide-details
        v-show="false"
      ></v-text-field>

    </v-card-title>

    <v-card-text>
      <v-data-table :headers="headers" :items="services" :search="search" :custom-filter="customSearch" flat>

        <template v-slot:item.stay.rooms="{ item }" >
          {{ getRooms(item.stay.rooms) }}</template>

        <template v-slot:item.stay.stayCreator="{ item }">
          {{ getCustomerFullName(item.stay.stayCreator) }}</template>

        <template v-slot:item.timeFrom="{ item }" >
          {{ getDay(item.timeFrom) }}</template>

        <template v-slot:item.timeTo="{ item }" >
          {{ getTime(item.timeFrom,item.timeTo) }}</template>

        <template v-slot:item.state="{ item }">
          {{serviceStateType(item.state) }}</template>

      </v-data-table>

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
      search: "MASSAGE",
      dialogController: false,
      dialogRoom: {},
      newRoomDialog: false,
      headers: [
        {
          text: "Služba",
          align: ' d-none',
          value: "serviceType",
        },
        {
          text: "Pobyt",
          align: "start",
          value: "stay.id",
        },
        {
          text: "Izba",
          align: "start",
          value: "stay.rooms",
        },
        {
          text: "Zákazník",
          align: "start",
          value: "stay.stayCreator"
        },
        {
          text: "Čas",
          align: "start",
          value: "timeTo",
        },
        {
          text: "Dátum",
          align: "start",
          value: "timeFrom",
        },
      ],
    };
  },

  computed:
    {
    ...mapState({
      services: (state) => state.services.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {

    async getData() {
      await Promise.all([await this.getAllServicesApi()]);
      this.isLoading = false;
    },

    async getAllServicesApi() {
      try {
        await console.log(this.$store.dispatch("services/getAll"));
      } catch (error) {
        console.error(error);
      }
    },

    serviceStateType(state) {
      switch (state) {
        case "ACTIVE":
          return "aktívny";
        case "RESERVED":
          return "rezervovaný";
        case "CANCELED":
          return "zrušený";
        case "FINISHED":
          return "ukončený";
        default:
          return "";
      }
    },
    getDay(date)
    {
      return moment(date).format("DD. MM. YYYY");
    },

    getTime(start,stop)
    {
      let time = "";
      time = time.concat(start[3].toString(),":",start[4].toString());
      time = time.concat(" - ",stop[3].toString(),":",stop[4].toString())
      return time;
    },

    //Concatenating first and last name to one string
    getCustomerFullName(stayCreator)
    {
      return stayCreator.firstName.concat(" ",stayCreator.lastName);
    },

    //Concatenating rooms ID to one string
    getRooms(rooms)
    {
      let tmp = "";

      for(let i = 0; i < rooms.length; i++)
      {
        if (i === 0)
        {
          tmp = tmp.concat(rooms[i].roomNumber.toString());
        }
        else
        {
          tmp = tmp.concat(",",rooms[i].roomNumber.toString());
        }
      }
      return tmp;
    },




  },
};
</script>
