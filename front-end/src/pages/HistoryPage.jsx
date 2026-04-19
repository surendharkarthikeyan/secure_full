import "../styles/HistoryPage.css";

export default function HistoryPage({ history, onLoadChat, onDeleteChat }) {
  return (
    <div className="history-page">
      <div className="history-header">
        <h1>⏱ Chat History</h1>
        <p>All your conversations in one place</p>
      </div>

      {history.length === 0 && (
        <p className="no-history">No conversations yet. Start a new chat to get going!</p>
      )}

      <div className="history-grid">
        {history.map((chat) => (
          <div key={chat.id} className="history-card">
            <div className="card-content">
              <h3>{chat.title}</h3>
              <p className="date">{chat.date}</p>
            </div>

            <div className="card-actions">
              <button
                className="load-btn"
                onClick={() => onLoadChat(chat)}
              >
                Open
              </button>

              <button
                className="delete-btn"
                onClick={() => onDeleteChat(chat.id)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
