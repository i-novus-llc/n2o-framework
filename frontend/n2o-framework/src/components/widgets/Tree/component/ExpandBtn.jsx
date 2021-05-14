import React from 'react'
import PropTypes from 'prop-types'

function ExpandBtn({ onShowAll, onHideAll }) {
    return (
        <div className="tree-exp-btn btn-group">
            <button type="button" className="btn btn-link" onClick={onShowAll}>Развернуть</button>
            <button type="button" className="btn btn-link" onClick={onHideAll}>Свернуть</button>
        </div>
    )
}

ExpandBtn.propTypes = {
    onShowAll: PropTypes.func,
    onHideAll: PropTypes.func,
}

export default ExpandBtn
