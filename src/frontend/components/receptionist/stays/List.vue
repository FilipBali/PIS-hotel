
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

        <v-btn class="ml-8" color="secondary" @click="newStay">
          Pridať pobyt
        </v-btn>
    </v-card-title>

    <v-card-text>
      <v-data-table :headers="headers" :items="stays" :search="search" flat>
        <template v-slot:item.dateFrom="{ item }">
          {{ formatDate(item.dateFrom) }}</template>
        <template v-slot:item.dateTo="{ item }">
          {{ formatDate(item.dateTo) }}</template>
        <template v-slot:item.state="{ item }">
          {{staysState(item.state) }}</template>
        <template v-slot:item.boardType="{ item }">
          {{staysBoardType(item.boardType) }}</template>
        <template v-slot:item.paymentType="{ item }">
          {{staysPaymentType(item.paymentType) }}</template>

        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="dialog=true">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteStay(item.id)"> mdi-delete </v-icon>
        </template>

      </v-data-table>
      <template>
        <v-row justify="center">
          <v-dialog
            v-model="dialog"
            persistent
            max-width="600px"
          >
                      <v-card>
              <v-card-title>
                <span class="text-h5">Zmeniť pobyt</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        label="Začiatok pobytu"
                      ></v-text-field>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        label="Koniec pobytu"
                      ></v-text-field>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        :items="['luxusná', 'štandardná','apartmán']"
                        label="Typ izby"
                      ></v-select>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        :items="['rezervovaný', 'zrušený', 'aktívny','skončený']"
                        label="Stav pobytu"
                      ></v-select>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        :items="['plná penzia', 'polpenzia']"
                        label="Typ pobytu"
                      ></v-select>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        :items="['kartou', 'hotovostne']"
                        label="Typ platby"
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="dialog = false"
                >
                  Zatvoriť
                </v-btn>
                <v-btn
                  depressed
                  color="primary"
                  @click="dialog = false"
                >
                  Uložiť
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
      </template>
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
      dialog: false,
      search: "",
      dialogController: false,
      dialogRoom: {},
      newRoomDialog: false,
      headers: [
        {
          text: "Číslo pobytu",
          align: "start",
          value: "id",
        },
        {
          text: "Meno zákazníka",
          align: "start",
          value: "stayCreator.firstName",
        },
        {
          text: "Priezvisko zákazníka",
          align: "start",
          value: "stayCreator.lastName",
        },
        { text: "Číslo Izby",
          align: "start",
          value: "rooms.roomNumber"
        },
        { text: "Typ Izby",
          align: "start",
          value: "rooms.roomType"
        },
        { text: "Počiatok pobytu",
          align: "start",
          value: "dateFrom"
        },
        { text: "Koniec pobytu",
          align: "start",
          value: "dateTo"
        },
        {
          text: "Stav pobytu",
          align: "start",
          value: "state",
        },
        {
          text: "Typ pobytu",
          align: "start",
          value: "boardType",
        },
        {
          text: "Počet hostí",
          align: "start",
          value: "accommodatedNumber",
        },
        {
          text: "Platba",
          align: "start",
          value: "paymentType",
        },
        {
          text: "Služby",
          align: "start",
          value: "services",
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
      stays: (state) => state.stays.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {
      newStay() {
       this.$router.push('new-stay');
    },
    async getData() {
      await Promise.all([await this.getAllStaysApi()]);
      this.isLoading = false;
    },
    async getAllStaysApi() {
      try {
        await console.log(this.$store.dispatch("stays/getAll"));
      } catch (error) {
        console.error(error);
      }
    },
    async deleteUserApi(id) {
      try {
        await this.$store.dispatch("stays/delete", id);
      } catch (error) {
        console.error(error);
      }
    },
    formatDate(date) {
      return moment(date).format("DD. MM. YYYY");
    },
    staysState(state) {
      switch (state) {
        case "RESERVED":
          return "rezervovaný";
        case "CANCELED":
          return "zrušený";
        case "ACTIVE":
          return "aktívny";
        case "FINISHED":
          return "skončený";
        default:
          return "";
      }
    },
    staysBoardType(state) {
      switch (state) {
        case "HALFBOARD":
          return "polpenzia";
        case "FULLBOARD":
          return "plná penzia";
        default:
          return "";
      }
    },
    staysPaymentType(state) {
      switch (state) {
        case "CARD":
          return "kartou";
        case "CASH":
          return "hotovostne";
        default:
          return "";
      }
    },
    editStay(room) {
      this.dialogController = true;
      this.newRoomDialog = false;
      this.dialogRoom = JSON.parse(JSON.stringify(stays)); // deepcopy
    },

    async deleteStay(id) {
      await this.deleteStaysApi(id);
      this.getAllStaysApi();
    },

    async deleteStaysApi() {
      try {
        await this.$store.dispatch("stays/delete");
      } catch (error) {
        console.error(error);
      }
    },
    closeDialog(val) {
      this.dialogController = val;
    },





  }
};
</script>
