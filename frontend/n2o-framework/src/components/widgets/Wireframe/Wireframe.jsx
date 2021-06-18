import React from 'react'
import PropTypes from 'prop-types'

import SnippetWireframe from '../../snippets/Wireframe/Wireframe'

const Wireframe = ({ title, className, height }) => (
    <div style={{ position: 'relative', height, width: '100%' }}>
        <SnippetWireframe className={className} title={title} />
    </div>
)

Wireframe.propTypes = {
    className: PropTypes.string,
    title: PropTypes.string,
    height: PropTypes.string,
}

export default Wireframe
