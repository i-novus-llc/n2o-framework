import React from 'react';

function ExpandBtn({ onShowAll, onHideAll }) {
  return (
    <div className="btn-group expand-btn" role="group" aria-label="Basic example">
      <button type="button" className="btn" onClick={onShowAll}>
        Открыть
      </button>
      <button type="button" className="btn btn-secondary" onClick={onHideAll}>
        Закрыть
      </button>
    </div>
  );
}

export default ExpandBtn;
