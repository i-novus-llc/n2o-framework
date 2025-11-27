import React from 'react'

import { Factory } from '../../../core/factory/Factory'
import { ContentMeta } from '../../../ducks/regions/Regions'

interface LayoutContentProps {
    content?: ContentMeta[]
}

export function LayoutContent({ content }: LayoutContentProps) {
    return (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
            {content?.map((meta) => {
                return <Factory {...meta} />
            })}
        </>
    )
}
