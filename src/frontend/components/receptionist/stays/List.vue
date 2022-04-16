
<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam pobytov <v-spacer></v-spacer>

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
      <v-data-table :headers="headers" :items="stays" :search="search" :custom-filter="customSearch" flat>
        <template v-slot:item.dateFrom="{ item }" >
          {{ formatDate(item.dateFrom) }}</template>
        <template v-slot:item.dateTo="{ item }">
          {{ formatDate(item.dateTo) }}</template>
        <template v-slot:item.state="{ item }">
          {{staysState(item.state) }}</template>
        <template v-slot:item.boardType="{ item }">
          {{staysBoardType(item.boardType) }}</template>
        <template v-slot:item.paymentType="{ item }">
          {{staysPaymentType(item.paymentType) }}</template>
        <template v-slot:item.stayCreator="{ item }">
          {{getCustomerFullName(item.stayCreator) }}</template>

        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="tableRowChangeBtn(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteStay(item.id)"> mdi-delete </v-icon>
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
                <span class="text-h5">Zmeniť pobyt</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row>
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
                            v-model="selectedStartDate"
                            label="Začiatok pobytu"
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
                          v-model="datePickerEndStay"
                          :close-on-content-click="false"
                          :nudge-right="40"
                          transition="scale-transition"
                          offset-y
                          min-width="auto"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                              v-model="selectedEndDate"
                              label="Koniec pobytu"
                              prepend-icon="mdi-calendar"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                            v-model="selectedEndDate"
                            @input="datePickerEndStay = false"
                          ></v-date-picker>
                        </v-menu>

                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedRoomCategory"
                        :items="roomCategories"
                        :item-text="item => roomType(item.type)"
                        item-value=id
                        label="Typ izby"
                      ></v-select>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedState"
                        :items="roomStateEnum"
                        :item-text="item => item.fe"
                        item-value=be
                        label="Stav pobytu"
                      ></v-select>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedStaysBoardType"
                        :items="staysBoardTypeEnum"
                        :item-text="item => item.fe"
                        item-value=be
                        label="Typ pobytu"
                      ></v-select>
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedStaysPayment"
                        :items="staysPaymentEnum"
                        :item-text="item => item.fe"
                        item-value=be
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

      // be = backend interpretation
      // fe = frontend interpretation
      // We need create structure because there is no enumerator table for it
      // TODO create global file with all structures and export it to all *.vue?
      roomStateEnum: [
        { be: 'RESERVED', fe: 'rezervovaný' },
        { be: 'CANCELED', fe: 'zrušený' },
        { be: 'ACTIVE', fe: 'aktívny' },
        { be: 'FINISHED', fe: 'skončený' }
      ],
      staysBoardTypeEnum: [
        { be: 'HALFBOARD', fe: 'polpenzia' },
        { be: 'FULLBOARD', fe: 'plná penzia' }
      ],
      staysPaymentEnum: [
        { be: 'CARD', fe: 'kartou' },
        { be: 'CASH', fe: 'hotovostne' }
      ],

      // form values initialization with fictive error values
      selectedState: "UNAVAILABLE",
      selectedRoomCategory: -1,
      selectedStaysBoardType: "UNAVAILABLE",
      selectedStaysPayment: "UNAVAILABLE",

      selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerStartStay: false,

      selectedEndDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerEndStay: false,


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
          text: "Zákazník",
          align: "start",
          value: "stayCreator",
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
      roomCategories: (state) => state.roomCategories.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {
      newStay() {
       this.$router.push('new-stay');
    },

    customSearch (value, search, item)
    {
      if (Array.isArray(value)) {
      // Only date

      } else {
         let acceptedRoomTypeStr =
           [
            'štandardná', 'apartment', 'luxusná',
            'standardna', 'standardná', 'štandardna', 'luxusna'
           ]

         let acceptedStaysStateStr =
           [
             'rezervovaný', 'zrušený', 'aktívny', 'skončený',
             'rezervovany', 'zruseny', 'zrusený','zrušeny', 'aktivny', 'skonceny', 'skoncený','skončeny',
           ]
         let acceptedStaysBoardTypeStr =
           [
             'polpenzia', 'plná penzia', 'plna penzia',
           ]
         let acceptedStaysPaymentTypeStr =
           [
             'kartou', 'hotovostne',
           ]

         let firstName = item.stayCreator.firstName;
         let lastName = item.stayCreator.lastName;

         if (firstName.toLowerCase().startsWith(search.toLowerCase())){
            return true;
         }

         if (lastName.toLowerCase().startsWith(search.toLowerCase())){
            return true;
         }

         for (let i = 0; i < acceptedRoomTypeStr.length; i++) {
           if (acceptedRoomTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

           let ret = this.roomType(item.roomType)

           if (ret !== "" &&
             ret.toLowerCase().startsWith(search.toLowerCase())){
                return true;
             }
           }
         }

         for (let i = 0; i < acceptedStaysStateStr.length; i++) {
           if (acceptedStaysStateStr[i].toLowerCase().startsWith(search.toLowerCase())){

             let ret = this.staysState(item.state)

             if (ret !== "" &&
               ret.toLowerCase().startsWith(search.toLowerCase())){
               console.log(ret)
               console.log(search)
               console.log(item)
               return true;
             }
           }
         }

        for (let i = 0; i < acceptedStaysBoardTypeStr.length; i++) {
          if (acceptedStaysBoardTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

            let ret = this.staysBoardType(item.boardType)

            if (ret !== "" &&
                ret.toLowerCase().startsWith(search.toLowerCase())){
              console.log(ret)
              console.log(search)
              console.log(item)
              return true;
            }
          }
        }

        for (let i = 0; i < acceptedStaysPaymentTypeStr.length; i++) {
          if (acceptedStaysPaymentTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

            let ret = this.staysPaymentType(item.paymentType)

            if (ret !== "" &&
                ret.toLowerCase().startsWith(search.toLowerCase())){


              console.log(ret)
              console.log(search)
              console.log(item)
              return true;
            }
          }
        }
      }
      return false;
    },

    async getData() {
      await Promise.all([await this.getAllStaysApi()]);
      await Promise.all([await this.getAllRoomCategoriesApi()]);
      this.isLoading = false;
    },

    async getAllStaysApi() {
      try {
        await console.log(this.$store.dispatch("stays/getAll"));
      } catch (error) {
        console.error(error);
      }
    },

    async getAllRoomCategoriesApi() {
      try {
        await this.$store.dispatch("roomCategories/getAll");
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

    formatDateDataPicker(date) {
      return moment(date).format("YYYY-");
    },

    roomType(state) {
      switch (state) {
        case "STANDARD":
          return "štandardná";
        case "APARTMENT":
          return "apartment";
        case "LUXURY":
          return "luxusná";
        default:
          return "";
      }
    },

    roomTypeToRoomID(state) {
      switch (state) {
        case "STANDARD":
          return 1;
        case "APARTMENT":
          return 2;
        case "LUXURY":
          return 3;
        default:
          return "";
      }
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

    tableRowChangeBtn(item){

      // selectedRoomCategory needs id (database primary key from room_category table) of room type
      this.selectedRoomCategory = this.roomTypeToRoomID(item.type)

      // roomState needs backend(be) interpretation
      this.selectedState = item.state

      // Also needs backend interpretation
      this.selectedStaysBoardType = item.boardType
      this.selectedStaysPayment = item.paymentType

      // Needs ISO format => YYYY-MM-DD
      this.selectedStartDate = moment(item.dateFrom).format('YYYY-MM-DD')
      this.selectedEndDate = moment(item.dateTo).format('YYYY-MM-DD')

      this.dialog = true;
    },

    getCustomerFullName(stayCreator)
    {
      return stayCreator.firstName.concat(" ",stayCreator.lastName);
    },


  }
};
</script>
