
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


        <template v-slot:item.stayCreator="{ item }">
          {{getCustomerFullName(item.stayCreator) }}</template>


        <template v-slot:item.actionShowRooms="{ item }">
          <a class="font-italic" @click="roomTableBtn(item)">
            Zobraz izby ({{ getNumberOfRooms(item.rooms) }})
          </a>
        </template>

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


        <template v-slot:item.servicesList="{ item }">
          <a class="font-italic" @click="serviceTableBtn(item)">
            Zobraz služby ({{ getServicesNumber(item) }})
          </a>
        </template>


        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="stayEditFormBtn(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteStay(item.id)"> mdi-delete </v-icon>
        </template>

      </v-data-table>


      <template>
          <v-dialog
            v-model="stayEditForm"
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

<!--                    <v-col-->
<!--                      cols="12"-->
<!--                      sm="6"-->
<!--                    >-->
<!--                      <v-select-->
<!--                        v-model="selectedRoomCategory"-->
<!--                        :items="roomCategories"-->
<!--                        :item-text="item => roomType(item.type)"-->
<!--                        item-value=id-->
<!--                        label="Typ izby"-->
<!--                      ></v-select>-->
<!--                    </v-col>-->

                    <v-col
                      cols="12"
                      sm="4"
                    >
                      <v-select
                        v-model="selectedStayState"
                        :items="stayStateEnum"
                        :item-text="item => item.fe"
                        item-value=be
                        label="Stav pobytu"
                      ></v-select>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="4"
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
                      sm="4"
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
                  @click="stayEditFormCancel()"
                >
                  Zatvoriť
                </v-btn>
                <v-btn
                  depressed
                  color="primary"
                  @click="stayEditFormSave()"
                >
                  Uložiť
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
      </template>

      <template>
        <v-dialog
          v-model="roomEditForm"
          persistent
          max-width="600px"
        >
          <v-card>
            <v-card-title>
              <span class="text-h5">Zmeniť izbu</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectedRoomState"
                      :items="roomStateEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Stav izby"
                    ></v-select>
                  </v-col>

<!--                  <v-col-->
<!--                    cols="12"-->
<!--                    sm="4"-->
<!--                  >-->
<!--                    <v-select-->
<!--                      v-model="selectedRoomType"-->
<!--                      :items="RoomTypeEnum"-->
<!--                      :item-text="item => item.fe"-->
<!--                      item-value=be-->
<!--                      label="Typ izby"-->
<!--                    ></v-select>-->
<!--                  </v-col>-->

                  <v-col
                    cols="12"
                    sm="6"
                  >
                    <v-select
                      v-model="selectedRoomType"
                      :items="roomCategories"
                      :item-text="item => roomType(item.type)"
                      item-value=id
                      label="Typ izby"
                    ></v-select>
                  </v-col>


<!--                  <v-col-->
<!--                    cols="12"-->
<!--                    sm="4"-->
<!--                  >-->
<!--                    <v-select-->
<!--                      v-model="selectedStaysPayment"-->
<!--                      :items="staysPaymentEnum"-->
<!--                      :item-text="item => item.fe"-->
<!--                      item-value=be-->
<!--                      label="Typ platby"-->
<!--                    ></v-select>-->
<!--                  </v-col>-->
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="roomEditFormCancel()"
              >
                Zatvoriť
              </v-btn>
              <v-btn
                depressed
                color="primary"
                @click="roomEditFormSave()"
              >
                Uložiť
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>

      <template>
          <v-dialog
            v-model="roomTable"
            persistent

          >
            <v-card>
              <v-card-title class="text-h8">
                Izby pobytu
              </v-card-title>
              <v-card-text>
                <template>
                  <v-data-table
                    ref="roomsTable"
                    :headers="headersRooms"
                    :items="rooms"
                    :items-per-page="5"
                    class="elevation-1"
                  >

                    <template v-slot:item.roomActions="{ item }">
                      <v-icon small class="mr-2" @click="roomEditFormBtn(item)">
                        mdi-pencil
                      </v-icon>
                      <v-icon small @click="deleteRoomAndUpdateTable(item)"> mdi-delete </v-icon>
                    </template>
                  </v-data-table>
                </template>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="green darken-1"
                  text
                  @click="roomTable = false"
                >
                  Zavrieť
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
      </template>

      <!-- SERVICES TABLE -->
      <template>
        <v-dialog
          v-model="serviceTable"
          persistent>
          <v-card>
            <v-card-title class="text-h8">
              Registrované služby
            </v-card-title>
            <v-card-text>
              <template>
                <v-data-table
                  :headers="headersService"
                  :items="concServices"
                  :items-per-page="5"
                  class="elevation-1"
                >

                  <template v-slot:item.serviceActions="{ item }">
                    <v-icon small class="mr-2" @click="editService()">
                      mdi-arrow-u-right-top
                    </v-icon>
                  </template>

                  <template v-slot:item.timeFrom="{ item }" >
                    {{ getDay(item.timeFrom) }}</template>

                  <template v-slot:item.timeTo="{ item }" >
                    {{ getTime(item.timeFrom,item.timeTo) }}</template>


                </v-data-table>
              </template>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="green darken-1"
                text
                @click="serviceTable = false"
              >
                Zavrieť
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
import BowlingLayout from "../../../layouts/bowling";
import moment from "moment";
import services from "../../../pages/receptionist/services";
import stays from "@/pages/receptionist/stays";

export default {

  components: {BowlingLayout},
  data() {
    return{

      //##########################################
      // be = backend interpretation
      // fe = frontend interpretation
      // We need create structure because there is no enumerator table for it
      //##########################################

      // form values initialization with fictive error values
      selectedStayState: "UNAVAILABLE",
      stayStateEnum: [
        { be: 'RESERVED', fe: 'rezervovaný' },
        { be: 'CANCELED', fe: 'zrušený' },
        { be: 'ACTIVE', fe: 'aktívny' },
        { be: 'FINISHED', fe: 'skončený' }
      ],

      // form values initialization with fictive error values
      selectedStaysBoardType: "UNAVAILABLE",
      staysBoardTypeEnum: [
        { be: 'HALFBOARD', fe: 'polpenzia' },
        { be: 'FULLBOARD', fe: 'plná penzia' }
      ],

      // form values initialization with fictive error values
      selectedStaysPayment: "UNAVAILABLE",
      staysPaymentEnum: [
        { be: 'CARD', fe: 'kartou' },
        { be: 'CASH', fe: 'hotovostne' }
      ],
      // form values initialization with fictive error values
      // selectedRoomCategory: -1,

      roomStateEnum: [
        { be: 'AVAILABLE', fe: 'dostupná' },
        { be: 'OCCUPIED', fe: 'obsadená' },
        { be: 'RESERVED', fe: 'rezervovaná' },
        { be: 'UNAVAILABLE', fe: 'nedostupná' },
      ],

      selectedRoomState: "UNAVAILABLE",
      selectedRoomType: "UNAVAILABLE",

      //##########################################
      //               DATEPICKER
      //##########################################
      selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerStartStay: false,

      selectedEndDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      datePickerEndStay: false,

      //##########################################
      //
      //##########################################
      isLoading: true,

      search: "",

      dialogController: false,
      dialogRoom: {},

      stayEditForm: false,
      roomEditForm: false,

      roomTable: false,
      serviceTable: false,

      rooms: [],
      concServices: [],


      //##########################################
      //         STORAGE/CACHE/REFERENCES
      //##########################################
      saveReferenceToStay: [],
      roomEditFormItem: [],
      stayEditFormItem: [],

      //##########################################
      //               HEADERS
      //##########################################

      //#############################
      //        ROOMS TABLE
      //#############################
      headersRooms: [
        {
          text: "Číslo izby",
          align: "start",
          value: "roomNumber",
        },
        {
          text: "Počet lôžok",
          align: "start",
          value: "bedsNum",
        },
        {
          text: "Typ",
          align: "start",
          value: "roomCategoryType",
        },
        {
          text: "Stav",
          align: "start",
          value: "state",
        },
        {
          text: "Akcie",
          align: "start",
          value: "roomActions",
        },
      ],
      //#############################
      //        Service TABLE
      //#############################
      headersService: [
        {
          text: "Typ služby",
          align: "start",
          value: "serviceType",
        },
        {
          text: "Počet dráh",
          align: "start",
          value: "bowlingTracks",
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
        {
          text: "Akcie",
          align: "start",
          value: "serviceActions",
        },
      ],


      //#############################
      //        STAYS TABLE
      //#############################
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

        { text: "Izby (počet)",
          align: "start",
          value: "actionShowRooms"
        },
        // { text: "Číslo Izby",
        //   align: "start",
        //   value: "rooms.roomNumber"
        // },
        // { text: "Typ Izby",
        //   align: "start",
        //   value: "rooms.roomType"
        // },
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
          text: "Služby(počet)",
          align: "start",
          value: "servicesList",
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
      services: (state) => state.services.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {
      newStay() {
       this.$router.push('new-stay');
    },
    editService() {
      this.$router.push('services');
    },

    //##########################################
    //               TOOLS
    //##########################################

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

    getNumberOfRooms(item){
       return item.length
    },

    getServicesNumber(item)
    {
      let serv = this.services;
      let count = 0;

      for (let i = 0; i < serv.length; i++)
      {
          if (item.id === serv[i].stay.id)
            count++;
      }
      return count;
    },


    formatDate(date) {
      return moment(date).format("DD. MM. YYYY");
    },

    // TODO deprecated?
    // formatDateDataPicker(date) {
    //   return moment(date).format("YYYY-");
    // },
    //
    // editStay(room) {
    //   this.dialogController = true;
    //   this.dialogRoom = JSON.parse(JSON.stringify(stays)); // deepcopy
    // },
    //
    // closeDialog(val) {
    //   this.dialogController = val;
    // },


    //##########################################
    //               ROOM FORM CALL
    //##########################################

    roomTableBtn(item){
      this.roomTable = false;

      this.saveReferenceToStay = item

      this.rooms = []
      for(let i = 0; i < item.rooms.length; i++){
        console.log(item.rooms[i]);
        this.rooms.push({
          id: item.rooms[i].id,
          roomNumber: item.rooms[i].roomNumber,
          bedsNum: item.rooms[i].bedsNum,
          roomCategory: item.rooms[i].roomCategory,
          roomCategoryType: this.roomType(item.rooms[i].roomCategory.type),
          state: this.roomState(item.rooms[i].state)
        })
      }
      console.log(this.rooms)

      this.roomTable = true;
    },

    updateRoomTable(){
      let updatedStay
      for(let i = 0; i < this.stays.length; i++){
        if (this.stays[i].id === this.saveReferenceToStay.id){
          updatedStay = this.stays[i]
          break;
        }
      }
      this.roomTableBtn(updatedStay)
    },

    //##########################################
    //               SERVICE FORM CALL
    //##########################################
    serviceTableBtn(item)
    {

      let serv = this.services;
      this.concServices = [];

      for(let i = 0; i < serv.length; i++)
      {

        if (serv[i].stay.id === item.id )
        {
          this.concServices.push(
            {
              serviceType: serv[i].serviceType,
              bowlingTracks: serv[i].bowlingTracks,
              timeFrom: serv[i].timeFrom,
              timeTo: serv[i].timeTo,
            })
        }
      }

      this.serviceTable = true;
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



    roomEditFormBtn(item){

        console.log(item)
        console.log(item.state)

        this.roomEditFormItem = item

        this.selectedRoomState = this.roomStateToBE(item.state)
        this.selectedRoomType = this.roomTypeToRoomID(item.roomCategoryType)

        this.roomEditForm = true;
    },

    roomEditFormSave(){
      let item = this.roomEditFormItem;
      console.log(item);
      console.log(this.selectedRoomType);

      let roomCategory = item.roomCategory;
      if(item.roomCategory.id !== this.selectedRoomType){

          for(let i = 0; i < this.roomCategories.length; i++ ){

             if (this.roomCategories[i].id === this.selectedRoomType){
                 roomCategory = this.roomCategories[i];
                 break;
             }
          }
      }


      this.editedRoom = {
        id: item.id,
        roomNumber: item.roomNumber,
        state: this.selectedRoomState,
        bedsNum: item.bedsNum,
        roomCategory: roomCategory
      }
      console.log("--------------------------")
      console.log(this.editedRoom)
      this.editRoom(this.editedRoom)
      this.roomEditForm = false;
    },

    roomEditFormCancel(){
      //  TODO reset form

      this.roomEditForm = false;
    },


    //TODO
    serviceTableRowChangeBtn(item){

    },

    stayEditFormBtn(item){

      this.stayEditFormItem = item

      // selectedRoomCategory needs id (database primary key from room_category table) of room type
      // this.selectedRoomCategory = this.roomTypeToRoomID(item.type)

      // roomState needs backend(be) interpretation
      this.selectedStayState = item.state

      // Also needs backend interpretation
      this.selectedStaysBoardType = item.boardType
      this.selectedStaysPayment = item.paymentType

      // Needs ISO format => YYYY-MM-DD
      this.selectedStartDate = moment(item.dateFrom).format('YYYY-MM-DD')
      this.selectedEndDate = moment(item.dateTo).format('YYYY-MM-DD')

      this.stayEditForm = true;

    },

    stayEditFormSave(){
      let item = this.stayEditFormItem

      console.log(this.selectedStartDate)
      console.log(this.selectedEndDate)

      let selectedStartDateMoment = moment(this.selectedStartDate).toArray()
      let selectedEndDateMoment = moment(this.selectedEndDate).toArray()

      //Pretoze indexuje mesiace od 0!!
      selectedStartDateMoment[1] = selectedStartDateMoment[1] + 1
      selectedEndDateMoment[1] = selectedEndDateMoment[1] + 1

      selectedStartDateMoment.length = 3
      selectedEndDateMoment.length = 3

      this.editedStay = {
          accommodatedNumber: item.accommodatedNumber,
          boardType: this.selectedStaysBoardType,
          dateFrom: selectedStartDateMoment,
          dateTo: selectedEndDateMoment,
          hosts: item.hosts,
          id: item.id,
          paymentType: this.selectedStaysPayment,
          rooms: item.rooms,
          state: this.selectedStayState,
          stayCreator: item.stayCreator
      }

      console.log(item)
      console.log(this.editedStay)
      this.editStay(this.editedStay)
      this.stayEditForm = false;
    },

    stayEditFormCancel(){
      //  TODO reset form

      this.stayEditForm = false;
    },


    getCustomerFullName(stayCreator)
    {
      return stayCreator.firstName.concat(" ",stayCreator.lastName);
    },


    //###########################
    //         MAPPING
    //###########################
    roomState(state) {
      switch (state) {
        case "AVAILABLE":
          return "dostupná";
        case "OCCUPIED":
          return "obsadená";
        case "RESERVED":
          return "rezervovaná";
        case "UNAVAILABLE":
          return "nedostupná";
        default:
          return "";
      }
    },

    roomStateToBE(state) {
      switch (state) {
        case "dostupná":
          return "AVAILABLE";
        case "obsadená":
          return "OCCUPIED";
        case "rezervovaná":
          return "RESERVED";
        case "nedostupná":
          return "UNAVAILABLE";
        default:
          return "";
      }
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
        case "štandardná":
          return 1;
        case "APARTMENT":
        case "apartment":
          return 2;
        case "LUXURY":
        case "luxusná":
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

    //##########################################
    //                API CALL
    //##########################################

    //###########################
    //           GET
    //###########################
    async getData() {
      await Promise.all([await this.getAllStaysApi()]);
      await Promise.all([await this.getAllRoomCategoriesApi()]);
      await Promise.all([await this.getAllServicesApi()]);
      this.isLoading = false;
    },

    async getAllStaysApi() {
      try {
        await this.$store.dispatch("stays/getAll");
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

    async getAllServicesApi() {
      try {
        await this.$store.dispatch("services/getAll");
      } catch (error) {
        console.error(error);
      }
    },
    //###########################
    //     EDIT/POST/UPDATE
    //###########################

    async editRoom(data) {
      await this.editRoomApi(data.id, data)
      await this.getData()

      this.updateRoomTable()
    },

    async editRoomApi(id, data) {
      try {
        await this.$store.dispatch("rooms/update", {id, data});
      } catch (error) {
        console.error("error");
        console.error(error);
      }
    },

    async editStay(data) {
      await this.editStayApi(data.id, data)
      await this.getData()

    },

    async editStayApi(id, data) {
      try {
        await this.$store.dispatch("stays/update", {id, data});
      } catch (error) {
        console.error("error");
        console.error(error);
      }
    },

    //###########################
    //           DELETE
    //###########################

    async deleteUserApi(id) {
      try {
        await this.$store.dispatch("stays/delete", id);
      } catch (error) {
        console.error(error);
      }
    },

    async deleteStay(id) {
      await this.deleteStaysApi(id);
      await this.getData();
    },

    async deleteStaysApi(id) {
      try {
        await this.$store.dispatch("stays/delete", id);
      } catch (error) {
        console.error(error);
      }
    },

    async deleteRoomAndUpdateTable(item){
      await this.deleteRoomApi(item.id)
      await this.getData()

      this.updateRoomTable()
    },

    async deleteRoomApi(id) {
      try {
        await this.$store.dispatch("rooms/delete", id);
      } catch (error) {
        console.error(error);
      }
    },

  }
};
</script>
