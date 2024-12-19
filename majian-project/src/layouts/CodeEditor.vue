<template>
  <div class="code-editor" ref="codeEditorRef" style="min-height: 68.5vh"></div>
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, watch } from "vue";
import { defineProps, withDefaults } from "vue/dist/vue";

interface Props {
  value: string;
  language: string;
  handleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  language: () => "",
  handleChange: (v: string) => {
    console.log(v);
  },
});
const codeEditor = ref();
const codeEditorRef = ref();

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  const model = monaco.editor.createModel("language", props.language);
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: props.language,
    automaticLayout: true,
    colorDecorators: true,
    minimap: {
      enabled: true,
    },
    readOnly: false,
    theme: "vs-dark",
    // lineNumbers: "off",
    // roundedSelection: false,
    // scrollBeyondLastLine: false,
  });
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue() as string);
  });
});
watch(
  () => props.language,
  (newVal) => {
    monaco.editor.setModelLanguage(
      // 踩坑一定要使用toRaw
      toRaw(codeEditor.value).getModel(),
      newVal
    );
    if (newVal === "java") {
      toRaw(codeEditor!.value)
        .getModel()
        .setValue(
          "public class MainApplication {\n" +
            "    public static void main(String[] args) {\n" +
            "\n" +
            "    }\n" +
            "}"
        );
      return;
    }
    if (newVal === "cpp") {
      toRaw(codeEditor!.value)
        .getModel()
        .setValue("int main(){\n" + "    \n" + "}\n");
      return;
    }
  }
);
</script>

<style scoped></style>
