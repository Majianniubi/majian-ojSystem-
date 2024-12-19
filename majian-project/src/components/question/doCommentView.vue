<template>
  <div id="commentView">
    <a-comment align="right">
      <template #avatar>
        <a-avatar>
          <img alt="avatar" src="@/assets/OIP-C.jpg" />
        </a-avatar>
      </template>
      <template #content>
        <div
          class="sendComment"
          style="
            font-size: 20px;
            text-indent: 2px;
            background-color: white;
            padding: 20px;
            border-radius: 20px;
            margin-bottom: 20px;
          "
        >
          <a-textarea
            auto-size
            placeholder="发表评论吧"
            style="border-radius: 10px; min-height: 100px; margin-bottom: 10px"
            v-model="addComment.content"
          />
          <a-space>
            <a-trigger
              trigger="click"
              :unmount-on-close="true"
              position="bottom"
              :popup-translate="[100, 20]"
            >
              <img
                src="@/assets/表情.png"
                @click="1 === 1"
                style="max-height: 30px; max-width: 30px"
              />表情
              <template #content>
                <div class="demo-basic">
                  <EmojiPicker
                    :native="true"
                    @select="onVue3EmojiPicker"
                    class="emoji"
                    style="min-height: 400px; min-width: 400px"
                  />
                </div>
              </template>
            </a-trigger>
            <a-button
              type="primary"
              shape="round"
              @click="sendComment"
              style="font-size: 15px; min-width: 80px; margin-left: 25px"
              >发表评论
            </a-button>
          </a-space>
        </div>
      </template>
    </a-comment>
    <div class="list-choice">
      <a-select
        :style="{ width: '150px' }"
        placeholder="选择排序方式"
        v-model="getCondition.sortField"
        @change="handleChange"
      >
        <a-option value="commentThumb">点赞量</a-option>
        <a-option value="createTime">创建时间</a-option>
      </a-select>
    </div>
    <div class="comment" v-if="items.length !== 0">
      <a-comment
        v-for="(item, index) in items"
        :key="index"
        :author="item.userVO?.userName"
        :datetime="item.createTime.split('.')[0].replace('T', '-')"
        align="right"
        style="margin-bottom: 20px"
      >
        <template #actions>
          <span class="action" key="heart" @click="onLikeChange(index, item)">
            <span v-if="like === index">
              <IconHeartFill :style="{ color: '#f53f3f' }" />
            </span>
            <span v-else>
              <IconHeart />
            </span>
            {{ item.commentThumb }}
          </span>
        </template>
        <template #content>
          <div>{{ item.content }}</div>
        </template>
        <template #avatar>
          <a-avatar>
            <img alt="avatar" src="@/assets/OIP-C.jpg" />
          </a-avatar>
        </template>
      </a-comment>
    </div>
    <div class="empty" v-else>
      <a-empty>暂时还没有人评论</a-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, onMounted, reactive, withDefaults } from "vue";
import WebURL from "@/constant/WebURL";
import { useStore } from "vuex";
import EmojiPicker from "vue3-emoji-picker";
import "vue3-emoji-picker/css";

const items = ref([]);
const store = useStore();

interface Props {
  questionId: number;
}

const props = withDefaults(defineProps<Props>(), {
  questionId: () => 0,
});
const myMessage = reactive({
  questionId: props.questionId,
  content: "",
});
const myWebSocket = ref();
onMounted(() => {
  connectWebsocket();
  myWebSocket.value.onmessage = (event: any) => {
    const message = JSON.parse(event.data);
    console.log(message);
    if (message.questionId === props.questionId) {
      loadComment();
    }
  };
  myWebSocket.value.onclose = (event: any) => {
    if (event.wasClean) {
      console.log(
        `WebSocket disconnected cleanly: ${event.code} - ${event.reason}`
      );
    } else {
      // 这里可能是由于网络故障、数据传输错误等原因导致连接非正常关闭
      console.log(
        `WebSocket disconnected uncleanly: ${event.code} - ${event.reason}`
      );
    }
  };
  loadComment();
});
const handleOnlineStatusChange = () => {
  if (navigator.onLine) {
    window.location.reload();
  } else {
    myWebSocket.value.close;
  }
};
window.addEventListener("online", handleOnlineStatusChange);
window.addEventListener("offline", handleOnlineStatusChange);
const getCondition = reactive({
  questionId: props.questionId as number,
  sortField: "commentThumb",
  sortOrder: "",
});
const loadComment = async () => {
  const res = await CommentControllerService.getByConditionUsingPost(
    getCondition
  );
  if (res.code === 0) {
    items.value = res.data;
  }
};
const connectWebsocket = () => {
  myWebSocket.value = new WebSocket(WebURL + "/sc");
  myWebSocket.value.onopen = () => {
    console.log("connect");
    store.dispatch("websocket/doUpdateWebSocket", myWebSocket.value);
  };
};
import { ref } from "vue";
import { IconHeart, IconHeartFill } from "@arco-design/web-vue/es/icon";
import { CommentControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const like = ref();
const onLikeChange = (index: any, item: any) => {
  item.commentThumb = parseInt(item.commentThumb) + 1;
  addCommentThumb(item);
  like.value = index;
  setTimeout(() => {
    like.value = -1;
  }, 300);
};

const addCommentThumb = async (item: any) => {
  const res = await CommentControllerService.updateCommentUsingPost({
    id: item.id,
    commentThumb: item.commentThumb,
  });
  if (res.code === 0 && myWebSocket.value) {
    myMessage.content = "点赞";
    myWebSocket.value.send(JSON.stringify(myMessage));
  } else {
    message.error({
      content: "点赞失败" + res.message,
      duration: 600,
    });
  }
};
const handleChange = () => {
  loadComment();
};
const onVue3EmojiPicker = (emoji: any) => {
  addComment.content = addComment.content + emoji.i;
};

const addComment = reactive({
  content: "",
  questionId: props.questionId,
  userId: store.state?.user?.loginUser?.id,
});
const sendComment = async () => {
  const res = await CommentControllerService.addCommentUsingPost(addComment);
  myMessage.content = "发表评论";
  if (res.code === 0) {
    myWebSocket.value.send(JSON.stringify(myMessage));
  } else {
    message.error({
      content: "发表失败" + res.message,
      duration: 600,
    });
  }
};
</script>

<style scoped>
#commentView {
  position: relative;
  max-width: 700px;
  max-height: 750px;
  margin: 0 auto;
  overflow-y: scroll;
  scrollbar-width: none;
}

.action {
  display: inline-block;
  padding: 0 4px;
  color: var(--color-text-1);
  line-height: 24px;
  background: transparent;
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.1s ease;
}

.list-choice {
  text-align: right;
  margin-bottom: 20px;
}

.action:hover {
  background: var(--color-fill-3);
}
</style>
