// noinspection JSDeprecatedSymbols

/**
 * Created by emamoshin on 01.06.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import shouldUpdate from 'recompose/shouldUpdate'

/**
 * @deprecated Компонент больше не используется
 */
class Row extends React.PureComponent {
    render() {
        const { active } = this.props

        return (
            <tr className={active ? 'table-active' : ''} {...this.props} />
        )
    }
}

Row.propTypes = {
    active: PropTypes.bool,
}

const checkPropsChange = (props, nextProps) => nextProps.active !== props.active || nextProps.id !== props.id

// noinspection JSValidateTypes
export default shouldUpdate(checkPropsChange)(Row)
