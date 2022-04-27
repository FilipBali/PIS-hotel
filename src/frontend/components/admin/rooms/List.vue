<template>
  <v-card>
    <v-card-title primary-title>
      Zoznam izieb
      <v-spacer></v-spacer
      ><v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Hľadať"
        single-line
        hide-details
      ></v-text-field>
      <v-btn class="ml-8" color="secondary" @click="newRoom">
        Pridať izbu
      </v-btn>
    </v-card-title>
    <v-card-text>
      <v-data-table :headers="headers" :items="rooms" :search="search" flat>
        <template v-slot:item.state="{ item }">
          {{ roomState(item.state) }}</template
        >
        <template v-slot:item.roomCategory.type="{ item }">
          {{ roomType(item.roomCategory.type) }}</template
        >
        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="editRoom(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteUser(item.id)"> mdi-delete </v-icon>
        </template>
      </v-data-table>
      <admin-rooms-edit-dialog
        :isNewRoom="newRoomDialog"
        :dialog="dialogController"
        :room="dialogRoom"
        @close-dialog="closeDialog"
      ></admin-rooms-edit-dialog>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapState } from "vuex";

export default {

  data() {
    return {
      isLoading: true,
      search: "",
      dialogController: false,
      dialogRoom: {
        roomNumber: "",
        bedsNum: "",
        state: "",
        roomCategory: null,
      },
      newRoomDialog: false,

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
    }),
  },
  created() {
    this.getData();
  },
  methods: {
    async getData() {
      await Promise.all([this.getAllRoomsApi(), this.getAllRolesApi()]);
      this.isLoading = false;
    },

    async getAllRoomsApi() {
      try {
        await this.$store.dispatch("rooms/getAll");
      } catch (error) {
        console.error(error);
      }
    },
    async deleteUserApi(id) {
      try {
        await this.$store.dispatch("rooms/delete", id);
      } catch (error) {
        console.error(error);
      }
    },
    async getAllRolesApi() {
      try {
        await this.$store.dispatch("roomCathegories/getAll");
      } catch (error) {
        console.error(error);
      }
    },

    async deleteUser(id) {
      await this.deleteUserApi(id);
      this.getAllRoomsApi();
    },

    newRoom() {
      this.dialogController = true;
      this.newRoomDialog = true;
      this.dialogRoom = {
        roomNumber: "",
        bedsNum: "",
        state: "",
        roomCategory: null,
      };
    },
    editRoom(room) {
      this.dialogController = true;
      this.newRoomDialog = false;
      this.dialogRoom = JSON.parse(JSON.stringify(room)); // deepcopy
    },
    closeDialog(val) {
      this.dialogController = val;
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
    roomState(state) {
      switch (state) {
        case "AVAILABLE":
          return "dostupná";
        case "OCCUPIED":
          return "obsadená";
        case "UNAVAILABLE":
          return "nedostupná";
        case "RESERVED":
          return "rezervovaná";
        default:
          return "";
      }
    },
  },
};
</script>
