<template>
  <div id="manageQuestion">
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
      <template #optional="{ record }">
        <a-space>
          <a-button type="dashed" status="success" @click="Delete(record)"
            >删除
          </a-button>
          <a-button status="warning" @click="Update(record)">更新</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const columns = [
  {
    title: "ID",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "创题人Id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
const dataList = ref([]);
const searchParams = ref({
  pageSize: 10,
  current: 1,
});
const total = ref(0);
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
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
      content: "加载数据失败",
      duration: 800,
    });
  }
});
const Delete = async (record: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: record.id,
  });
  if (res.code === 0) {
    message.success({
      content: "加删除数据失败" + res.message,
      duration: 600,
    });
    loadData();
  }
};
const router = useRouter();
const store = useStore();
const Update = (record: Question) => {
  store.dispatch("question/getQuestion", record);
  router.push({
    path: "/update/question",
  });
};
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
#manageQuestion {
  max-width: 1420px;
  margin: 0 auto;
}
</style>
