import React from 'react'

import Base from '../Base'

function Title({ level, ...rest }) {
    const tag = `h${level}`

    return <Base tag={tag} {...rest} />
}

export default Title
