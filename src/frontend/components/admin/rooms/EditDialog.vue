<template>
  <v-dialog v-model="dialogController" max-width="800px">
    <v-card>
      <v-card-title>
        {{ isNewRoom ? "Pridať užívateľa" : "Editácia užívateľa" }}
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <v-text-field
                v-model="room.roomNumber"
                label="Číslo izby"
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-text-field
                v-model="room.bedsNum"
                label="Počet lôžok"
              ></v-text-field>
            </v-col>
            <v-col>
              <v-select
                v-model="selectedState"
                :items="stateTypes"
                label="Stav"
              ></v-select>
            </v-col>
            <v-col>
              <!-- <v-select
                  v-model="selectedRoomType"
                  :items="roomTypes"
                  label="Typ izby"
                  :rules="[rules.required]"
                ></v-select> -->
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="secondary" text @click="close"> Zrušiť </v-btn>
        <v-btn color="secondary" text @click="save"> Uložiť </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";
import DatePicker from "../../DatePicker.vue";

export default {
  components: { DatePicker },
  props: ["dialog", "room", "isNewRoom"],
  data() {
    return {
      dialogController: this.dialog,
      selectedState: "dostupná",
      stateTypes: ["dostupná", "obsadená", "nedostupná"],
      // selectedRoomType: '',
      // roomTypes: roles.forEach(role => role[0].name),
    };
  },
  computed: {
    ...mapState({
      roles: (state) => state.roles.items,
    }),
  },
  watch: {
    dialog() {
      this.dialogController = this.dialog;
    },
  },
  methods: {
    async createRoom() {
      try {
        await this.$store.dispatch("rooms/create", this.prepare(this.room));
      } catch (error) {
        console.error(error);
      }
    },
    async updateRoom() {
      try {
        console.log(this.room);
        await this.$store.dispatch("rooms/update", {
          id: this.room.id,
          data: this.prepare(this.room),
        });
      } catch (error) {
        console.error(error);
      }
    },
    async getAllRooms() {
      try {
        await this.$store.dispatch("rooms/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    prepare(room) {
      this.room.roomNumber = parseInt(room.roomNumber);
      this.room.bedsNum = parseInt(room.bedsNum);
      this.room.state = this.roomState(this.selectedState);

      return room;
    },

    async save() {
      if (this.isNewRoom) {
        await this.createRoom();
      } else {
        await this.updateRoom();
      }
      await this.getAllRooms();
      this.close();
    },
    close() {
      this.dialogController = false;
      this.$emit("close-dialog", this.dialogController);
    },

    roomState(state) {
      switch (state) {
        case "dostupná":
          return "AVAILABLE";
        case "obsadená":
          return "OCCUPIED";
        case "nedostupná":
          return "UNAVAILABLE";
        default:
          return "";
      }
    },
  },
};
</script>
