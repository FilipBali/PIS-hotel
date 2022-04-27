<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam izieb <v-spacer></v-spacer
    ><v-text-field
      v-model="search"
      append-icon="mdi-magnify"
      label="Hľadať"
      single-line
      hide-details
    ></v-text-field>

      <v-btn class="ml-8" color="secondary" @click="roomNewFormBtn()">
        Pridať izbu
      </v-btn>

    </v-card-title>
    <v-card-text>
      <v-data-table :headers="headers" :items="rooms" :search="search" :custom-filter="customSearch" flat>

        <template v-slot:item.state="{ item }">
          {{ roomState(item.state) }}</template>

        <template v-slot:item.roomCategory.type="{ item }">
          {{ roomType(item.roomCategory.type) }}</template>

        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="roomEditFormBtn(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteRoom(item.id)"> mdi-delete </v-icon>
        </template>

      </v-data-table>


      <!--                -->
      <!-- NEW ROOM FORM  -->
      <!--                -->
      <v-dialog
        v-model="newRoomDialog"
        max-width="600px"
      >
        <v-card>
            <v-card-title>
              <span class="text-h5">Vytvoriť izbu</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-form
                    ref="roomNewFormRef"
                    v-model="roomNewForm"
                    persistent
                    max-width="600px"
                >
                  <v-row>

                      <v-col
                        cols="12"
                        sm="6"
                      >
                        <v-text-field
                          v-model="roomNumber"
                          :rules="[v => /\d+/.test(v) || 'Item is required']"
                          label="Číslo izby"
                          required
                        ></v-text-field>
                      </v-col>

                      <v-col
                        cols="12"
                        sm="6"
                      >
                        <v-select
                          v-model="numBeds"
                          :items="numBedsItems"
                          label="Počet lôžok"
                          :rules="[v => !!v || 'Item is required']"
                          required
                        ></v-select>
                      </v-col>


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
                          :rules="[v => !!v || 'Item is required']"
                          required
                        ></v-select>
                      </v-col>

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
                          :rules="[v => !!v || 'Item is required']"
                          required
                        ></v-select>
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
                  @click="roomNewFormCancel()"
                >
                  Zatvoriť
                </v-btn>
                <v-btn
                  :disabled="!roomNewForm"
                  depressed
                  color="primary"
                  @click="roomNewFormSave()"
                >
                  Uložiť
                </v-btn>
            </v-card-actions>
        </v-card>
      </v-dialog>


      <!--                 -->
      <!-- EDIT ROOM FORM  -->
      <!--                 -->
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
                      <v-text-field
                        v-model="selectedRoomNumberEdit"
                        :rules="[v => /\d+/.test(v) || 'Item is required']"
                        label="Číslo izby"
                        required>
                      </v-text-field>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedNumBedsEdit"
                        :items="numBedsItems"
                        label="Počet lôžok"
                        required>
                      </v-select>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedRoomStateEdit"
                        :items="roomStateEnum"
                        :item-text="item => item.fe"
                        item-value=be
                        label="Stav izby"
                      ></v-select>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                    >
                      <v-select
                        v-model="selectedRoomTypeEdit"
                        :items="roomCategories"
                        :item-text="item => roomType(item.type)"
                        item-value=id
                        label="Typ izby"
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


        <receptionist-rooms-edit-dialog
        :isNewRoom="newRoomDialog"
        :dialog="dialogController"
        :room="dialogRoom"
        @close-dialog="closeDialog"
      ></receptionist-rooms-edit-dialog>
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
      search: "",
      dialogController: false,
      dialogRoom: {},
      newRoomDialog: false,

      roomEditForm: false,

      roomNumber: '',
      selectedRoomNumberEdit: '',
      numBeds: 0,
      selectedNumBedsEdit: 0,

      numBedsItems: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],

      roomNewForm:false,
      selectedRoomState: "",
      roomStateEnum: [
        { be: 'AVAILABLE', fe: 'dostupná' },
        //{ be: 'OCCUPIED', fe: 'obsadená' },
        //{ be: 'RESERVED', fe: 'rezervovaná' },
        { be: 'UNAVAILABLE', fe: 'nedostupná' },
      ],
      selectedRoomType: "",

      selectedRoomTypeEdit: "",
      selectedRoomStateEdit: "",

      //##########################################
      //         STORAGE/CACHE/REFERENCES
      //##########################################
      roomEditFormItem: [],


      headers: [
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
        { text: "Stav", align: "start", value: "state" },
        {
          text: "Typ izby",
          align: "start",
          value: "roomCategory.type",
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
      rooms: (state) => state.rooms.items,
      roomCategories: (state) => state.roomCategories.items,
    }),
  },
  created() {
    this.getData();
  },
  methods: {
    async getData() {
      await Promise.all([await this.getAllRoomsApi()]);
      await Promise.all([await this.getAllRoomCategoriesApi()]);
      this.isLoading = false;
    },

    customSearch (value, search, item)
    {

      let acceptedRoomTypeStr =
        [
          'štandardná', 'apartment', 'luxusná',
          'standardna', 'standardná', 'štandardna', 'luxusna'
        ]

      let acceptedRoomsStateStr =
        [
          'dostupná', 'obsadená', 'rezervovaná', 'nedostupná',
          'dostupna', 'obsadena', 'rezervovana', 'nedostupna'
        ]


      if (item.id.toString() === search){
        return true;
      }

      if (item.bedsNum.toString() === search){
        return true;
      }


      for (let i = 0; i < acceptedRoomTypeStr.length; i++) {
        if (acceptedRoomTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

          let ret = this.roomType(item.roomCategory.type)

          if (ret !== "" &&
            ret.toLowerCase().startsWith(search.toLowerCase())){
            return true;
          }
        }
      }

      for (let i = 0; i < acceptedRoomsStateStr.length; i++) {
        if (acceptedRoomsStateStr[i].toLowerCase().startsWith(search.toLowerCase())){

          let ret = this.roomState(item.state)

          if (ret !== "" &&
            ret.toLowerCase().startsWith(search.toLowerCase())){
            return true;
          }
        }
      }
      return false;
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
    async deleteRoomsApi(id) {
      try {
        await this.$store.dispatch("rooms/delete", id);
      } catch (error) {
        console.error(error);
      }
    },

    async deleteRoom(id) {
      await this.deleteRoomsApi(id);
      this.getAllRoomsApi();
    },

    // newRoom() {
    //   // todo: doplnit room type
    //   this.dialogController = true;
    //   this.newRoomDialog = true;
    //   this.dialogRoom = {
    //     roomNumber: "",
    //     bedsNum: "",
    //     state: "",
    //   };
    // },
    editUser(room) {
      this.dialogController = true;
      this.newRoomDialog = false;
      this.dialogRoom = JSON.parse(JSON.stringify(room)); // deepcopy
    },
    closeDialog(val) {
      this.dialogController = val;
    },

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

    roomType(state) {
      switch (state) {
        case "STANDARD":
          return "štandardná";
        case "APARTMENT":
          return "apartmán";
        case "LUXURY":
          return "luxusná";
        default:
          return "";
      }
    },


    roomNewFormBtn(){
        this.newRoomDialog = true
    },

    roomNewFormSave(){

        console.log(this.selectedRoomType)
        let roomCategory
        for(let i = 0; i < this.roomCategories.length; i++ ){

          if (this.roomCategories[i].id === this.selectedRoomType){
            roomCategory = this.roomCategories[i];
            break;
          }
        }

        this.newRoom = {
          roomNumber: this.roomNumber,
          state: this.selectedRoomState,
          bedsNum: this.numBeds,
          roomCategory: roomCategory,
        }

        console.log(this.newRoom)
        this.db_createRoom(this.newRoom)
        this.newRoomDialog = false

        this.reset_newRoomForm()
        this.resetValidation_newRoomForm()

    },

    reset_newRoomForm() {
      this.$refs.roomNewFormRef.reset()
    },
    resetValidation_newRoomForm() {
      this.$refs.roomNewFormRef.resetValidation()
    },

    roomNewFormCancel(){
        this.newRoomDialog = false
        this.reset_newRoomForm()
        this.resetValidation_newRoomForm()
    },

    async db_createRoom(data) {
        await this.db_createRoomApi(data)
        await this.getData()

    },

    async db_createRoomApi(data) {
      try {
        await this.$store.dispatch("rooms/create", data);
      } catch (error) {
        console.error(error);
      }
    },


    roomEditFormBtn(item){
      console.log(item)
      this.roomEditFormItem = item

      this.selectedRoomNumberEdit = item.roomNumber
      this.selectedNumBedsEdit = item.bedsNum
      this.selectedRoomStateEdit = item.state
      this.selectedRoomTypeEdit = this.roomTypeToRoomID(item.roomCategory.type)

      this.roomEditForm = true;
    },


    roomEditFormSave(){
      let item = this.roomEditFormItem;
      // console.log(item);
      // console.log(this.selectedRoomTypeEdit);

      let roomCategory = item.roomCategory;
      if(item.roomCategory.id !== this.selectedRoomTypeEdit){

        for(let i = 0; i < this.roomCategories.length; i++ ){

          if (this.roomCategories[i].id === this.selectedRoomTypeEdit){
            roomCategory = this.roomCategories[i];
            break;
          }
        }
      }


      this.editedRoom = {
        id: item.id,
        roomNumber: this.selectedRoomNumberEdit,
        state: this.selectedRoomStateEdit,
        bedsNum: this.selectedNumBedsEdit,
        roomCategory: roomCategory
      }

      this.editRoom(this.editedRoom)
      this.roomEditForm = false;

    },

    roomEditFormCancel(){
      //  TODO reset form

      this.roomEditForm = false;
    },


    async editRoom(data) {
      await this.editRoomApi(data.id, data)
      await this.getData()

      // this.updateRoomTable()
    },

    async editRoomApi(id, data) {
      try {
        await this.$store.dispatch("rooms/update", {id, data});
      } catch (error) {
        console.error("error");
        console.error(error);
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

    roomTypeToRoomID(state) {
      switch (state) {
        case "STANDARD":
        case "štandardná":
          return 1;
        case "APARTMENT":
        case "apartmán":
          return 2;
        case "LUXURY":
        case "luxusná":
          return 3;
        default:
          return "";
      }
    },

  },
};
</script>
