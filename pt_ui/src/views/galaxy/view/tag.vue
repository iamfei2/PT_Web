<template>
  <torrent-list ref="torrentList" :query-params="queryParams"/>
</template>
<script>
import TorrentList from "@/components/TorrentList"
export default {
  components: { TorrentList },
  data () {
    return {
      tag: null
    }
  },
  computed: {
    queryParams () {
      return {
        pageNum: 1,
        pageSize: 20,
        tags: [this.$route.params.tag],
        orderByColumn: 'updateTime',
        isAsc: 'desc'
      }
    }
  }
}
</script>
