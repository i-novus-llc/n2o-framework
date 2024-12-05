import React, { ComponentType } from 'react'

import { id } from '../../../../../utils/id'

export const MODIFIERS = {
    preventOverflow: { boundariesElement: 'window' },
}

export const withUID = <T extends Record<string, unknown>>(Component: ComponentType<T>) => {
    return function Wrapper(props: T) {
        const uId = id()

        return <Component uId={uId} {...props} />
    }
}
