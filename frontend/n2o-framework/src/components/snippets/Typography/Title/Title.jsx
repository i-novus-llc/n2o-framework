import React from 'react'

import { Base } from '../Base'

// eslint-disable-next-line react/prop-types
export function Title({ level, ...rest }) {
    const tag = `h${level}`

    return <Base tag={tag} {...rest} />
}

export default Title
