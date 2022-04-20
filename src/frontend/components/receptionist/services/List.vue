
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

      <v-btn class="ml-8" color="secondary" @click="newServiceFormBtn(item)">
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
          <v-icon small class="mr-2" @click="editServiceFormBtn(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteServiceBtn(item.id)"> mdi-delete </v-icon>
        </template>


      </v-data-table>

      <!--  NEW SERVICE -->
      <template>
        <v-dialog
          v-model="newServiceForm"
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
                                  v-model="selectedStaysCombobox"
                                  :items="stays"
                                  :item-text="item => item.id + ' | ' + item.stayCreator.firstName +' '+ item.stayCreator.lastName"
                                  item-value=id
                                  label="Existujúce pobyty"
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
                          v-model="selectedDateCreateService"
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
                          v-model="selectedTimeCreateService"
                          label="Čas"
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
                @click="newServiceFormCancel()"
              >
                Zatvoriť
              </v-btn>
              <v-btn
                depressed
                color="primary"
                @click="newServiceFormSave()"
              >
                Uložiť
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>



      <!--  EDIT SERVICE -->
      <template>
        <v-dialog
          v-model="editServiceForm"
          persistent
          max-width="600px"
        >
          <v-card>
            <v-card-title>
              <span class="text-h5">Zmeniť službu</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectReservationStateEdit"
                      :items="reservationStateEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Stav rezervácie"
                    ></v-select>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectReservationPaymentEdit"
                      :items="staysPaymentEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ platby"
                    ></v-select>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-dialog
                      ref="dialogTimeFrom"
                      v-model="selectedServiceTimeFromDialog"
                      :return-value.sync="selectedServiceTimeFromValue"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeFromValue"
                          label="Do (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeFromDialog"
                        v-model="selectedServiceTimeFromValue"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeFromDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeFrom.save(selectedServiceTimeFromValue)"
                        >
                          OK
                        </v-btn>
                      </v-time-picker>
                    </v-dialog>
                  </v-col>


                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-dialog
                      ref="dialogTimeTo"
                      v-model="selectedServiceTimeToDialog"
                      :return-value.sync="selectedServiceTimeToValue"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeToValue"
                          label="Do (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeToDialog"
                        v-model="selectedServiceTimeToValue"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeToDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeTo.save(selectedServiceTimeToValue)"
                        >
                          OK
                        </v-btn>
                      </v-time-picker>
                    </v-dialog>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-menu
                      v-model="selectNewDate"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      transition="scale-transition"
                      offset-y
                      min-width="auto"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedDateService"
                          label="Dátum"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="selectedDateService"
                        @input="selectNewDate = false"
                      ></v-date-picker>
                    </v-menu>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-text-field
                      v-model="selectTrackAmout"
                      :rules="[v => /^(\s*|\d+)$/.test(v)]"
                      label="Počet dráh"
                    ></v-text-field>
                 </v-col>

              </v-row>

              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="editServiceFormCancel()"
              >
                Zatvoriť
              </v-btn>
              <v-btn
                depressed
                color="primary"
                @click="editServiceFormSave()"
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

      reservationStateEnum: [
        { be: 'ACTIVE', fe: 'aktívny' },
        { be: 'RESERVED', fe: 'rezervovaný' },
        { be: 'CANCELED', fe: 'zrušený' },
        { be: 'FINISHED', fe: 'ukončený' }
      ],

      staysPaymentEnum: [
        { be: 'CARD', fe: 'kartou' },
        { be: 'CASH', fe: 'hotovostne' }
      ],

      selectedService: "",

      selectedDateService: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerStartStay: false,

      selectedServiceTimeFromDialog: false,
      selectedServiceTimeFromValue:  null,

      selectedServiceTimeToDialog: false,
      selectedServiceTimeToValue:  null,

      selectedDateCreateService: '',
      selectedTimeCreateService: '',

      selectReservationPaymentEdit: '',
      selectReservationStateEdit: '',

      selectTrackAmout: 0,

      isLoading: true,
      search: "",
      selectedHostsCombobox: [],
      selectedRoomsCombobox: [],
      selectedStaysCombobox: [],

      timePickerService: false,
      newTime: "",

      editServiceForm: false,

      selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      selectNewDate: false,


      dialogController: false,
      newServiceForm: false,

      //##########################################
      //         STORAGE/CACHE/REFERENCES
      //##########################################
      serviceEditFormItem: [],

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
      stays: (state) => state.stays.items,
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
      await Promise.all([await this.getAllStaysApi()]);
      this.isLoading = false;
    },

    async getAllServicesApi() {
      try {
        await this.$store.dispatch("services/getAll");
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


    async deleteServiceBtn(id) {
      await this.deleteServiceApi(id);
      this.getAllServicesApi();
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

    newServiceFormBtn(item) {
      this.newServiceForm = true

      // this.dialogController = true;
      // this.newUserDialog = true;
      // this.dialogUser = {
      //   stayid: "",
      //   lastName: "",
      //   address: "",
      //   dateOfBirth: moment().format("yyyy-MM-DD"),
      //   timeTo: "",
      //   paymentType: "",
      //   state: "",
      // };
    },

    //TODO
    newServiceFormSave() {

      this.newServiceForm = false;
    },

    //TODO
    newServiceFormCancel() {

      this.newServiceForm = false;
    },


    async getAllRoomsApi() {
      try {
        await this.$store.dispatch("rooms/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    async getAllStaysApi() {
      try {
        await this.$store.dispatch("stays/getAll");
      } catch (error) {
        console.error(error);
      }
    },


    //TODO
    editServiceFormBtn(item) {
      console.log(item)

      //TODO rules na selectTrackAmout
      // ak otvorim s nezadanym, zatvorim, otvorim so zadanym, zatvorim a opat otovorim s nezadanym, tak vyzaduje cislo
      // =>reset formularu po zavreti

      this.serviceEditFormItem = item

      this.selectTrackAmout = item.bowlingTracks
      this.selectReservationPaymentEdit = item.paymentType
      this.selectReservationStateEdit = item.state
      this.selectedServiceTimeFromValue = item.timeFrom[3] +':' + item.timeFrom[4]
      this.selectedServiceTimeToValue = item.timeTo[3] +':' + item.timeTo[4]

      this.editServiceForm = true;
    },

    //TODO
    editServiceFormSave()
    {

      let item = this.serviceEditFormItem
      let newSplitTimeFrom = this.selectedServiceTimeFromValue.split(':')
      let newSplitTimeTo = this.selectedServiceTimeToValue.split(':')

      item.timeFrom[3] = parseInt(newSplitTimeFrom[0])
      item.timeFrom[4] = parseInt(newSplitTimeFrom[1])

      item.timeTo[3] = parseInt(newSplitTimeTo[0])
      item.timeTo[4] = parseInt(newSplitTimeTo[1])

      this.newService = {
        bowlingTracks: this.selectTrackAmout,
        id: item.id,
        paymentType: this.selectReservationPaymentEdit,
        serviceType: item.serviceType,
        state: this.selectReservationStateEdit,
        stay: item.stay,
        timeFrom: item.timeFrom,
        timeTo: item.timeTo,
      }

      console.log(newSplitTimeFrom)
      console.log(newSplitTimeTo)
      console.log(this.newService)

      //TODO nemeni sa db
      this.editService(this.newService)

      this.editServiceForm = false;
    },

    //TODO
    editServiceFormCancel()
    {
      this.editServiceForm = false;
    },


    async editService(data) {
      await this.editServiceApi(data.id, data)
      this.getData()

      // this.updateRoomTable()
    },

    async editServiceApi(id, data) {
      try {
        await this.$store.dispatch("services/update", {id, data});
      } catch (error) {
        console.error("error");
        console.error(error);
      }
    },

  },
};
</script>
