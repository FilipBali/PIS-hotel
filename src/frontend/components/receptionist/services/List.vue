
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

      <v-data-table :headers="headers" :items="services" :search="search" :custom-filter="customSearch" flat>

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
          v-model="newServiceDialog"
          persistent
          max-width="600px"
        >
          <v-card>
            <v-card-title>
              <span class="text-h5">Zaregistrovať službu pre pobyt</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-form
                  v-model="newServiceForm"
                  persistent
                  max-width="600px">
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectedReservationState_FServCreateVal"
                      :items="reservationStateEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Stav rezervácie"
                      required
                      :rules="[v => !!v || 'Item is required']"
                    ></v-select>
                  </v-col>

                    <v-col
                      cols="4"
                      sm="12">
                      <v-select
                        v-model="selectedStay_ServCreateVal"
                        :items="getAvailableStays(stays)"
                        :item-text="item => item.id + ' | ' + item.stayCreator.firstName +' '+ item.stayCreator.lastName"
                        item-value=id
                        @input="selectedStay_ServCreateChange()"
                        label="Existujúce aktívne pobyty"
                        required
                        :rules="[v => !!v || 'Item is required']"
                      ></v-select>
                    </v-col>


                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-dialog
                      ref="dialogTimeFrom"
                      v-model="selectedServiceTimeFrom_FCreateServDialog"
                      :return-value.sync="selectedServiceTimeFrom_FCreateServVal"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeFrom_FCreateServVal"
                          label="Od (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                          :rules="[v => !!v || 'Item is required']"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeFrom_FCreateServDialog"
                        v-model="selectedServiceTimeFrom_FCreateServVal"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeFrom_FCreateServDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeFrom.save(selectedServiceTimeFrom_FCreateServVal)"
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
                      v-model="selectedServiceTimeTo_FCreateServDialog"
                      :return-value.sync="selectedServiceTimeTo_FCreateServVal"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeTo_FCreateServVal"
                          label="Do (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                          :rules="[v => !!v || 'Item is required']"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeTo_FCreateServDialog"
                        v-model="selectedServiceTimeTo_FCreateServVal"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeTo_FCreateServDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeTo.save(selectedServiceTimeTo_FCreateServVal)"
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
                      v-model="selectedServiceDate_FServCreateDialog"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      transition="scale-transition"
                      offset-y
                      min-width="auto"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceDate_FServCreateVal"
                          label="Dátum"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"

                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="selectedServiceDate_FServCreateVal"
                        @input="disabledDataPicker_FServCreate()"

                        :rules="[v => !!v || 'Item is required']"
                        :min="selectedServiceDate_FServCreateMin"
                        :max="selectedServiceDate_FServCreateMax"
                      ></v-date-picker>
                    </v-menu>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectReservationPayment_FSerCreateVal"
                      :items="staysPaymentEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ platby"
                      :rules="[v => !!v || 'Item is required']"
                    ></v-select>
                  </v-col>


                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectedServiceService_FServCreateVal"
                      :items="serviceTypeEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ služby"
                      :rules="[v => !!v || 'Item is required']"
                      required
                    ></v-select>
                  </v-col>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-text-field
                      v-model="selectedServiceTrackAmount_FServCreateVal"
                      :rules="[v => /^(\s*|\d+)$/.test(v)]"
                      label="Počet dráh"
                    ></v-text-field>
                  </v-col>


                </v-row>
              </v-form>
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
                :disabled="!newServiceForm"
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
                      v-model="selectedReservationState_FServEditVal"
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
                      v-model="selectReservationPayment_FSerEditVal"
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
                      v-model="selectedServiceTimeFrom_FEditServDialog"
                      :return-value.sync="selectedServiceTimeFrom_FEditServVal"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeFrom_FEditServVal"
                          label="Od (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeFrom_FEditServDialog"
                        v-model="selectedServiceTimeFrom_FEditServVal"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeFrom_FEditServDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeFrom.save(selectedServiceTimeFrom_FEditServVal)"
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
                      v-model="selectedServiceTimeTo_FEditServDialog"
                      :return-value.sync="selectedServiceTimeTo_FEditServVal"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceTimeTo_FEditServVal"
                          label="Do (čas)"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="selectedServiceTimeTo_FEditServDialog"
                        v-model="selectedServiceTimeTo_FEditServVal"
                        full-width
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="selectedServiceTimeTo_FEditServDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.dialogTimeTo.save(selectedServiceTimeTo_FEditServVal)"
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
                      v-model="selectedServiceDate_FEditServDialog"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      transition="scale-transition"
                      offset-y
                      min-width="auto"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="selectedServiceDate_FEditServVal"
                          label="Dátum"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="selectedServiceDate_FEditServVal"
                        @input="selectedServiceDate_FEditServDialog = false"

                        :min="selectedServiceDate_FServEditMin"
                        :max="selectedServiceDate_FServEditMax"
                      ></v-date-picker>
                    </v-menu>
                  </v-col>


<!--                  <v-col-->
<!--                    cols="12"-->
<!--                    sm="6"-->
<!--                  >-->
<!--                    <v-select-->
<!--                      v-model="selectReservationPayment_FSerEditVal"-->
<!--                      :items="staysPaymentEnum"-->
<!--                      :item-text="item => item.fe"-->
<!--                      item-value=be-->
<!--                      label="Typ platby"-->
<!--                    ></v-select>-->
<!--                  </v-col>-->

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-text-field
                      v-model="selectedServiceTrackAmount_FEditServVal"
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

      //##########################################
      // DESCRIPTION
      //
      // FOR EXAMPLE:
      //  selectedServiceDate_FEditServVal
      //
      //  selected = User
      //  ServiceDate
      //  F
      //  EditServ
      //  Val
      //
      //
      //##########################################

      //##########################################
      //         SERVICE EDIT FORM
      //##########################################
      editServiceForm: false,

      selectedServiceTimeFrom_FEditServDialog: false,
      selectedServiceTimeFrom_FEditServVal:  null,

      selectedServiceTimeTo_FEditServDialog: false,
      selectedServiceTimeTo_FEditServVal:  null,

      selectReservationPayment_FSerEditVal: '',
      selectedReservationState_FServEditVal: '',

      selectedServiceDate_FEditServVal: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      selectedServiceDate_FEditServDialog: false,

      selectedServiceDate_FServEditMin: '',
      selectedServiceDate_FServEditMax: '',

      selectedServiceTrackAmount_FEditServVal: 0,

      //##########################################
      //         SERVICE CREATE FORM
      //##########################################
      newServiceForm: false,
      newServiceDialog: false,

      selectedServiceTimeFrom_FCreateServDialog: false,
      selectedServiceTimeFrom_FCreateServVal:  null,

      selectedServiceTimeTo_FCreateServDialog: false,
      selectedServiceTimeTo_FCreateServVal:  null,

      selectedServiceTime_FServCreateDialog: false,
      selectedServiceTime_FServCreateVal: '',

      selectedServiceDate_FServCreateVal: '',
      selectedServiceDate_FServCreateDialog: false,

      selectReservationPayment_FSerCreateVal: '',
      selectedServiceService_FServCreateVal: "",
      selectedReservationState_FServCreateVal: '',
      selectedServiceTrackAmount_FServCreateVal: '',

      selectedServiceDate_FServCreateMin: '',
      selectedServiceDate_FServCreateMax: '',
      disabledDataPicker_FServCreateVal: true,

      //#########################################

      selectedHostsCombobox: [],
      selectedRoomsCombobox: [],
      selectedStay_ServCreateVal: '',

      isLoading: true,
      search: "",


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

    customSearch (value, search, item)
    {

      console.log(value)
      console.log(search)
      console.log(item)

      let acceptedServiceTypeStr =
        [
          'bowling', 'masáž'
        ]

      let acceptedServiceStateStr =
        [
          'rezervovaný', 'zrušený', 'aktívny', 'skončený',
        ]

      let acceptedServicePaymentTypeStr =
        [
          'kartou', 'hotovostne',
        ]

      let firstName = item.stay.stayCreator.firstName;
      let lastName = item.stay.stayCreator.lastName;

      if (item.stay.stayCreator.id.toString() === search){
        return true;
      }
      if (firstName.toLowerCase().startsWith(search.toLowerCase())){
        return true;
      }

      if (lastName.toLowerCase().startsWith(search.toLowerCase())){
        return true;
      }

      for (let i = 0; i < acceptedServiceTypeStr.length; i++) {
        if (acceptedServiceTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

          let ret = this.serviceNameType(item.serviceType)

          if (ret !== "" &&
            ret.toLowerCase().startsWith(search.toLowerCase())){
            return true;
          }
        }
      }

      for (let i = 0; i < acceptedServiceStateStr.length; i++) {
        if (acceptedServiceStateStr[i].toLowerCase().startsWith(search.toLowerCase())){

          let ret = this.serviceStateType(item.state)

          if (ret !== "" &&
            ret.toLowerCase().startsWith(search.toLowerCase())){
            return true;
          }
        }
      }

      for (let i = 0; i < acceptedServicePaymentTypeStr.length; i++) {
        if (acceptedServicePaymentTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

          let ret = this.servicePaymentType(item.paymentType)

          if (ret !== "" &&
            ret.toLowerCase().startsWith(search.toLowerCase())){

            return true;
          }
        }
      }

      return false;
    },

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
      return moment(date).subtract(1, 'month').format("DD. MM. YYYY");
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
      this.newServiceDialog = true
    },

    //TODO
    newServiceFormSave() {

      let newServiceStay = this.stays.filter(obj => {
        return obj.id === this.selectedStay_ServCreateVal
      })

      console.log("--------------------------------------------")
      console.log(this.selectedServiceDate_FServCreateVal)
      console.log("--------------------------------------------")

      let newServDateTimeFrom = moment(this.selectedServiceDate_FServCreateVal).add(1, 'month').toArray()
      let newServDateTimeTo = moment(this.selectedServiceDate_FServCreateVal).add(1, 'month').toArray()

      let parsedTimeFrom = this.selectedServiceTimeFrom_FCreateServVal.split(':')
      let parsedTimeTo = this.selectedServiceTimeTo_FCreateServVal.split(':')

      newServDateTimeFrom[3] = parseInt(parsedTimeFrom[0])
      newServDateTimeFrom[4] = parseInt(parsedTimeFrom[1])

      newServDateTimeTo[3] = parseInt(parsedTimeTo[0])
      newServDateTimeTo[4] = parseInt(parsedTimeTo[1])


      console.log(newServDateTimeFrom)
      console.log(newServDateTimeTo)
      console.log(this.stays)
      console.log(this.selectedStay_ServCreateVal)
      this.newService = {
        timeFrom: newServDateTimeFrom,
        timeTo: newServDateTimeTo,
        serviceType: this.selectedServiceService_FServCreateVal,
        paymentType: this.selectReservationPayment_FSerCreateVal,
        bowlingTracks: this.selectedServiceTrackAmount_FServCreateVal,
        state: this.selectedReservationState_FServCreateVal,
        stay: newServiceStay[0],

      }

      console.log(this.newService)
      this.createService(this.newService)
      this.newServiceDialog = false;
    },

    //TODO
    newServiceFormCancel() {

      this.newServiceDialog = false;
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


    editServiceFormBtn(item) {

      //TODO rules na selectedServiceTrackAmount_FEditServVal
      // ak otvorim s nezadanym, zatvorim, otvorim so zadanym, zatvorim a opat otovorim s nezadanym, tak vyzaduje cislo
      // =>reset formularu po zavreti

      this.serviceEditFormItem = item

      this.selectedServiceTrackAmount_FEditServVal = item.bowlingTracks
      this.selectReservationPayment_FSerEditVal = item.paymentType
      this.selectedReservationState_FServEditVal = item.state
      this.selectedServiceTimeFrom_FEditServVal = item.timeFrom[3] +':' + item.timeFrom[4]
      this.selectedServiceTimeTo_FEditServVal = item.timeTo[3] +':' + item.timeTo[4]

      this.selectedServiceDate_FServEditMin = moment(item.stay.dateFrom).format("yyyy-MM-DD")
      this.selectedServiceDate_FServEditMax = moment(item.stay.dateTo).format("yyyy-MM-DD")

      this.editServiceForm = true;
    },

    editServiceFormSave() {

      let item = this.serviceEditFormItem
      let newSplitTimeFrom = this.selectedServiceTimeFrom_FEditServVal.split(':')
      let newSplitTimeTo = this.selectedServiceTimeTo_FEditServVal.split(':')

      item.timeFrom[3] = parseInt(newSplitTimeFrom[0])
      item.timeFrom[4] = parseInt(newSplitTimeFrom[1])

      item.timeTo[3] = parseInt(newSplitTimeTo[0])
      item.timeTo[4] = parseInt(newSplitTimeTo[1])

      this.editedService = {
        bowlingTracks: this.selectedServiceTrackAmount_FEditServVal,
        id: item.id,
        paymentType: this.selectReservationPayment_FSerEditVal,
        serviceType: item.serviceType,
        state: this.selectedReservationState_FServEditVal,
        stay: item.stay,
        timeFrom: item.timeFrom,
        timeTo: item.timeTo,
      }

      console.log(newSplitTimeFrom)
      console.log(newSplitTimeTo)
      console.log(this.editedService)

      this.editService(this.editedService)

      this.editServiceForm = false;
    },

    //TODO
    editServiceFormCancel() {

      this.editServiceForm = false;
    },


    async editService(data) {
      await this.editServiceApi(data.id, data)
      await this.getData()

      // this.updateRoomTable()
    },

    async editServiceApi(id, data) {
      try {
        await this.$store.dispatch("services/update", {id, data});
      } catch (error) {
        console.error(error);
      }
    },


    async createService(data) {
      await this.createServiceApi(data)
      await this.getData()

      // this.updateRoomTable()
    },

    async createServiceApi(data) {
      try {
          await this.$store.dispatch("services/create", data);
      } catch (error) {
          console.error(error);
      }
    },


    selectedStay_ServCreateChange(){
        let selectedServiceStay = this.stays.filter(obj => {
          return obj.id === this.selectedStay_ServCreateVal
        })
        console.log(selectedServiceStay)
        console.log(moment(selectedServiceStay[0].dateFrom).format("yyyy-MM-DD"))
        console.log(moment(selectedServiceStay[0].dateTo).format("yyyy-MM-DD"))
        this.selectedServiceDate_FServCreateMin = moment(selectedServiceStay[0].dateFrom).subtract(1, 'month').format("yyyy-MM-DD")
        this.selectedServiceDate_FServCreateMax = moment(selectedServiceStay[0].dateTo).subtract(1, 'month').format("yyyy-MM-DD")
        this.selectedServiceDate_FServCreateVal = moment(selectedServiceStay[0].dateFrom).subtract(1, 'month').format("yyyy-MM-DD")
        this.disabledDataPicker_FServCreateVal = false
    },

    disabledDataPicker_FServCreate(){
      console.log(this.disabledDataPicker_FServCreateVal)
        if (this.disabledDataPicker_FServCreateVal){
          this.selectedServiceDate_FServCreateVal = ''
        }
      this.selectedServiceDate_FServCreateDialog = false
    },

    getAvailableStays(stays){
        let availableStays = []

        for (let i = 0; i < stays.length; i++) {
          if (stays[i].state === "ACTIVE") {
            availableStays.push(stays[i])
          }
        }
        return availableStays
    }
  },
};
</script>
