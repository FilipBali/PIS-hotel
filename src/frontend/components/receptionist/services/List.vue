
<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam služieb <v-spacer></v-spacer>

      <v-spacer></v-spacer>


      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Hľadať"
        single-line
        hide-details
      ></v-text-field>

      <v-btn class="ml-8" color="secondary" @click="dialog = true">
        Pridať službu
      </v-btn>

    </v-card-title>

    <v-card-text>

      <v-data-table :headers="headers" :items="services" :search="search" flat>

        <template v-slot:item.stay.stayCreator="{ item }">
          {{ getCustomerFullName(item.stay.stayCreator) }}</template>

        <template v-slot:item.timeFrom="{ item }" >
          {{ getDay(item.timeFrom) }}</template>

        <template v-slot:item.timeTo="{ item }" >
          {{ getTime(item.timeFrom,item.timeTo) }}</template>

        <template v-slot:item.serviceType="{ item }">
          {{serviceNameType(item.serviceType) }}</template>

        <template v-slot:item.paymentType="{ item }">
          {{servicePaymentType(item.paymentType) }}</template>

        <template v-slot:item.state="{ item }">
          {{serviceStateType(item.state) }}</template>

        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="editService(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteService(item.id)"> mdi-delete </v-icon>
        </template>


      </v-data-table>

      <template>
        <v-dialog
          v-model="dialog"
          persistent
          max-width="600px"
        >
          <v-card>
            <v-card-title>
              <span class="text-h5">Zaregistrovať službu</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                    <v-col
                      cols="4"
                      sm="12">
                      <v-list>
                        <template>
                          <v-container fluid>
                            <v-row>
                              <v-col cols="12">
                                <v-combobox
                                  v-model="selectedRoomsCombobox"
                                  :items="rooms"
                                  :item-text="item => item.roomNumber"
                                  item-value=id
                                  label="Existujúce izby"
                                  multiple
                                  outlined
                                  dense
                                >
                                </v-combobox>
                              </v-col>
                            </v-row>
                          </v-container>
                        </template>
                      </v-list>
                    </v-col>

                   <v-col
                    cols="12"
                    sm="12">
                    <v-list>
                    <template>
                      <v-container fluid>
                        <v-row>
                          <v-col cols="12">
                            <v-combobox
                              v-model="selectedHostsCombobox"
                              :items="services"
                              :item-text="item => item.stay.stayCreator.firstName +' '+ item.stay.stayCreator.lastName  + ' | ' +item.stay.stayCreator.idNumber"
                              item-value=id
                              label="Existujúci hostia"
                              multiple
                              outlined
                              dense
                            >
                            </v-combobox>
                          </v-col>
                        </v-row>
                      </v-container>
                    </template>
                  </v-list>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-menu
                      v-model="datePickerStartStay"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      transition="scale-transition"
                      offset-y
                      min-width="auto"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedDate"
                          label="Dátum"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="selectedStartDate"
                        @input="datePickerStartStay = false"
                      ></v-date-picker>
                    </v-menu>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-menu
                      v-model="timePickerService"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      transition="scale-transition"
                      offset-y
                      min-width="auto"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedTime"
                          label="Dátum"
                          prepend-icon="mdi-alarm"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                    <v-time-picker
                      v-model="newTime"
                      format="ampm"
                      @input="timePickerService = false"
                    ></v-time-picker>
                    </v-menu>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >

                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectedService"
                      :items="serviceTypeEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ služby"
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
      </template>

     </v-card-text>
  </v-card>
</template>
<script>

import { mapState } from "vuex";
import moment from "moment";

export default {
  data() {
    return{
      serviceTypeEnum: [
        { be: 'BOWLING', fe: 'bowling' },
        { be: 'MASSAGE', fe: 'masáž' }
      ],

      isLoading: true,
      search: "",
      selectedHostsCombobox: [],
      selectedRoomsCombobox: [],

      timePickerService: false,
      newTime: "",

      selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerStartStay: false,


      dialogController: false,
      dialogRoom: {},
      dialog: false,
      newRoomDialog: false,
      headers: [
        {
          text: "Pobyt",
          align: "start",
          value: "stay.id",
        },
        {
          text: "Zákazník",
          align: "start",
          value: "stay.stayCreator",
        },
        {
          text: "Služba",
          align: "start",
          value: "serviceType",
        },
        {
          text: "Počet tratí",
          align: "start",
          value: "bowlingTracks"
        },
        {
          text: "Deň",
          align: "start",
          value: "timeFrom"
        },
        {
          text: "Hodiny",
          align: "start",
          value: "timeTo"
        },
        {
          text: "Typ platby",
          align: "start",
          value: "paymentType"
        },
        {
          text: "Rezervácie",
          align: "start",
          value: "state",
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
      services: (state) => state.services.items,
      rooms: (state) => state.rooms.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {

    async getData() {
      await Promise.all([await this.getAllServicesApi()]);
      await Promise.all([await this.getAllRoomsApi()]);
      this.isLoading = false;
    },

    async getAllServicesApi() {
      try {
        await console.log(this.$store.dispatch("services/getAll"));
      } catch (error) {
        console.error(error);
      }
    },

    async deleteServiceApi(id) {
      try {
        await this.$store.dispatch("services/delete", id);
      } catch (error) {
        console.error(error);
      }
    },


    /*async deleteService(id) {
      await this.deleteServiceApi(id);
      this.getAllServicesApi();
    },
*/

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

    serviceNameType(state) {
      switch (state) {
        case "BOWLING":
          return "bowling";
        case "MASSAGE":
          return "masáž";
        default:
          return "";
      }
    },

    servicePaymentType(state) {
      switch (state) {
        case "CASH":
          return "hotovostne";
        case "CARD":
          return "kartou";
        default:
          return "";
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

    newService() {
      this.dialogController = true;
      this.newUserDialog = true;
      this.dialogUser = {
        stayid: "",
        lastName: "",
        address: "",
        dateOfBirth: moment().format("yyyy-MM-DD"),
        timeTo: "",
        paymentType: "",
        state: "",
      };
    },

    editService(user) {

    },

    async getAllRoomsApi() {
      try {
        await this.$store.dispatch("rooms/getAll");
      } catch (error) {
        console.error(error);
      }
    },



  },
};
</script>
