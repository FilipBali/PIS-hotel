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
      .get(`/api/role`)
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
};
