export const state = () => ({
  items: [],
});

export const mutations = {
  SET_ITEMS(state, items) {
    state.items = items;
  },
};

export const actions = {
  getAll({ commit }) {
    return this.$axios
      .get(`/api/user`)
      .then((response) => {
        if (response.data) {
          commit("SET_ITEMS", response.data);
          return response.data;
        }
      })
      .catch((error) => {
        throw new Error(`API ${error}`);
      });
  },
  getById({ commit }, id) {
    return this.$axios
      .get(`/api/user/${id}`)
      .then((response) => {
        if (response.data) {
          commit("SET_ITEMS", response.data);
          return response.data;
        }
      })
      .catch((error) => {
        throw new Error(`API ${error}`);
      });
  },
  create(_, data) {
    return this.$axios.post(`/api/user`, data).catch((error) => {
      throw new Error(`API ${error}`);
    });
  },
  update(_, { id, data }) {
    return this.$axios.put(`/api/user/${id}`, data).catch((error) => {
      throw new Error(`API ${error}`);
    });
  },
  delete(_, id) {
    return this.$axios.delete(`/api/user/${id}`).catch((error) => {
      throw new Error(`API ${error}`);
    });
  },
  // TODO: update api string
  // logout(_, id) {
  //   return this.$axios
  //     .patch(`/api/users/${id}/logout`, {})
  //     .then((response) => {
  //       return response;
  //     })
  //     .catch((error) => {
  //       throw new Error(`API ${error}`);
  //     });
  // },
};
