<template>
    <v-card>
      <v-card-title>
          {{ "Formulár pre nový pobyt" }}

        <v-spacer></v-spacer>

        <v-dialog
          v-model="formNewHost"
          persistent
          max-width="600px"
        >
          <!-- button newHost-->
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="secondary"
              dark
              v-bind="attrs"
              v-on="on"
            >
              Nový hosť
            </v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">Vytvorenie nového hosťa</span>
            </v-card-title>

            <!-- newHost form-->
            <v-card-text>
              <v-container>
                <v-form
                  ref="formNewHost"
                  v-model="validFormNewHost"
                  >
                  <v-row>

                    <!--        Name         -->
                    <!-- create newHost Form -->
                    <v-col
                      cols="12"
                      sm="6"
                      md="6"
                    >
                      <v-text-field
                        v-model="newHostFormName"
                        :rules="[v => !!v || 'Item is required']"
                        label="Meno*"
                        required
                      ></v-text-field>
                    </v-col>

                    <!--        Surname      -->
                    <!-- create newHost Form -->
                    <v-col
                      cols="12"
                      sm="6"
                      md="6"
                    >
                      <v-text-field
                        v-model="newHostFormSurname"
                        :rules="[v => !!v || 'Item is required']"
                        label="Priezvisko*"
                        required
                      ></v-text-field>
                    </v-col>


                    <!--         ID          -->
                    <!-- create newHost Form -->
                    <v-col
                      cols="6"
                      sm="6"
                      md="6"
                    >
                      <v-text-field
                        v-model="newHostFormID"
                        label="číslo OP*"
                        :rules="[v => /^E(\d){9}$/.test(v) || 'Item is required']"
                        required
                      ></v-text-field>
                      <small>Nápoveda: E + deväť čísiel (príklad E123456789)</small>
                    </v-col>

                    <!--      Birthdate      -->
                    <!-- create newHost Form -->
                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-menu
                        v-model="datePickerBirthStay"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        transition="scale-transition"
                        offset-y
                        required
                        min-width="auto"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="newHostFormBirthDate"
                            label="Dátum narodenia"
                            prepend-icon="mdi-calendar"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker
                          v-model="newHostFormBirthDate"
                          @input="datePickerBirthStay = false"
                          :max="newHostFormBirthDate_max"
                        ></v-date-picker>
                      </v-menu>
                    </v-col>


                    <!--       Address       -->
                    <!-- create newHost Form -->
                    <v-col cols="12">
                      <v-text-field
                        v-model="newHostFormAddress"
                        label="Adresa*"
                        :rules="[v => !!v || 'Item is required']"
                        required
                      ></v-text-field>
                    </v-col>

                    <!--        Email        -->
                    <!-- create newHost Form -->
                    <v-col cols="12">
                      <v-text-field
                        v-model="newHostFormEmail"
                        :rules="[v => /.+@.+\..+/.test(v) || 'Item is required']"
                        label="E-mail*"
                        required
                      ></v-text-field>
                    </v-col>

                    <!--     Phone number    -->
                    <!-- create newHost Form -->
                    <v-col cols="12">
                      <v-text-field
                        v-model="newHostFormPhoneNumber"
                        :rules="[v => /^00(\d!*){10,12}$/.test(v) || 'Item is required']"
                        label="Telefónne číslo*"
                        required
                      ></v-text-field>
                      <small>Nápoveda: 00 + predvolba + číslo (príklad 00421222333444)</small>
                    </v-col>

                  </v-row>
                </v-form>
              </v-container>
              <small>*povinné</small>
            </v-card-text>

            <!-- create newHost form footer -->
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="cancelNewHost()"
              >
                Zavrieť
              </v-btn>
              <v-btn
                :disabled="!validFormNewHost"
                depressed
                color="primary"
                @click="createNewHost()"
              >
                Vytvoriť a pridať
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>


      </v-card-title>

      <v-card-text>
        <v-container fluid>
          <template>

            <!----------------------------->
            <!-- Formulár pre nový pobyt -->
            <!----------------------------->
            <v-form
              ref="addStayForm"
              v-model="valid"
            >

              <small style="color:red">
                Na prveho sa zaklada rezervacia
              </small>
              <style>
                 .hostCombobox .v-select__selection--comma:nth-child(1) {
                  /*text-decoration: underline;*/
                  color: #e74c3c;
                }
              </style>
              <div
              class="hostCombobox"
              >
              <v-combobox
                v-model="selectedHostsCombobox"
                :items="hosts"
                :item-text="item => item.firstName +' '+ item.lastName  + ' | ' +item.idNumber"
                item-value=id
                :id="hostCombobox"
                label="Existujúci hostia ( Meno | Dátum narodenia | číslo OP )"
                multiple
                item-color='primary'
                outlined
                dense
              >
              </v-combobox>
              </div>

              <v-combobox
                v-model="selectedRoomsCombobox"
                :items="roomsA"
                :item-text="item => 'Číslo izby: ' + item.roomNumber + ' (Postelí: '+ item.bedsNum + ') | Typ: ' + item.roomCategory.type"
                item-value=id
                label="Vybrane izby (Číslo izby (Počet postelí) | Typ izby)"
                multiple
                item-color='primary'
                outlined
                dense
              >
              </v-combobox>


              <v-row>
                  <v-col
                    cols="4"
                    sm="2"

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
                        @input="datePickerStartStayChange()"
                        :min="selectedStartDate_min"
                      ></v-date-picker>
                    </v-menu>

                  </v-col>

                  <v-col
                    cols="4"
                    sm="2"

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
                        @input="datePickerEndStayChange()"
                        :min="selectedEndDate_min"
                      ></v-date-picker>
                    </v-menu>

                  </v-col>


                  <v-col
                    cols="2"
                    sm="2"
                  >
                    <v-select
                      v-model="selectedNewStayBoardType"
                      :items="newStaysBoardTypeEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ pobytu"
                    ></v-select>
                  </v-col>

                  <v-col
                    cols="2"
                    sm="2"
                  >
                    <v-select
                      v-model="selectedNewStayPayment"
                      :items="newStayPaymentEnum"
                      :item-text="item => item.fe"
                      item-value=be
                      label="Typ platby"
                    ></v-select>
                  </v-col>
              </v-row>



              <v-col class="text-right">
                <v-btn
                  color="primary"
                  class="mr-4"
                  @click="createNewStay()"
                >
                  Vytvotiť pobyt
                </v-btn>
              </v-col>
            </v-form>
          </template>
        </v-container>
      </v-card-text>
    </v-card>
</template>


<script>
import List from "@/components/receptionist/services/List";
import {mapState} from "vuex";
import moment from "moment";

export default {
  components: {List},
  data: () => ({

    //##########################################
    //               DATEPICKER
    //##########################################
    datePickerBirthStay: false,

    selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    datePickerStartStay: false,

    selectedEndDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    datePickerEndStay: false,

    calendarPicker: false,

    //##########################################
    //               NEW HOST
    //##########################################
    newHostFormName: '',
    newHostFormSurname: '',
    newHostFormID: '',
    newHostFormBirthDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    newHostFormEmail: '',
    newHostFormPhoneNumber: '',
    newHostFormAddress: '',

    newHostFormBirthDate_max: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),

    selectedStartDate_min: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    selectedEndDate_min: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),

    //##########################################
    // be = backend interpretation
    // fe = frontend interpretation
    // We need create structure because there is no enumerator table for it
    //##########################################
    selectedNewStayBoardType: "",
    newStaysBoardTypeEnum: [
      { be: 'HALFBOARD', fe: 'polpenzia' },
      { be: 'FULLBOARD', fe: 'plná penzia' }
    ],

    selectedNewStayPayment: "",
    newStayPaymentEnum: [
      { be: 'CARD', fe: 'kartou' },
      { be: 'CASH', fe: 'hotovostne' }
    ],


    //##########################################
    //
    //##########################################
    selectedHostsCombobox: [],
    selectedRoomsCombobox: [],

    formNewHost: false,
    validFormNewHost: true,
    valid: true,
    select: null,
    numberRules: [],

    hostCombobox:null,

    //TODO naplnit availableRoomArray
    availableRoomArray: [],
  }),

  computed: {
    ...mapState({
      hosts: (state) => state.hosts.items,
      rooms: (state) => state.rooms.items,
      roomCategories: (state) => state.roomCategories.items,
      roomsA: (state) => state.roomsA.items,
    }),
  },

  created() {
    this.getData(this.selectedStartDate, this.selectedEndDate);
  },

  methods: {

    //##########################################
    //               FORM TOOLS
    //##########################################

    validate_addStayForm () {
      this.$refs.addStayForm.validate()
    },
    reset_addStayForm () {
      this.$refs.addStayForm.reset()
    },
    resetValidation_addStayForm () {
      this.$refs.addStayForm.resetValidation()
    },

    //NewHost form tools
    validate_newHostForm () {
      this.$refs.formNewHost.validate()
    },
    reset_newHostForm () {
      this.$refs.formNewHost.reset()
    },
    resetValidation_newHostForm () {
      this.$refs.formNewHost.resetValidation()
    },

    //##########################################
    //               FORM CALL
    //##########################################

    createNewStay(){

      this.roomCategory = {
        cost_per_bed: 44.25,
        equipment: "posteľ, nočný stolík, skriňa, písací stôl, zrkadlo a vešiaková stena, kúpelňa, TV, telefón, WIFI",
        id: 1,
        type: "STANDARD"
      }

      this.newRoom = {
        bedsNum: 2,
        id:1,
        roomCategory: this.roomCategory,
        roomNumber:1,
        state: "AVAILABLE"
      }

      //Pretoze indexuje mesiace od 0!!
      let selectedStartDateMoment = moment(this.selectedStartDate).add(1, 'month').toArray()
      let selectedEndDateMoment = moment(this.selectedEndDate).add(1, 'month').toArray()

      selectedStartDateMoment.length = 3
      selectedEndDateMoment.length = 3

      this.newStay = {
        accommodatedNumber: this.selectedHostsCombobox.length,
        boardType: this.selectedNewStayBoardType,
        dateFrom: selectedStartDateMoment,
        dateTo: selectedEndDateMoment,
        hosts: this.selectedHostsCombobox.splice(1),
        paymentType: this.selectedNewStayPayment,
        state: "RESERVED",
        rooms: this.selectedRoomsCombobox,
        stayCreator: this.selectedHostsCombobox[0]
      }

      this.db_createStay(this.newStay)

    },

    async createNewHost() {
      this.formNewHost = false

      this.newUser = {
        firstName: this.newHostFormName,
        lastName: this.newHostFormSurname,
        address: this.newHostFormAddress,
        dateOfBirth: this.newHostFormBirthDate,
        email: this.newHostFormEmail,
        idNumber: this.newHostFormID,
        phoneNumber: this.newHostFormPhoneNumber,
      };


      //Create new host
      await this.db_createHost(this.newUser)

      //Update data on page
      await this.getHostData();

      // re-init form
      this.reset_newHostForm()
      this.resetValidation_newHostForm()
    },

    cancelNewHost(){
      this.formNewHost = false
      this.reset_newHostForm()
      this.resetValidation_newHostForm()
    },


    //##########################################
    //                API CALL
    //##########################################

    async getData(from = '', to = '') {
      await Promise.all([await this.getAllRoomsApi()]);
      await Promise.all([await this.getAllRoomCategoriesApi()]);
      await Promise.all([await this.getHostData()]);
      if (from !== '') {
        await Promise.all([await this.getAvailableRoomsApi(from, to)]);
      }
      this.isLoading = false;
    },


    async getAllRoomCategoriesApi() {
      try {
        await this.$store.dispatch("roomCategories/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    async getAllRoomsApi() {
      try {
        await this.$store.dispatch("rooms/getAll");
      } catch (error) {
        console.error(error);
      }
    },


    async getAvailableRoomsApi(from, to) {
      try {
        return await this.$store.dispatch("roomsA/getAvailableRooms",{from, to});
      } catch (error) {
        console.error(error);
      }
    },


    // API call to create new stay in stay table
    async db_createStay(data) {
      try {
        await this.$store.dispatch("stays/create", data);
      } catch (error) {
        console.error(error);
      }
    },

    // API call to create new host in hostdo table
    async db_createHost(data) {
      try {
        await this.$store.dispatch("hosts/create", data);
      } catch (error) {
        console.error(error);
      }
    },

    // Ziska data z tabulky HOSTDO
    async getHostData() {
      // TODO: add getAllRoles()
      await Promise.all([await this.getAllHosts()]);
      this.isLoading = false;
    },

    // Ziska data z tabulky HOSTDO
    async getAllHosts() {
      try {
        await this.$store.dispatch("hosts/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    datePickerStartStayChange(){

      if (moment(this.selectedStartDate).isAfter(this.selectedEndDate)){
        //Start date nemoze byt vacsi ako End date
        //Ak je vacsi, zmenime End date na rovnaky ako Start Date
        this.selectedEndDate = this.selectedStartDate
      }

      this.getData(this.selectedStartDate, this.selectedEndDate)
      this.datePickerStartStay = false
    },

    datePickerEndStayChange(){
      this.getData(this.selectedStartDate, this.selectedEndDate)

      if (moment(this.selectedEndDate).isBefore(this.selectedStartDate)){
        //End date nemoze byt mensi ako Start date
        //Ak je mensi, zmenime Start date na rovnaky ako End Date
        this.selectedStartDate = this.selectedEndDate
      }

      this.datePickerEndStay = false
    },

    async getAvailableRooms() {

      await this.getAvailableRoomsApi(this.selectedStartDate, this.selectedEndDate)

      return this.roomsA
    }
  },


}
</script>
