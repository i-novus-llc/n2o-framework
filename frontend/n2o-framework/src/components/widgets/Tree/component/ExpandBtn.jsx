import React from 'react'

function ExpandBtn({ onShowAll, onHideAll }) {
    return (
        <div className="tree-exp-btn btn-group">
            <button type="button" className="btn btn-link" onClick={onShowAll}>

        Развернуть
            </button>
            <button type="button" className="btn btn-link" onClick={onHideAll}>

        Свернуть
            </button>
        </div>
    )
}

export default ExpandBtn
