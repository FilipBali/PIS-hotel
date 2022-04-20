export const state = () => ({
  items: [],
});

export const mutations = {
  SET_ITEMS(state, items) {
    state.items = items;
    console.log("///")
  },
};

export const actions = {
  getAvailableRooms({ commit }, {from, to}) {
    return this.$axios
      .get(`/api/room/available-rooms?from=${from}&to=${to}`)
      .then((response) => {
        if (response.data) {
          commit('SET_ITEMS', response.data)
          return response.data
        }
      })
      .catch((error) => {
        throw new Error(`API ${error}`)
      })
  },


};
