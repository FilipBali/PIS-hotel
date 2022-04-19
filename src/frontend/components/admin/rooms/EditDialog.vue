<template>
  <v-dialog v-model="dialogController" max-width="800px" @click:outside="close">
    <v-card>
      <v-card-title>
        {{ isNewRoom ? "Pridať užívateľa" : "Editácia užívateľa" }}
      </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="validForm" @submit.prevent="save()">
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <v-text-field
                v-model="room.roomNumber"
                :rules="roomNumRules"
                label="Číslo izby"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-text-field
                v-model="room.bedsNum"
                :rules="bedsNumRules"
                label="Počet lôžok"
                required
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
              <v-select
                v-model="selectedRoomCat"
                :items="roomCathegories"
                :item-text="(item) => item.type"
                label="Typ izby"
                return-object
              ></v-select>
            </v-col>
          </v-row>
        </v-form>
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
      selectedState: this.isNewRoom
        ? "dostupná"
        : this.roomEnum(this.room.state),
      stateTypes: ["dostupná", "obsadená", "nedostupná"],
      selectedRoomCat: this.room.state,

      validForm: true,
      roomNumRules: [
        (v) => !!v || "Zadajte číslo izby",
        (v) => !isNaN(v) || "Číslo izby musí byť číslo",
      ],
      bedsNumRules: [
        (v) => !!v || "Zadajte počet postelí",
        (v) => !isNaN(v) || "Počet postelí musí byť číslo",
      ],
    };
  },
  mounted() {
    console.log(this.room);
  },
  computed: {
    ...mapState({
      roomCathegories: (state) => state.roomCathegories.items,
    }),
  },
  watch: {
    dialog() {
      this.dialogController = this.dialog;
    },
    room() {
      this.selectedState = this.isNewRoom
        ? "dostupná"
        : this.roomEnum(this.room.state);
      this.selectedRoomCat = this.room.roomCategory;
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
      let pRoom = {
        roomNumber: parseInt(room.roomNumber),
        bedsNum: parseInt(room.bedsNum),
        state: this.roomState(this.selectedState),
        roomCategory: this.selectedRoomCat,
      };
      return pRoom;
    },

    async save() {
      await this.$refs.form.validate();

      if (this.validForm) {
        if (this.isNewRoom) {
          await this.createRoom();
        } else {
          await this.updateRoom();
        }

        await this.getAllRooms();
        this.close();
      }
    },
    close() {
      this.dialogController = false;
      this.selectedState = "dostupná";
      this.selectedRoomCat = "";
      this.$refs.form.resetValidation();
      this.$emit("close-dialog", this.dialogController);
    },

    roomEnum(state) {
      switch (state) {
        case "AVAILABLE":
          return "dostupná";
        case "OCCUPIED":
          return "obsadená";
        case "UNAVAILABLE":
          return "nedostupná";
        default:
          return "";
      }
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
