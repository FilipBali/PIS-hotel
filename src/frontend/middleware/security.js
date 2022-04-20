export default function ({ route, $auth, app }) {
  const user = $auth.$storage.getUniversal("user");
  if (user) {
    $auth.setUser(user);
  }

  if (route.path === "/") {
    if ($auth.loggedIn) {
      if (user.roles.includes("ROLE_ADMIN")) {
        app.router.push("/admin/users");
      } else if (user.roles.includes("ROLE_RECEPTIONIST")) {
        app.router.push("/receptionist");
      } else if (user.roles.includes("ROLE_COOK")) {
        app.router.push("/chef");
      } else if (user.roles.includes("ROLE_MASSEUR")) {
        app.router.push("/masseur");
      } else if (user.roles.includes("ROLE_BOWLING")) {
        app.router.push("/bowling");
      } else {
        app.router.push("/user/profile");
      }
    } else {
      app.router.push("/login");
    }
  } else {
    if (route.path.includes("admin")) {
      if (
        !user ||
        user.roles.length < 1 ||
        !user.roles.includes("ROLE_ADMIN")
      ) {
        app.router.push("/");
      }
    } else if (route.path.includes("receptionist")) {
      if (
        !user ||
        user.roles.length < 1 ||
        !user.roles.includes("ROLE_RECEPTIONIST")
      ) {
        app.router.push("/");
      }
    } else if (route.path.includes("chef")) {
      if (!user || user.roles.length < 1 || !user.roles.includes("ROLE_COOK")) {
        app.router.push("/");
      }
    } else if (route.path.includes("masseur")) {
      if (
        !user ||
        user.roles.length < 1 ||
        !user.roles.includes("ROLE_MASSEUR")
      ) {
        app.router.push("/");
      }
    } else if (route.path.includes("bowling")) {
      if (
        !user ||
        user.roles.length < 1 ||
        !user.roles.includes("ROLE_BOWLING")
      ) {
        app.router.push("/");
      }
    }
  }
}
