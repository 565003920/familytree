<template>
  <div class="family-tree-page">
    <div class="header">
      <h1>家族树</h1>
      <el-button @click="$router.back()">返回</el-button>
    </div>
    <div id="tree-container" ref="treeContainer"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTreeData } from '../api/relations'
import * as d3 from 'd3'

const route = useRoute()
const treeContainer = ref(null)

onMounted(async () => {
  const { data } = await getTreeData(route.params.id)
  renderTree(data.relations)
})

const renderTree = (members) => {
  const width = 1200
  const height = 800

  const svg = d3.select('#tree-container')
    .append('svg')
    .attr('width', width)
    .attr('height', height)
    .append('g')
    .attr('transform', 'translate(100,50)')

  const root = buildHierarchy(members)
  const treeLayout = d3.tree().size([height - 100, width - 200])
  const treeData = treeLayout(d3.hierarchy(root))

  svg.selectAll('.link')
    .data(treeData.links())
    .enter()
    .append('path')
    .attr('class', 'link')
    .attr('d', d3.linkHorizontal()
      .x(d => d.y)
      .y(d => d.x))

  const node = svg.selectAll('.node')
    .data(treeData.descendants())
    .enter()
    .append('g')
    .attr('class', 'node')
    .attr('transform', d => `translate(${d.y},${d.x})`)

  node.append('circle')
    .attr('r', 6)

  node.append('text')
    .attr('dy', -10)
    .text(d => d.data.name)
}

const buildHierarchy = (members) => {
  const root = members.find(m => !m.fatherId && !m.motherId) || members[0]
  if (!root) return { name: '暂无数据', children: [] }

  const buildNode = (member) => {
    const children = members.filter(m => m.fatherId === member.id || m.motherId === member.id)
    return {
      name: member.name,
      children: children.map(buildNode)
    }
  }

  return buildNode(root)
}
</script>

<style scoped>
.family-tree-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.header {
  max-width: 1200px;
  margin: 0 auto 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h1 {
  font-size: 36px;
  font-weight: 700;
  color: white;
  margin: 0;
}

#tree-container {
  background: white;
  border-radius: 16px;
  max-width: 1200px;
  margin: 0 auto;
  overflow: auto;
}
</style>

<style>
.link {
  fill: none;
  stroke: #667eea;
  stroke-width: 2px;
}

.node circle {
  fill: #764ba2;
  stroke: #667eea;
  stroke-width: 2px;
}

.node text {
  font-size: 14px;
  font-weight: 600;
}
</style>
