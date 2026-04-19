import { useState, useRef } from "react";
import ChatMessage from "../components/ChatMessage";
import "../styles/ChatPage.css";

const BACKEND_URL = "http://localhost:8080/api/chat";

export default function ChatPage({ chatName, messages, setMessages, chatId }) {
  const [input, setInput] = useState("");
  const [image, setImage] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);

  const fileInputRef = useRef(null);

  const replaceProcessing = (text) => {
    setMessages((prev) =>
      prev.map((m) =>
        m.processing
          ? { role: "ai", type: "text", text }
          : m
      )
    );
  };

  const sendMessage = async () => {
    if ((!input.trim() && !image) || isProcessing) return;

    const newMessages = [];

    if (image) {
      newMessages.push({
        role: "user",
        type: "image",
        imageUrl: URL.createObjectURL(image)
      });
    }

    if (input.trim()) {
      newMessages.push({
        role: "user",
        type: "text",
        text: input
      });
    }

    newMessages.push({
      role: "ai",
      type: "text",
      text: "🔍 Processing your request securely...",
      processing: true
    });

    setMessages((prev) => [...prev, ...newMessages]);

    const formData = new FormData();
    formData.append("chatId", chatId);
    if (input.trim()) formData.append("message", input);
    if (image) formData.append("image", image);

    setInput("");
    setImage(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
    setIsProcessing(true);

    try {
      const res = await fetch(BACKEND_URL, {
        method: "POST",
        body: formData
      });

      const replyText = await res.text();
      replaceProcessing(replyText);
    } catch {
      replaceProcessing("❌ Something went wrong.");
    } finally {
      setIsProcessing(false);
    }
  };

  return (
    <div className="chat-page">
      <h2>{chatName || "Secure AI Chat"}</h2>
      <p className="tagline">PII Protection Enabled</p>

      <div className="chat-container">
        {messages.map((msg, i) => (
          <ChatMessage key={i} {...msg} />
        ))}
      </div>

      {image && (
        <div className="image-preview-wrapper">
          <img
            src={URL.createObjectURL(image)}
            alt="preview"
            className="image-preview"
          />
          <button
            className="remove-image"
            onClick={() => {
              setImage(null);
              if (fileInputRef.current) fileInputRef.current.value = "";
            }}
          >
            ✕
          </button>
        </div>
      )}

      <div className="chat-input-bar">
        <input
          type="file"
          accept="image/*"
          hidden
          ref={fileInputRef}
          onChange={(e) => setImage(e.target.files[0])}
        />

        <button
          className="icon-btn"
          onClick={() => fileInputRef.current.click()}
          disabled={isProcessing}
        >
          +
        </button>

        <input
          className="chat-text-input"
          placeholder="Ask anything"
          value={input}
          disabled={isProcessing}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />

        <button
          className="send-btn"
          onClick={sendMessage}
          disabled={isProcessing}
        >
          ➤
        </button>
      </div>
    </div>
  );
}
