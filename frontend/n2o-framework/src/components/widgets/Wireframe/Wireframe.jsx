import React from 'react'

import Wireframe from '../../snippets/Wireframe/Wireframe'

export default ({ title, className, height }) => (
    <div style={{ position: 'relative', height, width: '100%' }}>
        <Wireframe className={className} title={title} />
    </div>
)
