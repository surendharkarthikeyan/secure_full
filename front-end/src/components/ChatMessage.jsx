import "../styles/ChatMessage.css";

export default function ChatMessage({ role, text, imageUrl, type }) {
  return (
    <div className={`chat-message ${role}`}>
      {type === "image" && (
        <img src={imageUrl} alt="uploaded" className="chat-image" />
      )}

      {type === "text" && <div className="message-text">{text}</div>}
    </div>
  );
}
