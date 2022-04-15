<template>
  <v-form v-model="valid" @submit.prevent="login">
    <v-card>
      <v-card-title>Prihlásenie do systému </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="username"
          label="Používateľské meno:"
          :rules="[rules.required]"
        />
        <v-text-field
          v-model="password"
          name="name"
          label="Heslo:"
          :type="showPassword ? 'text' : 'password'"
          :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
          :rules="[rules.required]"
          @click:append="showPassword = !showPassword"
        />
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" type="submit">Prihlásiť sa</v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      username: "",
      password: "",
      showPassword: false,
      rules: {
        required: (value) => !!value || "Povinný údaj",
      },
    };
  },
  methods: {
    login() {
      if (!this.valid) {
        try {
          const loginData = {
            login: this.username,
            password: this.password,
          };
          // TODO: erase temporary login
          if (loginData.login === "admin") {
            let userData = {
              id: 1,
              login: "admin",
              email: "admin@gmail.com",
              roles: [
                {
                  id: 2,
                  name: "ROLE_ADMIN",
                },
              ],
            };
            this.$auth.$storage.setUniversal("user", userData, true);
            this.$auth.setUser(userData);
            this.$toast.success("Úspešne prihlásený");
            this.$router.push("admin/users");
          } else if (loginData.login === "rec") {
            let userData = {
              id: 1,
              login: "rec",
              email: "rec@gmail.com",
              roles: [
                {
                  id: 1,
                  name: "ROLE_USER",
                },
              ],
            };
            this.$auth.$storage.setUniversal("user", userData, true);
            this.$auth.setUser(userData);
            this.$toast.success("Úspešne prihlásený");
            this.$router.push("receptionist/stays");
          } else {
            this.$toast.error("Wrong login: admin or rec");
          }

          // this.$auth.loginWith("local", { loginData })
          // .then((response) => {
          //   this.$auth.$storage.setUniversal("user", response.data, true);
          //   this.$auth.setUser(response.data);
          //   this.$auth.setUserToken(
          //     response.data.token,
          //     response.data.refresh_token
          //   );
          //   this.$toast.success("Úspešne prihlásený");
          // })
          // .catch((e) => {
          //   console.error(e);
          // });
        } catch (err) {
          throw new Error(`API ${err}`);
        }
      }
    },
  },
};
</script>

<style></style>
