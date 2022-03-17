<template>
  <v-app dark>
    <v-app-bar fixed app dark color="primary">
      <v-toolbar-title v-text="title" />
      <v-spacer />
    </v-app-bar>
    <v-main>
      <v-container fill-height>
        <Nuxt />
      </v-container>
    </v-main>
    <v-footer :absolute="!fixed" app dark color="primary">
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: "DefaultLayout",
  data() {
    return {
      fixed: false,
      title: "Hotel PIS",
    };
  },
};
</script>
