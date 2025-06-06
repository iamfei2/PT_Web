import { listCategory } from "@/api/galaxy/category";

const state = {
  categories: []
}

const mutations = {
  SET_CATEGORIES: (state, categories) => {
    state.categories = categories
  }
}

const actions = {
  GetCategories ({commit, state}) {
    listCategory({pageSize: 999999}).then(res => {
      commit('SET_CATEGORIES', res.data)
      // resolve(res.rows)
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
