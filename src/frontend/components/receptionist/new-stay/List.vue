<template>
    <v-card>
      <v-card-title>
          {{ "Formulár pre nový pobyt" }}

        <v-spacer></v-spacer>

        <!--DEBUG BTN-->
        <v-btn
          color="primary"
          dark
          class="ma-2"
          @click="getHostsCombobox()"
        >
          Zobraz combobox do console
        </v-btn>


        <v-dialog
          v-model="formNewHost"
          persistent
          max-width="600px"
        >
          <!-- button newHost-->
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              dark
              v-bind="attrs"
              v-on="on"
            >
              Novy hosť
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
                  v-model="validFormNewHost"
                  ref="formNewHost">
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
                        v-model="datePickerStartStay"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        transition="scale-transition"
                        offset-y
                        required
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
                @click="formNewHost = false"
              >
                Zavrieť
              </v-btn>
              <v-btn
                :disabled="!validFormNewHost"
                color="blue darken-1"
                text
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
                div.v-select__selection:nth-child(1) {
                  /*text-decoration: underline;*/
                  color: #e74c3c;
                }
              </style>
              <v-combobox
                v-model="selectedHostsCombobox"
                :items="hosts"
                :item-text="item => item.firstName +' '+ item.lastName  + ' | ' +item.idNumber"
                item-value=id
                label="Existujúci hostia ( Meno | Dátum narodenia | číslo OP )"
                multiple
                item-color='primary'
                outlined
                dense
              >
              </v-combobox>


              <v-btn
                color="success"
                class="mr-4"
                @click="validate"
              >
                Validate
              </v-btn>

              <v-btn
                color="error"
                class="mr-4"
                @click="reset"
              >
                Reset Form
              </v-btn>

              <v-btn
                color="warning"
                @click="resetValidation"
              >
                Reset Validation
              </v-btn>
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

    selectedStartDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    datePickerStartStay: false,

    calendarPicker: false,

    // Premenne formularu pre noveho hosta
    newHostFormName: '',
    newHostFormSurname: '',
    newHostFormID: '',
    newHostFormBirthDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    newHostFormEmail: '',
    newHostFormPhoneNumber: '',
    newHostFormAddress: '',

    selectedHostsCombobox: [],

    formNewHost: false,

    validFormNewHost: true,
    valid: true,
    select: null,
    numberRules: [],

    //TODO naplnit availableRoomArray
    availableRoomArray: [],
  }),

  computed: {
    ...mapState({
      hosts: (state) => state.hosts.items,
    }),
  },

  created() {
    this.getHostData();
  },

  methods: {

    // Ziska ID vybranych uzivalov z comboboxu
    getHostsCombobox() {
      console.log(this.selectedHostsCombobox)
      return this.selectedHostsCombobox
    },


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

    // Preformatuje datum
    formatDate(date) {
      return moment(date).format("DD. MM. YYYY");
    },

    async createHostApi(data) {
      try {
        await this.$store.dispatch("hosts/create", data);
      } catch (error) {
        console.error(error);
      }
    },

    async createNewHost() {
      this.validate_newHostForm()

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

      await this.createHostApi(this.newUser)
      await this.getHostData();



      this.reset_newHostForm()
      this.resetValidation_newHostForm()
    },

  },
}
</script>
