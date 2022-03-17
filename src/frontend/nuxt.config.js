import colors from "vuetify/es5/util/colors";
import en from "vuetify/lib/locale/en";
import sk from "vuetify/lib/locale/sk";

export default {
  ssr: false,

  head: {
    titleTemplate: "%s - frontend",
    title: "frontend",
    htmlAttrs: {
      lang: "en",
    },
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      { hid: "description", name: "description", content: "" },
      { name: "format-detection", content: "telephone=no" },
    ],
    link: [{ rel: "icon", type: "image/x-icon", href: "/favicon.ico" }],
  },

  css: ["~/assets/styles.scss"],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: ["~/plugins/axios"],

  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    "@nuxtjs/vuetify",
    // "@nuxtjs/eslint-module",
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: ["@nuxtjs/axios", "@nuxtjs/auth-next", "@nuxtjs/toast"],

  // Axios module configuration (https://go.nuxtjs.dev/config-axios)
  axios: {
    baseURL: "http://localhost:8080/",
  },
  publicRuntimeConfig: {
    axios: {
      browserBaseURL: process.env.BROWSER_BASE_URL,
    },
  },
  privateRuntimeConfig: {
    axios: {
      baseURL: process.env.BASE_URL,
    },
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ["~/assets/variables.scss"],
    theme: {
      dark: false,
      themes: {
        light: {
          primary: "#2c3e50",
          accent: colors.grey.darken3,
          secondary: "#e74c3c",
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3,
        },
        dark: {
          primary: "#2c3e50",
          accent: colors.grey.darken3,
          secondary: "#e74c3c",
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3,
        },
      },
    },
    lang: {
      locales: { en, sk },
      current: "sk",
    },
  },

  auth: {
    redirect: {
      login: "/login",
      logout: "/",
      callback: "/",
      home: "/",
    },
    strategies: {
      local: {
        scheme: "refresh",
        token: {
          property: "token",
          maxAge: 60 * 10,
          type: "Bearer",
        },
        refreshToken: {
          property: "refresh_token",
          data: "refresh_token",
          maxAge: 60 * 60 * 24,
        },
        user: {
          property: false,
          autoFetch: false,
        },
        endpoints: {
          login: { url: "api/user/login", method: "post" },
          refresh: { url: "api/user/token/refresh", method: "post" },
          user: false,
          logout: false,
        },
        // autoLogout: false
      },
    },
  },

  toast: {
    position: "top-center",
    containerClass: "toast-container",
    className: "toast",
    duration: 4000,
  },

  // eslint: {},

  router: {
    middleware: ["security"],
    base: "/",
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {},
};
