import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  state: () => ({
    MyWebSocket: null,
  }),
  actions: {
    doUpdateWebSocket({ commit, state }, payload) {
      commit("updateMyWebSocket", payload);
    },
  },
  mutations: {
    updateMyWebSocket(state, payload) {
      state.MyWebSocket = payload;
    },
  },
} as StoreOptions<any>;
