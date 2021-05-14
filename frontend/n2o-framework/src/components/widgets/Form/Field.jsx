import React, { memo } from 'react'
import PropTypes from 'prop-types'

import StandardField from './fields/StandardField/StandardField'

/**
 * Поле формы {@link Form}
 * @reactProps {node} component - компонент, который оборачивает поле. Дефолт: {@link StandardField}
 * @reactProps {node} control - контрол, который передастся в компонент (остальные пропсы тоже передаются в компонент)
 * @example
 * <Field component={CustomField} className="foo bar"/>
 */
function Field(props) {
    const { component, ...rest } = props

    return React.createElement(component, rest)
}

Field.defaultProps = {
    component: StandardField,
}

Field.propTypes = {
    component: PropTypes.node,
}

export default memo(Field)
