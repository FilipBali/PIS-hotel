<template>
  <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    :return-value.sync="isoDate"
    transition="scale-transition"
    offset-y
    min-width="auto"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="skDate"
        :label="label"
        prepend-icon="mdi-calendar"
        readonly
        v-bind="attrs"
        v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker v-model="isoDate" no-title scrollable>
      <v-spacer></v-spacer>
      <v-btn text color="primary" @click="menu = false"> Zrušiť </v-btn>
      <v-btn text color="primary" @click="saveDate"> OK </v-btn>
    </v-date-picker>
  </v-menu>
</template>

<script>
import moment from "moment";

export default {
  props: {
    date: {
      type: String,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      menu: false,
      modal: false,
      isoDate: this.date,
    };
  },
  computed: {
    skDate() {
      return moment(this.isoDate).format("DD. MM. YYYY");
    },
  },
  methods: {
    saveDate() {
      this.$refs.menu.save(this.isoDate);
      this.$emit("updated-date", this.isoDate);
    },
  },
};
</script>
