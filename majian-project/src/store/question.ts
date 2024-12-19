import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  state: () => ({
    choiceQuestion: {},
  }),
  actions: {
    getQuestion({ state, commit }, payload) {
      commit("updateChoiceQuestion", payload);
    },
  },
  mutations: {
    updateChoiceQuestion(state, payload) {
      state.choiceQuestion = payload;
    },
  },
} as StoreOptions<any>;
