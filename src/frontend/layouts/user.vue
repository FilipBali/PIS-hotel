<template>
  <v-app dark>
    <v-app-bar fixed app dark color="primary">
      <v-toolbar-title v-text="title" />
      <v-spacer />
      <v-avatar color="secondary" size="40">JH</v-avatar>
      <v-btn plain min-height="100%" @click="profile"> Janko Hrasko </v-btn>
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon plain v-bind="attrs" v-on="on" @click="logout">
            <v-icon>mdi-logout</v-icon>
          </v-btn>
        </template>
        <span>Odhlásiť sa</span>
      </v-tooltip>
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
  name: "UserLayout",
  data() {
    return {
      fixed: false,
      title: "Hotel PIS",
    };
  },
  methods: {
    async logout() {
      await this.$auth.logout();
      this.$auth.$storage.removeUniversal("user");
      this.$router.push("/login");
    },
    profile() {
      this.$router.push("/user/profile");
    },
  },
};
</script>
