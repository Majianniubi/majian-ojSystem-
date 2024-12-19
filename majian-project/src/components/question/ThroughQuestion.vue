<template>
  <div id="throughQuestion">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input
          v-model="searchParams.title"
          placeholder="请输入名称"
          @input="doSearch"
        />
      </a-form-item>
      <a-form-item field="tag" label="标签" style="min-width: 240px">
        <a-input-tag
          v-model="searchParams.tags"
          placeholder="请输入标签"
          colcr="green"
          @keyup="doSearch"
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
      <template #optional="{ record }">
        <a-space>
          <a-button status="warning" @click="doQuestion(record)">做题</a-button>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ record.createTime.split("T")[0] }}
      </template>
      <template #acceptedNum="{ record }">
        {{
          record.acceptedNum === 0
            ? 0
            : record.submitNum / record.acceptedNum + "%"
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
  QuestionQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedNum",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
const dataList = ref([]);
const searchParams = ref<QuestionQueryRequest>({
  tags: [],
  title: "",
  pageSize: 10,
  current: 1,
});
const total = ref(0);
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
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
const doQuestion = (record: Question) => {
  router.push({
    path: `do/question/${record.id}`,
  });
};
</script>

<style scoped>
#throughQuestion {
  max-width: 1480px;
  margin: 0 auto;
}
</style>
