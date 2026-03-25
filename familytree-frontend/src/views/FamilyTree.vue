<template>
  <div class="family-tree-page">
    <div class="page-header">
      <h1>家族树</h1>
      <el-button @click="$router.push(`/family/${route.params.id}`)">返回家族详情</el-button>
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
    .attr('r', 10)

  node.append('text')
    .attr('dy', -16)
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
#tree-container {
  background: white;
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-surface);
  overflow: auto;
}
</style>

<style>
.link {
  fill: none;
  stroke: #8b7355;
  stroke-width: 2px;
}

.node circle {
  fill: #c4a97d;
  stroke: #8b7355;
  stroke-width: 2px;
}

.node text {
  font-size: 14px;
  font-weight: 600;
  font-family: "Noto Serif SC", "Source Han Serif SC", "SimSun", serif;
}
</style>
