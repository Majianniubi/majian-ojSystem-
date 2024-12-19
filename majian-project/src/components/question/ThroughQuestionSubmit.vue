<template>
  <div id="throughQuestionSubmit">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="题号" style="min-width: 240px">
        <a-input
          v-model="searchParams.questionId"
          placeholder="请输入题目id"
          @input="doSearch"
        />
      </a-form-item>
      <a-form-item field="tag" label="语言" style="min-width: 240px">
        <a-input
          v-model="searchParams.language"
          placeholder="请输入题目语言"
          @input="doSearch"
        />
      </a-form-item>
      <a-form-item field="tag" style="min-width: 240px">
        <a-button type="primary" shape="round" @click="doSearch">搜素</a-button>
      </a-form-item>
    </a-form>
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <a-space>
          <a-tag
            v-for="(color, index) of record.tags"
            :key="index"
            color="green"
            >{{ color }}
          </a-tag>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ record.createTime.split("T")[0] }}
      </template>
      <template #judgeInfo="{ record }">
        {{ JSON.stringify(record.judgeInfo) }}
      </template>
      <template #status="{ record }">
        {{
          record.status === 2
            ? "判题完成"
            : record.status === 0
            ? "待判题"
            : record.status === 1
            ? "判题中"
            : record.status === 3
            ? "判题失败"
            : record.status
        }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionSubmitQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "语言",
    dataIndex: "language",
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
  },
  {
    title: "判题状态",
    slotName: "status",
  },
  {
    title: "题目ID",
    dataIndex: "questionId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
];
const dataList = ref([]);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: "",
  pageSize: 10,
  current: 1,
});
const total = ref(0);
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    {
      ...searchParams.value,
      sortOrder: "descend",
      sortField: "createTime",
    }
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  }
  return res;
};
onMounted(async () => {
  const res = await loadData();
  if (res.code !== 0) {
    message.success({
      content: "加载数据失败" + res.message,
      duration: 800,
    });
  }
});
const router = useRouter();
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
watchEffect(() => {
  loadData();
});
const debounce = (func: any, wait: number) => {
  let lastTimer = 0;
  return function (...args: any) {
    const now = new Date().getTime();
    if (now - lastTimer >= wait) {
      setTimeout(() => func.apply(this, args), wait);
      lastTimer = now;
    }
  };
};
const search = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
const doSearch = debounce(search, 300);
</script>

<style scoped>
#throughQuestionSubmit {
  max-width: 1480px;
  margin: 0 auto;
}
</style>
