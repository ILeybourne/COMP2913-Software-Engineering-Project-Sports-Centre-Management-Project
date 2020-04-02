const state = {
  loading: 0,
  block_load: true
};

const getters = {
  loading: state => state.loading > 0,
  blocking: state => state.block_load
};

const mutations = {
  START_LOADING: state => state.loading++,
  FINISH_LOADING: state => state.loading--,
  ENABLE_LOAD: state => (state.block_load = false)
};

const namespaced = true;

const loader = {
  namespaced,
  state,
  getters,
  mutations
};

export default loader;
