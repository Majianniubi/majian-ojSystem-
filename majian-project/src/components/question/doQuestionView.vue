<template>
  <div id="doQuestionView">
    <a-row class="grid-demo" :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question?.title">
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig?.timeLimit }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig?.stackLimit }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig?.memoryLimit }}
                </a-descriptions-item>
              </a-descriptions>
              <MdViewer :value="question.content || ''" />
              <template #extra>
                <a-tag
                  v-for="(tag, index) of question.tags"
                  :key="index"
                  color="green"
                  style="margin-right: 10px"
                  >{{ tag }}
                </a-tag>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="content" title="评论">
            <doCommentView :questionId="props.id"></doCommentView>
          </a-tab-pane>
          <a-tab-pane key="answer">
            <template #title>答案</template>
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <div class="mask" v-if="visible">
          <a-spin tip="提交代码中" />
        </div>
        <a-card title="代码" style="margin-top: 57px">
          <CodeEditor
            :value="questionSubmit.code"
            :language="questionSubmit.language"
            :handle-change="codeChange"
          />
          <template #extra>
            <a-select
              style="width: 150px"
              placeholder="请选择代码语言"
              v-model="questionSubmit.language"
            >
              <a-option>java</a-option>
              <a-option>cpp</a-option>
              <a-option>go</a-option>
            </a-select>
            <a-button type="primary" @click="codeSubmit">提交</a-button>
          </template>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { withDefaults, defineProps } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
} from "../../../generated";
import CodeEditor from "@/layouts/CodeEditor.vue";
import message from "@arco-design/web-vue/es/message";
import MdViewer from "@/layouts/MdViewer.vue";
import DoCommentView from "@/components/question/doCommentView.vue";

const route = useRoute();

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});
const question = ref();
onMounted(async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error({
      content: "数据错误" + res.message,
      duration: 600,
    });
  }
});
const questionSubmit = ref<QuestionSubmitAddRequest>({
  language: "java",
  code:
    "public class MainApplication {\n" +
    "    public static void main(String[] args) {\n" +
    "    }\n" +
    "}",
});
const codeChange = (v: string) => {
  questionSubmit.value.code = v;
};
const codeSubmit = async () => {
  visible.value = true;
  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...questionSubmit.value,
    questionId: props.id as any,
  });
  if (res.code === 0) {
    visible.value = false;
    message.success({
      content: "提交成功",
      duration: 600,
    });
  } else {
    message.error({
      content: "提交失败" + res.message,
      duration: 600,
    });
  }
};
const visible = ref(false);
</script>

<style scoped>
.mask {
  z-index: 4;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50%;
  height: 80%;
  bottom: 20px;
  background-color: rgba(0, 0, 0, 0.4);
}
</style>
