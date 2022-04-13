<template>
    <v-card>
      <v-card-title>
        {{ "Formulár pre nový pobyt" }}
      </v-card-title>
      <v-card-text>
        <v-container fluid>

          <template>
            <v-form
              ref="form"
              v-model="valid"
              lazy-validation
            >

              <!------------>
              <!-- POP-UP ADD NEW HOSTS -->
              <!------------>
                <template>
                  <div>
                    <v-row
                      justify="center"
                    >
                      <!--    BUTTONS    -->
                      <v-btn
                        color="primary"
                        class="ma-2"
                        dark
                        @click="dialog = true"
                      >
                        Pridať hostí
                      </v-btn>

                      <v-dialog
                        v-model="dialog"
                        fullscreen
                        hide-overlay
                        transition="dialog-bottom-transition"
                        scrollable
                      >
                        <v-card tile>
                          <v-toolbar
                            flat
                            dark
                            color="primary"
                          >

                            <!-- X button newHosts-->
                            <v-btn
                              icon
                              dark
                              @click="dialog = false"
                            >
                              <v-icon>mdi-close</v-icon>
                            </v-btn>

                            <v-toolbar-title>Formulár pre pridanie hostí</v-toolbar-title>

                            <v-spacer></v-spacer>

                            <!-- sa button newHosts-->
                            <v-toolbar-items>
                              <v-btn
                                dark
                                text
                                @click="dialog = false"
                              >
                                Uložiť
                              </v-btn>
                            </v-toolbar-items>
                          </v-toolbar>
                          <v-card-text>

                            <!--DEBUG-->
                            <v-btn
                              color="primary"
                              dark
                              class="ma-2"
                              @click="getHostsCombobox()"
                            >
                              Zobraz combobox do console
                            </v-btn>

                            <template>
                              <v-row justify="center">
                                <v-dialog
                                  v-model="dialogNewHost"
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
                                        <v-row>
                                          <v-col
                                            cols="12"
                                            sm="6"
                                            md="6"
                                          >
                                            <v-text-field v-model="newHostFormName"
                                              label="Meno*"
                                              required
                                            ></v-text-field>
                                          </v-col>

                                          <v-col
                                            cols="12"
                                            sm="6"
                                            md="6"
                                          >
                                            <v-text-field v-model="newHostFormSurname"
                                              label="Priezvisko*"
                                              required
                                            ></v-text-field>
                                          </v-col>

                                          <v-col
                                            cols="6"
                                            sm="6"
                                            md="6"
                                          >
                                            <v-text-field v-model="newHostFormID"
                                              label="číslo OP*"
                                              required
                                            ></v-text-field>
                                          </v-col>

                                          <v-col
                                            cols="6"
                                            sm="6"
                                            md="6"
                                          >
                                            <v-menu
                                              ref="calendarPicker"
                                              v-model="calendarPicker"
                                              :close-on-content-click="false"
                                              :return-value.sync="newHostFormBirthDate"
                                              transition="scale-transition"
                                              offset-y
                                              min-width="auto"
                                            >
                                              <template v-slot:activator="{ on, attrs }">
                                                <v-text-field
                                                  v-model="newHostFormBirthDate"
                                                  label="Date"
                                                  prepend-icon="mdi-calendar"
                                                  readonly
                                                  v-bind="attrs"
                                                  v-on="on"
                                                ></v-text-field>
                                              </template>
                                              <v-date-picker
                                                v-model="newHostFormBirthDate"
                                                no-title
                                                scrollable
                                              >
                                                <v-spacer></v-spacer>
                                                <v-btn
                                                  text
                                                  color="primary"
                                                  @click="calendarPicker = false"
                                                >
                                                  Cancel
                                                </v-btn>
                                                <v-btn
                                                  text
                                                  color="primary"
                                                  @click="$refs.calendarPicker.save(date)"
                                                >
                                                  OK
                                                </v-btn>
                                              </v-date-picker>
                                            </v-menu>
                                          </v-col>


                                          <v-col cols="12">
                                            <v-text-field v-model="newHostFormAddress"
                                              label="Adresa*"
                                              required
                                            ></v-text-field>
                                          </v-col>
                                          <v-col cols="12">
                                            <v-text-field v-model="newHostFormEmail"
                                              label="E-mail*"
                                              required
                                            ></v-text-field>
                                          </v-col>
                                          <v-col cols="12">
                                            <v-text-field v-model="newHostFormPhoneNumber"
                                              label="Telefónne číslo*"
                                              required
                                            ></v-text-field>
                                          </v-col>
                                        </v-row>
                                      </v-container>

                                      <small>*povinné</small>

                                    </v-card-text>

                                    <!-- newHost footer -->
                                    <v-card-actions>
                                      <v-spacer></v-spacer>
                                      <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="dialogNewHost = false"
                                      >
                                        Zavrieť
                                      </v-btn>
                                      <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="createNewHost()"
                                      >
                                        Vytvoriť a pridať
                                      </v-btn>
                                    </v-card-actions>

                                  </v-card>
                                </v-dialog>
                              </v-row>
                            </template>

                            <!--combobox-->
                            <v-list>
                              <template>
                                <v-container fluid>
                                  <v-row>
                                    <v-col cols="6">
                                      <v-combobox
                                        v-model="selectedHostsCombobox"
                                        :items="hosts"
                                        :item-text="item => item.firstName +' '+ item.lastName  + ' | ' +item.idNumber"
                                        item-value=id
                                        label="Existujúci hostia ( Meno | Dátum narodenia | číslo OP )"
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
                          </v-card-text>

                          <div style="flex: 1 1 auto;"></div>
                        </v-card>
                      </v-dialog>

                    </v-row>
                  </div>
                </template>


              <v-select
                v-model="select"
                :items="availableRoomArray"
                :rules="[v => !!v || 'Item is required']"
                label="Cisla volných izieb."
                required
              ></v-select>

              <v-btn
                :disabled="!valid"
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

    dialogNewHost: false,
    dialog: false,

    valid: true,
    select: null,

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
      return this.selectedHostsCombobox
    },

    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
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

    createNewHost(){
      console.log(this.newHostFormName)
      this.dialogNewHost = false

      //TODO POST na BE a vytvorit hosta

      this.newUser = {
        firstName: this.newHostFormName,
        lastName: this.newHostFormSurname,
        address: this.newHostFormAddress,
        dateOfBirth: this.newHostFormBirthDate,
        email: this.newHostFormEmail,
        idNumber: this.newHostFormID,
        phoneNumber: this.newHostFormPhoneNumber,
      };

      console.log(this.newUser)

      this.createHostApi(this.newUser)

      this.newHostFormName = ''
      this.newHostFormSurname = ''
      this.newHostFormID = ''
      this.newHostFormBirthDate= (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)
      this.newHostFormEmail = ''
      this.newHostFormPhoneNumber = ''
      this.newHostFormAddress = ''
    },

  },
}
</script>
