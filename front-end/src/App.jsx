import { useEffect, useState } from "react";
import {
  Routes,
  Route,
  useNavigate,
  useLocation
} from "react-router-dom";

import Sidebar from "./components/Sidebar";
import ChatPage from "./pages/ChatPage";
import NewChatPage from "./pages/NewChatPage";
import HistoryPage from "./pages/HistoryPage";

const ACTIVE_CHAT_KEY = "active_chat";

export default function App() {
  const [chatName, setChatName] = useState("");
  const [currentMessages, setCurrentMessages] = useState([]);
  const [currentChatId, setCurrentChatId] = useState(null);
  const [history, setHistory] = useState([]);

  const navigate = useNavigate();
  const location = useLocation();

  // ===============================
  // FETCH CHAT HISTORY
  // ===============================
  const fetchHistory = async () => {
    const res = await fetch("http://localhost:8080/api/chat/history");
    const data = await res.json();
    setHistory([...data].reverse());
  };

  // ===============================
  // LOAD CHAT BY ID (CORE FUNCTION)
  // ===============================
  const loadChatById = async (chatId, title, shouldNavigate) => {
    const res = await fetch(`http://localhost:8080/api/chat/${chatId}`);
    const logs = await res.json();

    const mappedMessages = logs.flatMap((log) => {
      const group = [];

      // User image
      if (log.imageGiven) {
        group.push({
          role: "user",
          type: "image",
          imageUrl: `http://localhost:8080/api/chat/image/${log.id}`
        });
      }

      // User typed text only
      if (log.actualUserText?.trim()) {
        group.push({
          role: "user",
          type: "text",
          text: log.actualUserText
        });
      }

      // AI response
      group.push({
        role: "ai",
        type: "text",
        text: log.aiResponse
      });

      return group;
    });

    setChatName(title);
    setCurrentChatId(chatId);
    setCurrentMessages(mappedMessages);

    // Persist active chat
    localStorage.setItem(
      ACTIVE_CHAT_KEY,
      JSON.stringify({ chatId, title })
    );

    // Navigate only when required
    if (shouldNavigate) {
      navigate("/chat");
    }
  };

  // ===============================
  // INITIAL APP LOAD
  // ===============================
  useEffect(() => {
    fetchHistory();

    const saved = localStorage.getItem(ACTIVE_CHAT_KEY);
    if (saved) {
      const { chatId, title } = JSON.parse(saved);

      loadChatById(
        chatId,
        title,
        location.pathname === "/chat" // 🔥 key fix
      );
    }
  }, []);

  // ===============================
  // START NEW CHAT
  // ===============================
  const handleStartChat = (name, chatId) => {
    setChatName(name);
    setCurrentChatId(chatId);
    setCurrentMessages([]);

    localStorage.setItem(
      ACTIVE_CHAT_KEY,
      JSON.stringify({ chatId, title: name })
    );

    fetchHistory();
    navigate("/chat");
  };

  // ===============================
  // LOAD CHAT FROM HISTORY
  // ===============================
  const handleLoadChat = (chat) => {
    loadChatById(chat.id, chat.title, true);
  };

  // ===============================
  // DELETE CHAT
  // ===============================
  const handleDeleteChat = async (chatId) => {
    await fetch(`http://localhost:8080/api/chat/${chatId}`, {
      method: "DELETE"
    });

    const saved = JSON.parse(localStorage.getItem(ACTIVE_CHAT_KEY));
    if (saved?.chatId === chatId) {
      localStorage.removeItem(ACTIVE_CHAT_KEY);
      setChatName("");
      setCurrentChatId(null);
      setCurrentMessages([]);
      navigate("/");
    }

    fetchHistory();
  };

  return (
    <div className="app-layout">
      <Sidebar history={history} onLoadChat={handleLoadChat} />

      <div className="page-content">
        <Routes>
          <Route
            path="/"
            element={<NewChatPage onStartChat={handleStartChat} />}
          />

          <Route
            path="/chat"
            element={
              <ChatPage
                chatName={chatName}
                messages={currentMessages}
                setMessages={setCurrentMessages}
                chatId={currentChatId}
              />
            }
          />

          <Route
            path="/history"
            element={
              <HistoryPage
                history={history}
                onLoadChat={handleLoadChat}
                onDeleteChat={handleDeleteChat}
              />
            }
          />
        </Routes>
      </div>
    </div>
  );
}
