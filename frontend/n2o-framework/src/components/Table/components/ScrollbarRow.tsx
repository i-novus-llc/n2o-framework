import React, { RefObject } from 'react'
import { Scrollbar } from '@i-novus/n2o-components/lib/layouts/Scroll/Bar'

type Props = {
    cellType?: 'th' | 'td'
    targetRef: RefObject<HTMLElement>
    colSpan?: number
}

export const ScrollbarRow = ({
    cellType: Cell = 'td',
    targetRef,
    colSpan,
}: Props) => {
    return (
        <tr>
            <Cell className='cell-scrollbar' colSpan={colSpan}>
                <Scrollbar targetRef={targetRef} />
            </Cell>
        </tr>
    )
}
