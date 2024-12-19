<template>
  <div id="addQuestionView">
    <dvi>
      <h2>添加题目</h2>
    </dvi>
    <div>
      <a-form
        :model="form"
        auto-label-width
        @submit="handleSubmit"
        label-align="left"
      >
        <a-form-item field="title" label="题目" style="max-width: 500px">
          <a-input v-model="form.title" placeholder="请输入题目的标题" />
        </a-form-item>
        <a-form-item field="tag" label="标签" style="max-width: 500px">
          <a-input-tag v-model="form.tags" />
        </a-form-item>
        <a-form-item field="answer" label="答案" auto-label-width>
          <MdEditor :value="form.answer" :handle-change="onAnswerChange" />
        </a-form-item>
        <a-form-item field="content" label="题目内容" auto-label-width>
          <MdEditor :value="form.content" :handle-change="onContentChange" />
        </a-form-item>
        <a-form-item field="judgeCase" label="判断用例"></a-form-item>
        <a-form-item
          v-for="(data, index) of form.judgeCase"
          :label="`用例 ${index + 1}`"
          :key="index"
        >
          <a-space direction="vertical " style="min-width: 400px">
            <a-form-item
              field="judgeCase.input"
              label="输入用例"
              auto-label-width
            >
              <a-input v-model="data.input" placeholder="输入用例" />
            </a-form-item>
            <a-form-item
              field="judgeCase.output"
              label="输出用例"
              auto-label-width
            >
              <a-input v-model="data.output" placeholder="输出用例" />
            </a-form-item>
          </a-space>
          <a-button
            type="primary"
            shape="round"
            @click="handleDelete(index)"
            :style="{ marginLeft: '10px' }"
            >删除用例
          </a-button>
        </a-form-item>
        <div>
          <a-button
            @click="handleAdd"
            status="success"
            type="primary"
            shape="round"
            style="margin-bottom: 20px; margin-left: 200px"
            >添加用例
          </a-button>
        </div>
        <a-form-item field="判题配置" label="题目内容" auto-label-width>
          <a-space direction="vertical " style="min-width: 400px">
            <a-form-item
              field="judgeConfig.timeLimit"
              label="时间限制(单位:ms)"
              auto-label-width
            >
              <a-input-number
                v-model="form.judgeConfig.timeLimit"
                placeholder="请输入时间限制"
                mode="button"
                min="0"
                size="large"
              />
            </a-form-item>
            <a-form-item
              field="judgeConfig.stackLimit"
              label="堆栈限制(单位:kb)"
              auto-label-width
            >
              <a-input-number
                v-model="form.judgeConfig.stackLimit"
                placeholder="请输入堆栈限制"
                mode="button"
                min="0"
                size="large"
              />
            </a-form-item>
            <a-form-item
              field="judgeConfig.memoryLimit"
              label="内存限制(单位:kb)"
              auto-label-width
            >
              <a-input-number
                v-model="form.judgeConfig.memoryLimit"
                placeholder="请输入内存限制"
                mode="button"
                min="0"
                size="large"
              />
            </a-form-item>
          </a-space>
        </a-form-item>
        <a-form-item>
          <a-button
            html-type="submit"
            status="success"
            style="height: 50px; width: 300px"
            >提交题目
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/layouts/MdEditor.vue";
import { reactive } from "vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const form = reactive({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
});
const handleSubmit = async () => {
  const res = await QuestionControllerService.addQuestionUsingPost(form as any);
  if (res.code === 0) {
    message.success("提交成功" + res.message);
  } else {
    message.error("提交失败" + res.message);
  }
};
const handleAdd = () => {
  form.judgeCase.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index: number) => {
  form.judgeCase.splice(index, 1);
};
const onAnswerChange = (v: string) => {
  form.answer = v;
};
const onContentChange = (v: string) => {
  form.content = v;
};
</script>

<style scoped>
#addQuestionView {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
