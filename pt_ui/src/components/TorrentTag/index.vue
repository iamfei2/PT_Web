<template>
  <div v-if="torrent">
    <el-button v-for="(v, k) in torrent.tags" :key="'tag_' + k" :size="size"
               :style="{marginRight: '5px', color: colors[k%colors.length]}"
               plain @click="handleTagClick(v)">{{ v }}</el-button>
  </div>
</template>

<script>
export default {
  name: 'TorrentTag',
  props: {
    size: {
      type: String,
      default: 'small'
    },
    torrent: {
      type: Object,
      default: null
    },
    limit: {
      type: Number,
      default: 0
    }
  },
  computed: {
    tagList () {
      if (this.limit === 0) return torrent.tags
      return torrent.tags.slice(0, this.limit)
    }
  },
  data () {
    return {
      colors: ['#3498db','#9b59b6','#34495e','#16a085','#1abc9c','#27ae60','#2980b9','#8e44ad','#2c3e50','#2ecc71','#f1c40f','#e67e22','#e74c3c', '#ecf0f1','#95a5a6','#f39c12','#d35400','#c0392b','#bdc3c7','#7f8c8d']
    }
  },
  methods: {
    handleTagClick (tag) {
      this.$router.push('/detail/tag/' + tag)
    }
  }
}
</script>
