import React from 'react'

import { Text } from '../../../../snippets/Typography/Text/Text'

import { type SubTextType } from './types'

export function SubText({ subText, format }: SubTextType) {
    if (Array.isArray(subText)) {
        return (
            // eslint-disable-next-line react/jsx-no-useless-fragment
            <>
                {subText?.map((text, index) => (
                    // eslint-disable-next-line react/no-array-index-key
                    <Text key={index} className="text-muted" text={text} format={format} />
                ))}
            </>
        )
    }

    return <Text className="text-muted" text={subText} format={format} />
}

export default SubText
