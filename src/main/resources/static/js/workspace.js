'use strict';

const HexaDocsWorkspace = (() => {
    const state = {
        stompClient: null,
        activeKnowledgeBase: null,
        activeConversation: null,
        hasMessages: false
    };

    const elements = {};

    function initializeElements() {
        elements.statusDot = document.getElementById('statusDot');
        elements.statusLabel = document.getElementById('statusLabel');
        elements.knowledgeBaseSelection = document.getElementById('knowledgeBaseSelection');
        elements.knowledgeBaseSelect = document.getElementById('knowledgeBaseSelect');
        elements.showCreateKbButton = document.getElementById('showCreateKbButton');
        elements.knowledgeBaseCreation = document.getElementById('knowledgeBaseCreation');
        elements.newKnowledgeBaseName = document.getElementById('newKnowledgeBaseName');
        elements.cancelCreateKb = document.getElementById('cancelCreateKb');
        elements.confirmCreateKb = document.getElementById('confirmCreateKb');
        elements.activeKb = document.getElementById('activeKb');
        elements.newConversationButton = document.getElementById('newConversationButton');
        elements.conversationList = document.getElementById('conversationList');
        elements.file = document.getElementById('file');
        elements.docName = document.getElementById('docName');
        elements.language = document.getElementById('language');
        elements.uploadButton = document.getElementById('uploadButton');
        elements.uploadFeedback = document.getElementById('uploadFeedback');
        elements.chat = document.getElementById('chat');
        elements.emptyState = document.getElementById('emptyState');
        elements.chatTitle = document.getElementById('chatTitle');
        elements.chatKnowledgeBase = document.getElementById('chatKnowledgeBase');
        elements.input = document.getElementById('input');
        elements.sendButton = document.getElementById('sendButton');
        elements.conversationModal = document.getElementById('createConversationModal');
        elements.conversationTitle = document.getElementById('conversationTitle');
        elements.closeConversationModal = document.getElementById('closeCreateConversationModal');
        elements.cancelConversation = document.getElementById('cancelCreateConversation');
        elements.confirmConversation = document.getElementById('confirmCreateConversation');
    }

    function connect() {
        state.stompClient = new StompJs.Client({
            brokerURL: 'ws://localhost:8080/ws',
            reconnectDelay: 5000,
            debug: () => {}
        });

        state.stompClient.onConnect = () => {
            updateConnectionStatus(true);
            subscribeToChat();
        };

        state.stompClient.onDisconnect = () => updateConnectionStatus(false);
        state.stompClient.onStompError = () => updateConnectionStatus(false);

        state.stompClient.activate();
    }

    function subscribeToChat() {
        state.stompClient.subscribe('/topic/chat', handleIncomingMessage);
    }

    function handleIncomingMessage(message) {
        const data = JSON.parse(message.body);

        if (data.conversationId && data.conversationId !== state.activeConversation?.id) {
            return;
        }

        appendMessage(data.answer, 'assistant');
    }

    function updateConnectionStatus(connected) {
        elements.statusDot.classList.toggle('connection-status__dot--connected', connected);
        elements.statusLabel.textContent = connected ? 'Connected' : 'Disconnected';
    }

    async function loadKnowledgeBases() {
        try {
            const response = await fetch('/api/knowledge-bases');

            if (!response.ok) {
                throw new Error('Failed to load knowledge bases');
            }

            const knowledgeBases = await response.json();
            renderKnowledgeBases(knowledgeBases);
        } catch (error) {
            showSystemMessage('Failed to load knowledge bases.');
            console.error(error);
        }
    }

    function renderKnowledgeBases(knowledgeBases) {
        elements.knowledgeBaseSelect.innerHTML = `
    <option value="">Select existing knowledge base</option>
        `;

        knowledgeBases.forEach(knowledgeBase => {
            const option = document.createElement('option');

            option.value = knowledgeBase.id;
            option.textContent = knowledgeBase.name;

            elements.knowledgeBaseSelect.appendChild(option);
        });
    }

    function showKnowledgeBaseCreation() {
        elements.knowledgeBaseSelection.hidden = true;
        elements.knowledgeBaseCreation.hidden = false;
        elements.newKnowledgeBaseName.value = '';
        elements.newKnowledgeBaseName.focus();
    }

    function hideKnowledgeBaseCreation() {
        elements.knowledgeBaseCreation.hidden = true;
        elements.knowledgeBaseSelection.hidden = false;
        elements.newKnowledgeBaseName.value = '';
    }

    async function createKnowledgeBase() {
        const name = elements.newKnowledgeBaseName.value.trim();

        if (!name) {
            showSystemMessage('Enter a knowledge base name.');
            elements.newKnowledgeBaseName.focus();
            return;
        }

        setButtonLoading(elements.confirmCreateKb, true);

        try {
            const response = await fetch('/api/knowledge-bases', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({name})
            });

            if (!response.ok) {
                throw new Error('Failed to create knowledge base');
            }

            const knowledgeBase = await response.json();

            hideKnowledgeBaseCreation();
            await loadKnowledgeBases();

            elements.knowledgeBaseSelect.value = knowledgeBase.id;
            await selectKnowledgeBase(knowledgeBase.id);

            showSystemMessage(`Knowledge base created · ${knowledgeBase.name}`);
        } catch (error) {
            showSystemMessage('Failed to create knowledge base.');
            console.error(error);
        } finally {
            setButtonLoading(elements.confirmCreateKb, false);
        }
    }

    async function selectKnowledgeBase(id) {
        if (!id) {
            resetKnowledgeBase();
            return;
        }

        const selectedOption = elements.knowledgeBaseSelect.selectedOptions[0];

        state.activeKnowledgeBase = {
            id,
            name: selectedOption.textContent
        };

        state.activeConversation = null;

        updateKnowledgeBaseUI();
        clearChat();

        await loadConversations();
    }

    function resetKnowledgeBase() {
        state.activeKnowledgeBase = null;
        state.activeConversation = null;

        elements.activeKb.textContent = '—';
        elements.chatKnowledgeBase.textContent = 'No knowledge base selected';
        elements.chatTitle.textContent = 'No conversation selected';

        renderConversationList([]);
        clearChat();
    }

    function updateKnowledgeBaseUI() {
        elements.activeKb.textContent = state.activeKnowledgeBase.name;
        elements.chatKnowledgeBase.textContent = state.activeKnowledgeBase.name;
        elements.chatTitle.textContent = 'No conversation selected';
    }

    async function loadConversations() {
        if (!state.activeKnowledgeBase) {
            renderConversationList([]);
            return;
        }

        try {
            const knowledgeBaseId = encodeURIComponent(state.activeKnowledgeBase.id);
            const response = await fetch(`/api/conversations?knowledgeBaseId=${knowledgeBaseId}`);

            if (!response.ok) {
                throw new Error('Failed to load conversations');
            }

            const conversations = await response.json();
            renderConversationList(conversations);
        } catch (error) {
            showSystemMessage('Failed to load conversations.');
            console.error(error);
        }
    }

    function renderConversationList(conversations) {
        elements.conversationList.innerHTML = '';

        if (!conversations || conversations.length === 0) {
            const message = document.createElement('div');

            message.className = 'empty-message';
            message.textContent = state.activeKnowledgeBase
                ? 'No conversations'
                : 'Select a knowledge base';

            elements.conversationList.appendChild(message);
            return;
        }

        conversations.forEach(conversation => {
            elements.conversationList.appendChild(createConversationItem(conversation));
        });
    }

    function createConversationItem(conversation) {
        const item = document.createElement('button');

        item.type = 'button';
        item.className = 'conversation-item';
        item.dataset.id = conversation.id;

        if (conversation.id === state.activeConversation?.id) {
            item.classList.add('conversation-item--active');
        }

        const title = document.createElement('span');
        title.className = 'conversation-item__title';
        title.textContent = conversation.title || 'Untitled conversation';

        const date = document.createElement('span');
        date.className = 'conversation-item__date';
        date.textContent = formatDate(conversation.createdAt);

        item.append(title, date);
        item.addEventListener('click', () => selectConversation(conversation.id));

        return item;
    }

    function openConversationModal() {
        if (!state.activeKnowledgeBase) {
            showSystemMessage('Select a knowledge base first.');
            return;
        }

        elements.conversationTitle.value = '';
        elements.conversationModal.classList.add('modal--open');
        elements.conversationTitle.focus();
    }

    function closeConversationModal() {
        elements.conversationModal.classList.remove('modal--open');
        elements.conversationTitle.value = '';
    }

    async function createConversation() {
        if (!state.activeKnowledgeBase) {
            showSystemMessage('Select a knowledge base first.');
            return;
        }

        const title = elements.conversationTitle.value.trim();

        if (!title) {
            elements.conversationTitle.focus();
            return;
        }

        setButtonLoading(elements.confirmConversation, true);

        try {
            const knowledgeBaseId = encodeURIComponent(state.activeKnowledgeBase.id);
            const response = await fetch(`/api/conversations?knowledgeBaseId=${knowledgeBaseId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({title})
            });

            if (!response.ok) {
                throw new Error('Failed to create conversation');
            }

            const conversation = await response.json();

            closeConversationModal();
            await loadConversations();
            await selectConversation(conversation.id);
        } catch (error) {
            showSystemMessage('Failed to create conversation.');
            console.error(error);
        } finally {
            setButtonLoading(elements.confirmConversation, false);
        }
    }

    async function selectConversation(id) {
        try {
            const response = await fetch(`/api/conversations/${id}/messages`);

            if (!response.ok) {
                throw new Error('Failed to load conversation');
            }

            const data = await response.json();
            const messages = Array.isArray(data) ? data : data.messages;

            state.activeConversation = data.conversation || {id};

            clearChat();

            messages?.forEach(message => {
                const role = message.role || message.type;

                appendMessage(
                    message.content,
                    isUserRole(role) ? 'user' : 'assistant'
                );
            });

            updateConversationSelection();

            elements.chatTitle.textContent =
                state.activeConversation.title || 'Conversation';

            localStorage.setItem(
                `activeConversation:${state.activeKnowledgeBase.id}`,
                id
            );
        } catch (error) {
            showSystemMessage('Failed to load conversation.');
            console.error(error);
        }
    }

    function isUserRole(role) {
        return role === 'USER' || role === 'user';
    }

    function updateConversationSelection() {
        document.querySelectorAll('.conversation-item').forEach(item => {
            item.classList.toggle(
                'conversation-item--active',
                item.dataset.id === state.activeConversation?.id
            );
        });
    }

    async function uploadDocument() {
        if (!state.activeKnowledgeBase) {
            showSystemMessage('Select a knowledge base first.');
            return;
        }

        const file = elements.file.files[0];

        if (!file) {
            showSystemMessage('Select a file to upload.');
            return;
        }

        const name = elements.docName.value.trim();
        const formData = new FormData();

        formData.append('file', file);
        formData.append('name', name || file.name);
        formData.append('knowledgeBaseId', state.activeKnowledgeBase.id);
        formData.append('language', elements.language.value);

        setButtonLoading(elements.uploadButton, true);
        elements.uploadFeedback.textContent = 'Uploading…';

        try {
            const response = await fetch('/api/documents/upload', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error('Upload failed');
            }

            const data = await response.json();

            elements.uploadFeedback.textContent = '✓ Uploaded';

            showSystemMessage(`Document indexed · ${data.documentId}`);

            elements.file.value = '';
            elements.docName.value = '';

            setTimeout(() => {
                elements.uploadFeedback.textContent = '';
            }, 3000);
        } catch (error) {
            elements.uploadFeedback.textContent = 'Upload failed';

            showSystemMessage('Document upload failed.');
            console.error(error);
        } finally {
            setButtonLoading(elements.uploadButton, false);
        }
    }

    function sendMessage() {
        const text = elements.input.value.trim();

        if (!state.activeKnowledgeBase) {
            showSystemMessage('Select a knowledge base first.');
            return;
        }

        if (!state.activeConversation) {
            showSystemMessage('Create or select a conversation first.');
            return;
        }

        if (!text) {
            return;
        }

        if (!state.stompClient || !state.stompClient.connected) {
            showSystemMessage('WebSocket is not connected.');
            return;
        }

        appendMessage(text, 'user');
        elements.input.value = '';

        state.stompClient.publish({
            destination: '/app/chat.ask',
            body: JSON.stringify({
                conversationId: state.activeConversation.id,
                question: text
            })
        });
    }

    function clearChat() {
        elements.chat.innerHTML = `
        <div class="empty-state" id="emptyState">
            <svg viewBox="0 0 40 40" aria-hidden="true">
                <path
                    d="M20 4 L35 12.3 V27.7 L20 36 L5 27.7 V12.3 L20 4Z"
                    stroke="currentColor"
                    stroke-width="1.5"
                />
                <circle
                    cx="20"
                    cy="20"
                    r="4"
                    stroke="currentColor"
                    stroke-width="1.5"
                />
            </svg>
            <p>Start a conversation</p>
        </div>
    `;

        state.hasMessages = false;
    }

    function appendMessage(text, type) {
        hideEmptyState();

        const row = document.createElement('div');
        row.className = `message message--${type}`;

        const avatar = document.createElement('div');
        avatar.className = 'message__avatar';
        avatar.textContent = type === 'user' ? 'U' : 'AI';

        const bubble = document.createElement('div');
        bubble.className = 'message__bubble';
        bubble.textContent = text;

        row.append(avatar, bubble);
        elements.chat.appendChild(row);

        elements.chat.scrollTop = elements.chat.scrollHeight;
    }

    function showSystemMessage(text) {
        hideEmptyState();

        const message = document.createElement('div');

        message.className = 'system-message';
        message.textContent = text;

        elements.chat.appendChild(message);
        elements.chat.scrollTop = elements.chat.scrollHeight;
    }

    function hideEmptyState() {
        if (state.hasMessages) {
            return;
        }

        document.getElementById('emptyState')?.remove();
        state.hasMessages = true;
    }

    function setButtonLoading(button, loading) {
        button.disabled = loading;

        if (loading) {
            button.dataset.originalText = button.textContent;
            button.textContent = 'Working…';
            return;
        }

        button.textContent = button.dataset.originalText;
    }

    function formatDate(value) {
        if (!value) {
            return '';
        }

        const date = new Date(value);

        if (Number.isNaN(date.getTime())) {
            return '';
        }

        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }

    function registerEventListeners() {
        elements.knowledgeBaseSelect.addEventListener(
            'change',
            event => selectKnowledgeBase(event.target.value)
        );

        elements.showCreateKbButton.addEventListener(
            'click',
            showKnowledgeBaseCreation
        );

        elements.cancelCreateKb.addEventListener(
            'click',
            hideKnowledgeBaseCreation
        );

        elements.confirmCreateKb.addEventListener(
            'click',
            createKnowledgeBase
        );

        elements.newConversationButton.addEventListener(
            'click',
            openConversationModal
        );

        elements.closeConversationModal.addEventListener(
            'click',
            closeConversationModal
        );

        elements.cancelConversation.addEventListener(
            'click',
            closeConversationModal
        );

        elements.confirmConversation.addEventListener(
            'click',
            createConversation
        );

        elements.conversationModal.addEventListener(
            'click',
            event => {
                if (event.target === elements.conversationModal) {
                    closeConversationModal();
                }
            }
        );

        elements.conversationTitle.addEventListener(
            'keydown',
            event => {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    createConversation();
                }

                if (event.key === 'Escape') {
                    closeConversationModal();
                }
            }
        );

        elements.uploadButton.addEventListener(
            'click',
            uploadDocument
        );

        elements.sendButton.addEventListener(
            'click',
            sendMessage
        );

        elements.input.addEventListener(
            'keydown',
            event => {
                if (event.key === 'Enter' && !event.shiftKey) {
                    event.preventDefault();
                    sendMessage();
                }
            }
        );

        elements.newKnowledgeBaseName.addEventListener(
            'keydown',
            event => {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    createKnowledgeBase();
                }

                if (event.key === 'Escape') {
                    hideKnowledgeBaseCreation();
                }
            }
        );
    }

    async function initialize() {
        initializeElements();
        registerEventListeners();
        await loadKnowledgeBases();
        connect();
    }

    return {
        initialize
    };
})();

document.addEventListener('DOMContentLoaded', () => {
    HexaDocsWorkspace.initialize();
});
