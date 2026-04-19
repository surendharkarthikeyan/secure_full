import { NavLink } from "react-router-dom";
import { useTheme } from "../context/ThemeContext";
import "../styles/Sidebar.css";

export default function Sidebar({ history, onLoadChat }) {
  const { theme, toggleTheme } = useTheme();

  return (
    <aside className="sidebar">
      <h2>⬡ SecureAI</h2>
      <p className="subtitle">Encrypted Chat</p>

      <NavLink to="/" className="menu-btn">＋ New Chat</NavLink>
      <NavLink to="/chat" className="menu-btn">◈ Current Chat</NavLink>
      <NavLink to="/history" className="menu-btn">⏱ History</NavLink>

      <div className="sidebar-history">
        <p className="sidebar-history-title">Recent</p>

        {history.length === 0 && (
          <p className="sidebar-empty">No conversations yet</p>
        )}

        {history.map((chat) => (
          <div
            key={chat.id}
            className="sidebar-chat-item"
            onClick={() => onLoadChat(chat)}
          >
            › <span>{chat.title}</span>
          </div>
        ))}
      </div>

      <div className="theme-toggle-wrapper">
        <button className="theme-toggle-btn" onClick={toggleTheme}>
          <span className="theme-icon">
            {theme === "dark" ? "☀️" : "🌙"}
          </span>
          <span className="theme-label">
            {theme === "dark" ? "Light Mode" : "Dark Mode"}
          </span>
        </button>
      </div>
    </aside>
  );
}
