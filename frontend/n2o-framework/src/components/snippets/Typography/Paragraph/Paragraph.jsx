import React from 'react'

import { Base } from '../Base'

function Paragraph({ ...rest }) {
    const tag = 'p'

    return <Base tag={tag} {...rest} />
}

export default Paragraph
