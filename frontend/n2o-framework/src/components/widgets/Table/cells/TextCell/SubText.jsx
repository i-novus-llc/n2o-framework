import React from 'react'
import map from 'lodash/map'
import isArray from 'lodash/isArray'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
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

SubText.propTypes = {
    subText: PropTypes.array,
    format: PropTypes.string,
}

export default SubText
