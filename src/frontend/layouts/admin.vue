<template>
  <v-app dark>
    <v-app-bar fixed app dark color="primary">
      <v-toolbar-title v-text="title" />
      <v-spacer />
      <v-btn
        v-for="(item, i) in items"
        :key="i"
        min-height="100%"
        plain
        :to="item.to"
        ><b>{{ item.title }}</b></v-btn
      >
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
      <v-container fluid>
        <Nuxt />
      </v-container>
    </v-main>
    <v-footer :absolute="!fixed" app dark color="primary">
      <span> {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: "AdminLayout",
  data() {
    return {
      fixed: false,
      title: "Hotel PIS",
      items: [
        {
          title: "Užívatelia",
          to: "/admin/users",
        },
        {
          title: "Izby",
          to: "/admin/rooms",
        },
      ],
    };
  },
  methods: {
    async logout() {
      // TODO: update after logout api implementation
      // await this.$store.dispatch('user/logout', this.$auth.user.id)
      await this.$auth.logout();
      this.$auth.$storage.removeUniversal("user");
      this.$router.push("/login");
    },
    profile() {
      console.log("Ta ak bude, ta otvorime profila");
    },
  },
};
</script>
