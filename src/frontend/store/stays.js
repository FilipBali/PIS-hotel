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
      .get(`/api/stay`)
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
    return this.$axios.post(`/api/stay`, data).catch((error) => {
      throw new Error(`API ${error}`);
    });
  },
  update(_, { id, data }) {
    return this.$axios
      .put(`/api/stay/${id}`, data)
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        throw new Error(`API ${error}`);
      });
  },
  delete(_, id) {
    return this.$axios.delete(`/api/stay/${id}`).catch((error) => {
      throw new Error(`API ${error}`);
    });
  },
};
