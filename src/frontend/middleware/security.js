export default function ({ route, $auth, app }) {
  const user = $auth.$storage.getUniversal("user");
  if (user) {
    $auth.setUser(user);
  }

  console.log("middleware");
  console.log(user);

  if (route.path === "/") {
    console.log("in / path");
    if ($auth.loggedIn) {
      console.log("loggedInnnn");
      if (user.roles.includes("ROLE_ADMIN")) {
        app.router.push("/admin/users");
      } else {
        app.router.push("/receptions");
      }
    } else {
      app.router.push("/login");
    }
  } else {
    // else if z TODO nizsie
  }

  // if ($auth.loggedIn) {
  //   if (route.path === "/") {
  //     if (user.roles.includes("ROLE_ADMIN")) {
  //       app.router.push("/admin/users");
  //     } else {
  //       app.router.push("/reception");
  //     }
  //   } else {
  //   }
  // } else {
  //   // if (route.path !== "/login") app.router.push("/login");
  // }

  // if (route.path === "/") {
  //   if (!user || user.id == null || user.roles.length < 1 || !$auth.loggedIn) {
  //     app.router.push("/login");
  //     return;
  //   }
  //   if (user.roles.includes("ROLE_ADMIN")) {
  //     app.router.push("/admin/users");
  //   } else {
  //     app.router.push("/introduction");
  //   }
  // }
  // TODO: asi ochranuje admin routu, budem musiet pridat
  // if (route.path.includes("admin")) {
  //   if (!user || user.roles.length < 1 || !user.roles.includes("ROLE_ADMIN")) {
  //     app.router.push("/");
  //   }
  // }
  // if (route.name === 'admin-auctions-new') {
  //   app.store.commit('auction/INIT_VALUES')
  // }
  // if (route.name === 'admin-auction-orders-new') {
  //   app.store.commit('auctionOrder/INIT')
  // }
}
