<template>
  <div class="ai-assistant-wrapper">
    <!-- 浮动按钮 -->
    <transition name="bounce">
      <div
        v-if="!isOpen"
        class="ai-float-btn"
        @click="toggleChat"
        :class="{ 'ai-pulse': !isOpen }"
      >
        <div class="ai-btn-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z" />
            <line x1="9" y1="10" x2="15" y2="10" />
            <line x1="12" y1="7" x2="12" y2="13" />
          </svg>
        </div>
        <span class="ai-btn-badge">AI助手</span>
      </div>
    </transition>

    <!-- 聊天窗口 -->
    <transition name="chat-slide">
      <div v-if="isOpen" class="ai-chat-panel">
        <!-- 头部 -->
        <div class="ai-header">
          <div class="ai-header-left">
            <div class="ai-avatar">
              <span>🤖</span>
            </div>
            <div class="ai-header-info">
              <h3>社团团购助手</h3>
              <span class="ai-status-dot"></span>
              <span class="ai-status-text">Qwen-Plus 在线</span>
            </div>
          </div>
          <div class="ai-header-actions">
            <button class="ai-icon-btn" @click="clearChat" title="清空对话">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3 6 5 6 21 6" /><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
              </svg>
            </button>
            <button class="ai-icon-btn ai-close-btn" @click="toggleChat" title="关闭">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
        </div>

        <!-- 消息区域 -->
        <div ref="messageContainer" class="ai-messages">
          <!-- 欢迎消息 -->
          <div v-if="messages.length === 0" class="ai-welcome">
            <div class="ai-welcome-avatar">🤖</div>
            <div class="ai-welcome-bubble">
              <p>你好！我是<strong>社团团购助手</strong> 🎉</p>
              <p>我可以帮你：</p>
              <ul>
                <li>📦 解答商品管理、秒杀活动相关问题</li>
                <li>📋 协助订单处理和数据分析</li>
                <li>💡 提供社团活动创意方案</li>
                <li>🔧 回答技术问题（Java/Vue/MySQL等）</li>
              </ul>
              <p class="ai-welcome-hint">试试问我：<em>"如何策划一场成功的社团招新活动？"</em></p>
            </div>
          </div>

          <!-- 消息列表 -->
          <div
            v-for="(msg, index) in messages"
            :key="index"
            class="ai-msg-row"
            :class="'ai-msg-' + msg.role"
          >
            <div v-if="msg.role === 'assistant'" class="ai-msg-avatar">🤖</div>
            <div class="ai-msg-bubble" :class="'ai-bubble-' + msg.role">
              <div class="ai-msg-content" v-html="formatMessage(msg.content)"></div>
            </div>
            <div v-if="msg.role === 'user'" class="ai-msg-avatar ai-user-avatar">😊</div>
          </div>

          <!-- 打字指示器 -->
          <div v-if="isLoading" class="ai-msg-row ai-msg-assistant">
            <div class="ai-msg-avatar">🤖</div>
            <div class="ai-msg-bubble ai-bubble-assistant">
              <div class="ai-typing">
                <span></span><span></span><span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 快捷问题 -->
        <div v-if="messages.length === 0 && !isLoading" class="ai-quick-actions">
          <button
            v-for="(q, i) in quickQuestions"
            :key="i"
            class="ai-quick-btn"
            @click="sendQuickQuestion(q.text)"
          >{{ q.label }}</button>
        </div>

        <!-- 输入区域 -->
        <div class="ai-input-area">
          <textarea
            ref="inputRef"
            v-model="inputText"
            @keydown.enter.exact.prevent="sendMessage"
            placeholder="输入你的问题... (Enter发送)"
            rows="1"
            :disabled="isLoading"
            @input="autoResize"
          ></textarea>
          <button
            class="ai-send-btn"
            :class="{ 'ai-send-loading': isLoading, 'ai-send-active': inputText.trim() }"
            @click="sendMessage"
            :disabled="isLoading || !inputText.trim()"
          >
            <svg v-if="!isLoading" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <line x1="22" y1="2" x2="11" y2="13" /><polygon points="22 2 15 22 11 13 2 9 22 2" />
            </svg>
            <div v-else class="ai-spinner"></div>
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from 'axios'

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const AI_CHAT_URL = `${API_BASE}/api/dashboard/ai-chat`
const AI_STATUS_URL = `${API_BASE}/api/dashboard/ai-status`

export default {
  name: 'AiAssistant',
  data() {
    return {
      isOpen: false,
      inputText: '',
      messages: [],
      isLoading: false,
      quickQuestions: [
        { label: '📊 如何提高团购转化率？', text: '如何提高社团团购活动的用户参与度和转化率？' },
        { label: '⚡ 秒杀活动怎么设计？', text: '帮我设计一个校园社团秒杀活动的完整方案，包括时间、规则、预热策略' },
        { label: '🔧 项目技术架构说明', text: '请介绍一下这个社团团购项目的技术架构和核心功能模块' },
        { label: '📝 写一份活动策划案', text: '帮我写一份校园春季社团招新活动的详细策划案' }
      ]
    }
  },
  watch: {
    isOpen(val) {
      if (val) {
        this.$nextTick(() => this.scrollToBottom())
      }
    },
    messages() {
      this.$nextTick(() => this.scrollToBottom())
    }
  },
  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen
    },
    async sendMessage() {
      const text = this.inputText.trim()
      if (!text || this.isLoading) return

      this.messages.push({ role: 'user', content: text })
      this.inputText = ''
      this.isLoading = true

      const history = this.messages.slice(0, -1).map(m => ({
        role: m.role,
        content: m.content
      }))

      try {
        const res = await axios.post(AI_CHAT_URL, {
          message: text,
          history: history.slice(-10)
        })

        const reply = res.data?.data || '抱歉，我暂时无法回答这个问题。'
        this.messages.push({ role: 'assistant', content: reply })
      } catch (err) {
        console.error('AI请求失败:', err)
        this.messages.push({
          role: 'assistant',
          content: '❌ 网络连接失败，请检查后端服务是否启动。'
        })
      } finally {
        this.isLoading = false
        this.$nextTick(() => {
          if (this.$refs.inputRef) this.$refs.inputRef.focus()
        })
      }
    },
    sendQuickQuestion(text) {
      this.inputText = text
      this.sendMessage()
    },
    clearChat() {
      this.messages = []
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },
    autoResize(e) {
      e.target.style.height = 'auto'
      e.target.style.height = Math.min(e.target.scrollHeight, 120) + 'px'
    },
    formatMessage(text) {
      return text
        .replace(/```([\s\S]*?)```/g, '<pre class="ai-code-block"><code>$1</code></pre>')
        .replace(/`([^`]+)`/g, '<code class="ai-inline-code">$1</code>')
        .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
        .replace(/\n/g, '<br>')
    }
  }
}
</script>

<style scoped>
.ai-assistant-wrapper {
  position: fixed;
  z-index: 10000;
  bottom: 28px;
  right: 28px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', sans-serif;
}

/* ====== 浮动按钮 ====== */
.ai-float-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.5), 0 0 40px rgba(118, 75, 162, 0.2);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.35s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  position: relative;
}

.ai-float-btn:hover {
  transform: scale(1.1) rotate(-5deg);
  box-shadow: 0 6px 30px rgba(102, 126, 234, 0.65), 0 0 60px rgba(118, 75, 162, 0.3);
}

.ai-float-btn:active {
  transform: scale(0.95);
}

.ai-btn-icon svg {
  width: 26px;
  height: 26px;
  color: #fff;
}

.ai-btn-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #fff;
  color: #667eea;
  font-size: 10px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 10px;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

@keyframes ai-pulse-ring {
  0% { transform: scale(1); opacity: 0.8; }
  100% { transform: scale(1.5); opacity: 0; }
}
.ai-pulse::before {
  content: '';
  position: absolute;
  inset: -4px;
  border-radius: 50%;
  background: rgba(102, 126, 234, 0.4);
  animation: ai-pulse-ring 2s infinite;
}

/* ====== 聊天面板 ====== */
.ai-chat-panel {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 400px;
  height: 600px;
  max-height: calc(100vh - 80px);
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border-radius: 22px;
  box-shadow:
    0 25px 60px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.6),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: chat-enter 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes chat-enter {
  from { opacity: 0; transform: translateY(30px) scale(0.92); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

/* ====== 头部 ====== */
.ai-header {
  padding: 16px 18px 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.ai-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-avatar {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  background: rgba(255,255,255,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  backdrop-filter: blur(4px);
}

.ai-header-info h3 {
  margin: 0;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

.ai-status-text {
  font-size: 11px;
  opacity: 0.85;
  margin-left: 4px;
}

.ai-status-dot {
  display: inline-block;
  width: 7px;
  height: 7px;
  background: #4ade80;
  border-radius: 50%;
  box-shadow: 0 0 6px #4ade80;
  vertical-align: middle;
}

.ai-header-actions {
  display: flex;
  gap: 4px;
}

.ai-icon-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: rgba(255,255,255,0.15);
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.ai-icon-btn:hover {
  background: rgba(255,255,255,0.28);
  transform: scale(1.08);
}

.ai-close-btn:hover {
  background: rgba(239, 68, 68, 0.6);
}

/* ====== 消息区 ====== */
.ai-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  scroll-behavior: smooth;

  &::-webkit-scrollbar {
    width: 5px;
  }
  &::-webkit-scrollbar-track {
    background: transparent;
  }
  &::-webkit-scrollbar-thumb {
    background: rgba(0,0,0,0.12);
    border-radius: 3px;
  }
}

/* 欢迎消息 */
.ai-welcome {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}

.ai-welcome-avatar {
  width: 32px;
  height: 32px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 17px;
  flex-shrink: 0;
}

.ai-welcome-bubble {
  background: linear-gradient(135deg, #f8f9ff 0%, #f0eef8 100%);
  border: 1px solid rgba(102, 126, 234, 0.12);
  border-radius: 16px;
  border-top-left-radius: 4px;
  padding: 14px 16px;
  max-width: 320px;
  font-size: 13.5px;
  line-height: 1.65;
  color: #374151;
}

.ai-welcome-bubble p { margin: 0 0 8px; }
.ai-welcome-bubble p:last-child { margin-bottom: 0; }
.ai-welcome-bubble ul {
  margin: 8px 0;
  padding-left: 18px;
}
.ai-welcome-bubble li { margin-bottom: 4px; }

.ai-welcome-hint {
  font-size: 12px !important;
  color: #8b5cf6 !important;
  opacity: 0.85;
}

/* 消息行 */
.ai-msg-row {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}
.ai-msg-user {
  flex-direction: row-reverse;
}

.ai-msg-avatar {
  width: 30px;
  height: 30px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.ai-user-avatar {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%) !important;
}

/* 消息气泡 */
.ai-msg-bubble {
  max-width: 280px;
  padding: 11px 15px;
  border-radius: 16px;
  font-size: 13.5px;
  line-height: 1.65;
  word-break: break-word;
}

.ai-bubble-assistant {
  background: linear-gradient(135deg, #f8f9ff 0%, #f0eef8 100%);
  border: 1px solid rgba(102, 126, 234, 0.1);
  border-top-left-radius: 4px;
  color: #374151;
}

.ai-bubble-user {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-top-right-radius: 4px;
}

.ai-msg-content {
  white-space: pre-wrap;
}

/* 代码块 */
.ai-code-block {
  background: #1e1e2e;
  color: #cdd6f4;
  padding: 10px 14px;
  border-radius: 10px;
  font-size: 12.5px;
  line-height: 1.5;
  overflow-x: auto;
  margin: 8px 0;
  font-family: 'Cascadia Code', 'Fira Code', Consolas, monospace;
}

.ai-inline-code {
  background: rgba(102, 126, 234, 0.12);
  color: #6366f1;
  padding: 1px 6px;
  border-radius: 5px;
  font-size: 12.5px;
  font-family: 'Cascadia Code', Consolas, monospace;
}

/* 打字动画 */
.ai-typing {
  display: flex;
  gap: 5px;
  padding: 4px 0;
}

.ai-typing span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #a78bfa;
  animation: typing-bounce 1.4s infinite ease-in-out;
}
.ai-typing span:nth-child(2) { animation-delay: 0.16s; }
.ai-typing span:nth-child(3) { animation-delay: 0.32s; }

@keyframes typing-bounce {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
  30% { transform: translateY(-6px); opacity: 1; }
}

/* 快捷问题 */
.ai-quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 7px;
  padding: 0 18px 10px;
  flex-shrink: 0;
}

.ai-quick-btn {
  padding: 7px 13px;
  border: 1.5px solid rgba(102, 126, 234, 0.25);
  background: rgba(102, 126, 234, 0.05);
  border-radius: 20px;
  color: #6366f1;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.25s;
  white-space: nowrap;
}

.ai-quick-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* 输入区 */
.ai-input-area {
  padding: 12px 16px 14px;
  border-top: 1px solid rgba(0,0,0,0.06);
  background: rgba(250, 250, 252, 0.8);
  display: flex;
  gap: 10px;
  align-items: flex-end;
  flex-shrink: 0;
}

.ai-input-area textarea {
  flex: 1;
  border: 1.5px solid #e5e7eb;
  border-radius: 14px;
  padding: 10px 15px;
  font-size: 13.5px;
  line-height: 1.5;
  resize: none;
  outline: none;
  font-family: inherit;
  background: #fff;
  transition: border-color 0.25s, box-shadow 0.25s;
  max-height: 120px;
}

.ai-input-area textarea:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.12);
}

.ai-input-area textarea:disabled {
  background: #f9fafb;
  cursor: not-allowed;
}

.ai-send-btn {
  width: 42px;
  height: 42px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.25s;
  flex-shrink: 0;
}

.ai-send-btn:hover:not(:disabled) {
  transform: scale(1.08);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.45);
}

.ai-send-btn:disabled {
  background: #d1d5db;
  cursor: not-allowed;
}

.ai-send-active:not(:disabled) {
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.45);
}

/* 加载旋转 */
.ai-spinner {
  width: 18px;
  height: 18px;
  border: 2.5px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 过渡动画 */
.bounce-enter-active, .bounce-leave-active {
  transition: all 0.35s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.bounce-enter-from, .bounce-leave-to {
  opacity: 0;
  transform: scale(0.3);
}

.chat-slide-enter-active {
  transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.chat-slide-leave-active {
  transition: all 0.25s ease-in;
}
.chat-slide-enter-from {
  opacity: 0;
  transform: translateY(30px) scale(0.88);
}
.chat-slide-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.92);
}

/* 响应式 */
@media (max-width: 480px) {
  .ai-chat-panel {
    width: calc(100vw - 24px);
    height: calc(100vh - 100px);
    right: -6px;
    bottom: -6px;
    border-radius: 18px;
  }
  .ai-assistant-wrapper {
    right: 12px;
    bottom: 12px;
  }
}
</style>
