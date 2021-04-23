import React from 'react'
import PropTypes from 'prop-types'
import { pure } from 'recompose'

import StandardField from './fields/StandardField/StandardField'

/**
 * Поле формы {@link Form}
 * @reactProps {node} component - компонент, который оборачивает поле. Дефолт: {@link StandardField}
 * @reactProps {node} control - контрол, который передастся в компонент (остальные пропсы тоже передаются в компонент)
 * @example
 * <Field component={CustomField} className="foo bar"/>
 */
class Field extends React.Component {
    render() {
        const { component, ...props } = this.props
        return React.createElement(component, props)
    }
}

Field.defaultProps = {
    component: StandardField,
}

Field.propTypes = {
    component: PropTypes.node,
}

export default pure(Field)
