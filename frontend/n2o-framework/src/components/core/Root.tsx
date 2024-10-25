import React, { ReactNode } from 'react'
import { pure } from 'recompose'

import OverlayPages from './OverlayPages'

function Root({ children }: { children: ReactNode }) {
    return (
        <>
            {children}
            <OverlayPages />
        </>
    )
}

export default pure(Root)
