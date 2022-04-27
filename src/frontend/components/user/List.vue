<template>
  <v-card>
    <v-card-title primary-title>
      Vitaj <v-spacer></v-spacer>

      <v-spacer></v-spacer>

    </v-card-title>

    <v-card-text>
      <v-data-table :headers="headers" :items="stays" :search="search" :custom-filter="customSearch" flat>


      </v-data-table>
    </v-card-text>
  </v-card>
</template>
<script>

import { mapState } from "vuex";
import moment from "moment";

export default {
  data() {
    return{



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

    async getData() {
      await Promise.all([await this.getAllUsersApi()]);
      this.isLoading = false;
    },

    async getAllUsersApi() {
      try {
        await this.$store.dispatch("users/getAll");
      } catch (error) {
        console.error(error);
      }
    },

  },
};
</script>
