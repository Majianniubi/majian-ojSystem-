import { createStore } from "vuex";
import user from "./user";
import question from "./question";
import websocket from "./websocket";

export default createStore({
  mutations: {},
  actions: {},
  modules: {
    user,
    question,
    websocket,
  },
});
