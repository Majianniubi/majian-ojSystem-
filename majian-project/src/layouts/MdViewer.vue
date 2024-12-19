<template>
  <div class="mdEditor">
    <Viewer :value="value" :plugins="plugins" style="min-height: 60vh" />
  </div>
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { ref, defineProps, withDefaults } from "vue";

interface Props {
  value: string;
}

const plugins = [gfm(), highlight()];
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
});
</script>

<style scoped></style>
