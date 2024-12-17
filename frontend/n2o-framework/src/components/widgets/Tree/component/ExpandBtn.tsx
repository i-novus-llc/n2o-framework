import React from 'react'

import { type ExpandBtnProps } from '../types'

export function ExpandBtn({ onShowAll, onHideAll }: ExpandBtnProps) {
    return (
        <div className="tree-exp-btn btn-group">
            <button type="button" className="btn btn-link" onClick={onShowAll}>Развернуть</button>
            <button type="button" className="btn btn-link" onClick={onHideAll}>Свернуть</button>
        </div>
    )
}

export default ExpandBtn
