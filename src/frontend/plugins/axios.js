// TODO: customize on our api

export default function ({ $axios, $toast }) {
  $axios.onResponse((res) => {
    if (res.data.errors) {
      if (Array.isArray(res.data.errors)) {
        res.data.errors.forEach((error) => {
          $toast.error(error.message);
        });
      } else {
        $toast.error(res.data.errors);
      }
      return Promise.reject(res.data.errors);
    }
  });

  $axios.onError((error) => {
    if (error.response && error.response.status) {
      const code = error.response.status;

      switch (code) {
        case 500:
          $toast.error(
            "Nepodarilo sa nám spracovať vašu požiadavku, prosím skúste to neskôr."
          );
          break;
        case 401:
          $toast.error("Musíte sa prihlásiť.");
          break;
        case 403:
          if (error.response.config.url.split("/").at(-1) !== "change-password")
            $toast.error("Nesprávne prihlasovacie údaje.");
          break;
        default:
          break;
      }
    }
  });
}
