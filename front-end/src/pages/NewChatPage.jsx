import { useState } from "react";
import "../styles/NewChatPage.css";

export default function NewChatPage({ onStartChat }) {
  const [chatName, setChatName] = useState("");
  const [error, setError] = useState("");

  const startNewChat = async () => {
    if (!chatName.trim()) {
      setError("Please enter a chat name to continue");
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
      setError("Failed to start chat. Please try again.");
    }
  };

  return (
    <div className="new-chat-page">
      <div className="new-chat-logo">⬡</div>

      <h1>Start a New Conversation</h1>
      <p>Your messages are secured with end-to-end PII redaction and encryption</p>

      <div className="feature-badges">
        <div className="feature-badge">
          <span className="badge-icon">🔒</span> End-to-End Encrypted
        </div>
        <div className="feature-badge">
          <span className="badge-icon">🛡️</span> PII Redacted
        </div>
        <div className="feature-badge">
          <span className="badge-icon">⚡</span> AI Powered
        </div>
      </div>

      <div className="chat-name-input-group">
        <input
          type="text"
          placeholder="Enter conversation name..."
          value={chatName}
          onChange={(e) => {
            setChatName(e.target.value);
            setError("");
          }}
          onKeyDown={(e) => e.key === "Enter" && startNewChat()}
          className="chat-name-input"
        />
        {error && <p className="error-message">{error}</p>}
      </div>

      <button className="start-btn" onClick={startNewChat}>
        Start Chat →
      </button>
    </div>
  );
}
