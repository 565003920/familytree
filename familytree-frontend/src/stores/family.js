import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useFamilyStore = defineStore('family', () => {
  const currentFamily = ref(null)
  const members = ref([])
  const relations = ref([])

  const setCurrentFamily = (family) => {
    currentFamily.value = family
  }

  const loadMembers = (memberList) => {
    members.value = memberList
  }

  const loadRelations = (relationList) => {
    relations.value = relationList
  }

  return { currentFamily, members, relations, setCurrentFamily, loadMembers, loadRelations }
})
