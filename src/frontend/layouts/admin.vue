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
      <v-avatar color="secondary" size="40">{{ this.initials }}</v-avatar>
      <v-btn plain min-height="100%" @click="profile">
        {{ this.userName }}
      </v-btn>
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
        {
          title: "Pridať pobyt",
          to: "/admin/new-stay",
        },
        {
          title: "Pobyty",
          to: "/admin/stays",
        },
        {
          title: "Izby",
          to: "/admin/rooms",
        },
        {
          title: "Hostia",
          to: "/admin/hosts",
        },
        {
          title: "Služby pre hostí",
          to: "/admin/services",
        },
      ],
    };
  },
  computed: {
    actUser() {
      return this.$auth.$storage.getUniversal("user");
    },
    initials() {
      let initials = "";

      if (this.actUser.firstName.length > 0) {
        initials += this.actUser.firstName[0].toUpperCase();
      }
      if (this.actUser.lastName.length > 0) {
        initials += this.actUser.lastName[0].toUpperCase();
      }

      return initials;
    },
    userName() {
      return this.actUser.firstName + " " + this.actUser.lastName;
    },
  },
  methods: {
    async logout() {
      await this.$auth.logout();
      this.$auth.$storage.removeUniversal("user");
      this.$router.push("/login");
    },
    profile() {
      this.$router.push("/admin/profile");
    },
  },
};
</script>
