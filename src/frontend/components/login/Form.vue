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
