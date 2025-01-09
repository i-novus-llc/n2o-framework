import React from 'react'

import { HeaderWrapperProps } from './types'

export function HeaderWrapper({ children, isValid }: HeaderWrapperProps) {
    // eslint-disable-next-line react/jsx-no-useless-fragment
    if (isValid) { return <>{children}</> }

    return <h3 className="n2o-widget-list-item-header">{children}</h3>
}
