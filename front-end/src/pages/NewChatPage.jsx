import { useState } from "react";
import "../styles/NewChatPage.css";

export default function NewChatPage({ onStartChat }) {
  const [chatName, setChatName] = useState("");
  const [error, setError] = useState("");

  const startNewChat = async () => {
    if (!chatName.trim()) {
      setError("Chat name is required");
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/api/chat/session", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: chatName })
      });

      const data = await res.json();
      onStartChat(chatName, data.id);
    } catch (err) {
      setError("Failed to start chat");
    }
  };

  return (
    <div className="new-chat-page">
      <h1>Start a New Conversation</h1>
      <p>Create a secure, PII-protected chat session</p>

      <div className="chat-name-input-group">
        <input
          type="text"
          placeholder="Enter chat name"
          value={chatName}
          onChange={(e) => {
            setChatName(e.target.value);
            setError("");
          }}
          className="chat-name-input"
        />
        {error && <p className="error-message">{error}</p>}
      </div>

      <button className="start-btn" onClick={startNewChat}>
        Start Chat
      </button>
    </div>
  );
}
