import React from 'react'
import { FormattedText } from '@i-novus/n2o-components/lib/Typography/FormattedText'

import { type SubTextType } from './types'

export function SubText({ subText, format }: SubTextType) {
    if (Array.isArray(subText)) {
        return (
            // eslint-disable-next-line react/jsx-no-useless-fragment
            <>
                {subText?.map(text => (
                    <span key={text} className="text-muted"><FormattedText format={format}>{text}</FormattedText></span>
                ))}
            </>
        )
    }

    return <span className="text-muted"><FormattedText format={format}>{subText}</FormattedText></span>
}
