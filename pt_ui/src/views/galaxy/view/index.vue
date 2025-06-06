<template>
  <resource-viewer v-if="torrentId" :torrentId="torrentId" />
</template>

<script>
import ResourceViewer from "@/components/ResourceView"
export default {
  components: { ResourceViewer },
  data() {
    return {
      torrentId: null
    }
  },
  mounted() {
    this.torrentId = this.$route.params.torrentId
  }
}
</script>
