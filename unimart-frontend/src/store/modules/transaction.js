import { 
  getMyTransactions, 
  payTransaction, 
  refundTransaction 
} from '@/api/transaction'

const state = {
  transactions: []
}

const mutations = {
  SET_TRANSACTIONS(state, transactions) {
    state.transactions = transactions
  }
}

const actions = {
  async fetchTransactions({ commit }) {
    try {
      const response = await getMyTransactions()
      commit('SET_TRANSACTIONS', response)
    } catch (error) {
      console.error('获取交易列表失败:', error)
      throw error
    }
  },
  async payTransaction({ dispatch }, transactionId) {
    try {
      await payTransaction(transactionId)
      dispatch('fetchTransactions')
    } catch (error) {
      console.error('支付失败:', error)
      throw error
    }
  },
  async refundTransaction({ dispatch }, transactionId) {
    try {
      await refundTransaction(transactionId)
      dispatch('fetchTransactions')
    } catch (error) {
      console.error('退款申请失败:', error)
      throw error
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 