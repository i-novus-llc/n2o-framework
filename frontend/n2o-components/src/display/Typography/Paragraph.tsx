import React from 'react'

import { Base } from './Base'

export function Paragraph({ ...rest }) {
    const tag = 'p'

    return <Base tag={tag} {...rest} />
}
