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
import jwt_decode from "jwt-decode";

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
      if (this.valid) {
        try {
          const loginData = new FormData();
          loginData.append("email", this.username);
          loginData.append("password", this.password);

          // TODO: erase temporary login
          // if (loginData.login === "admin") {
          //   let userData = {
          //     id: 1,
          //     login: "admin",
          //     email: "admin@gmail.com",
          //     roles: [
          //       {
          //         id: 2,
          //         name: "ROLE_ADMIN",
          //       },
          //     ],
          //   };
          //   this.$auth.$storage.setUniversal("user", userData, true);
          //   this.$auth.setUser(userData);
          //   this.$toast.success("Úspešne prihlásený");
          //   this.$router.push("admin/users");
          // } else if (loginData.login === "rec") {
          //   let userData = {
          //     id: 1,
          //     login: "rec",
          //     email: "rec@gmail.com",
          //     roles: [
          //       {
          //         id: 1,
          //         name: "ROLE_USER",
          //       },
          //     ],
          //   };
          //   this.$auth.$storage.setUniversal("user", userData, true);
          //   this.$auth.setUser(userData);
          //   this.$toast.success("Úspešne prihlásený");
          //   this.$router.push("reception");
          // } else {
          //   this.$toast.error("Wrong login: admin or rec");
          // }
          this.$auth
            .loginWith("local", { data: loginData })
            .then((response) => {
              this.$auth.setUserToken(
                response.data.access_token,
                response.data.refresh_token
              );
              this.$auth.$storage.setUniversal(
                "user",
                jwt_decode(response.data.access_token)
              );

              this.$toast.success("Úspešne prihlásený");

              // var decoded = jwt_decode(response.data.access_token);
              // let role = decoded.roles[0];

              // if (role === "ROLE_ADMIN") {
              //   this.$router.push("admin/users");
              // } else if (role === "ROLE_RECEPTIONIST") {
              //   this.$router.push("reception");
              // } else if (role === "ROLE_COOK") {
              //   this.$router.push("cook");
              // } else if (role === "ROLE_MASSEUR") {
              //   this.$router.push("masseur");
              // } else if (role === "ROLE_BOWLING") {
              //   this.$router.push("bowling");
              // } else {
              //   console.log("Stay here.");
              // }
            })
            .catch((e) => {
              this.$toast.error("Nesprávne prihlasovacie údaje");
              console.error(e);
            });
        } catch (err) {
          throw new Error(`API ${err}`);
        }
      }
    },
  },
};
</script>

<style></style>
