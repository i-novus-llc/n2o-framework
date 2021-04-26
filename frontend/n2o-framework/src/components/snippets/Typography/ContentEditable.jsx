import React, { Component, createRef } from 'react'
import PropTypes from 'prop-types'

import { moveCursorToEnd } from './utils'

class ContentEditable extends Component {
    constructor(props) {
        super(props)
        this.ref = createRef()
    }

    componentDidUpdate(prevProps) {
        if (this.props.editable !== prevProps.editable && this.props.editable) {
            this.ref.current.focus()
            moveCursorToEnd(this.ref.current)
        }
    }

    render() {
        const { editable, children, onChange } = this.props

        return editable ? (
            <div
                className="editable"
                ref={this.ref}
                contentEditable={editable}
                onInput={onChange}
            >
                {children}
            </div>
        ) : (
            children
        )
    }
}

ContentEditable.propTypes = {
    editable: PropTypes.bool,
    onChange: PropTypes.func,
    children: PropTypes.node,
}

ContentEditable.defaultProps = {
    editable: false,
    onChange: () => {},
    children: null,
}

export default ContentEditable
