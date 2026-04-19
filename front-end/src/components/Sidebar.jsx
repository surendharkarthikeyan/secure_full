import { NavLink } from "react-router-dom";
import "../styles/Sidebar.css";

export default function Sidebar({ history, onLoadChat }) {
  return (
    <aside className="sidebar">
      <h2>🔐 Secure AI Chat</h2>
      <p className="subtitle">PRIVACY FIRST</p>

      <NavLink to="/" className="menu-btn">➕ New Chat</NavLink>
      <NavLink to="/chat" className="menu-btn">💬 Current Chat</NavLink>
      <NavLink to="/history" className="menu-btn">📜 History</NavLink>

      <div className="sidebar-history">
        <p className="sidebar-history-title">Recent Chats</p>

        {history.length === 0 && (
          <p className="sidebar-empty">No chats yet</p>
        )}

        {history.map((chat) => (
          <div
            key={chat.id}
            className="sidebar-chat-item"
            onClick={() => onLoadChat(chat)}
          >
            💬 <span>{chat.title}</span>
          </div>
        ))}
      </div>
    </aside>
  );
}
