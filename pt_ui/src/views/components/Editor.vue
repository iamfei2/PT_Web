<template>
  <div>
    <el-toolbar v-if="showToolbar" :editor="editorRef" />
    <div ref="editor" style="border: 1px solid #dcdfe6; border-radius: 4px"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps, defineEmits } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const props = defineProps({
  modelValue: String,
  showToolbar: { type: Boolean, default: true }
})
const emit = defineEmits(['update:modelValue'])

const editorRef = ref()
const editor = ref(null)

watch(() => props.modelValue, (newValue) => {
  if (editor.value && newValue !== editor.value.getHtml()) {
    editor.value.setHtml(newValue || '')
  }
})

onMounted(() => {
  editor.value = new Editor({
    selector: '#editor',
    html: props.modelValue || '',
    config: {
      placeholder: '请输入内容...',
      MENU_CONF: {}
    },
    onChange(editor) {
      emit('update:modelValue', editor.getHtml())
    }
  })

  // 初始化内容
  emit('update:modelValue', editor.value.getHtml())
})
</script>
