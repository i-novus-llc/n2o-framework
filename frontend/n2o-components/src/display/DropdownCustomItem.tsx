import React from 'react'
import { Button } from 'reactstrap'
import omit from 'lodash/omit'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    [key: string]: any;
}

export function DropdownCustomItem(props: Props) {
    return <Button {...omit(props, ['color'])} className="dropdown-item-btn" />
}
