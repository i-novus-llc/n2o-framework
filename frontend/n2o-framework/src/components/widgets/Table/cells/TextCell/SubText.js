import React from 'react'
import map from 'lodash/map'
import isArray from 'lodash/isArray'

import Text from '../../../../snippets/Typography/Text/Text'

function SubText({ subText, format }) {
    return isArray(subText) ? (
        map(subText, (text, i) => (
            <Text key={i} className="text-muted" text={text} format={format} />
        ))
    ) : (
        <Text className="text-muted" text={subText} format={format} />
    )
}

export default SubText
