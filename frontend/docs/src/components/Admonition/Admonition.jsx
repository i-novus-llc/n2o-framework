import React from 'react'
import PropTypes from 'prop-types'

import NoteIcon from './note.svg'
import IdeaIcon from './idea.svg'
import InfoIcon from './info.svg'
import WarningIcon from './warning.svg'
import FireIcon from './fire.svg'

// Хз как его вытащить из @docusaurus, так что просто повторяю тут

function Admonition({ type, title, text }) {
    let wrapperClassName = ''
    let icon = null

    switch (type) {
        case 'note':
            wrapperClassName = 'admonition admonition-note alert alert--secondary'
            icon = <NoteIcon/>
            break
        case 'success':
            wrapperClassName = 'admonition admonition-tip alert alert--success'
            icon = <IdeaIcon/>
            break
        case 'info':
            wrapperClassName = 'admonition admonition-info alert alert--info'
            icon = <InfoIcon/>
            break
        case 'warning':
            wrapperClassName = 'admonition admonition-caution alert alert--warning'
            icon = <WarningIcon/>
            break
        case 'danger':
            wrapperClassName = 'admonition admonition-danger alert alert--danger'
            icon = <FireIcon/>
            break
    }

    return (
        <div className={wrapperClassName}>
            <div className="admonition-heading">
                <h5><span className="admonition-icon">{icon}</span>{title}
                </h5>
            </div>
            <div className="admonition-content"><p>{text}</p></div>
        </div>
    )
}

Admonition.propTypes = {
    type: PropTypes.oneOf(['note', 'success', 'info', 'warning', 'danger']).isRequired,
    title: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired,
}

export { Admonition }
