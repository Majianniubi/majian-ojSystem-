<template>
  <div class="mdEditor">
    <Editor
      :value="value"
      :mode="mode"
      :plugins="plugins"
      @change="handleChange"
    />
  </div>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { ref, defineProps, withDefaults } from "vue";

interface Props {
  mode?: string;
  value: string;
  handleChange: (v: string) => void;
}

const plugins = [gfm(), highlight()];
const props = withDefaults(defineProps<Props>(), {
  mode: () => "split",
  value: () => "",
  handleChange: (v: string) => {
    console.log("hello majian");
  },
});
</script>

<style scoped></style>
