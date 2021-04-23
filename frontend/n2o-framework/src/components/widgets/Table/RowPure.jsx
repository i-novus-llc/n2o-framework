/**
 * Created by emamoshin on 01.06.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import shouldUpdate from 'recompose/shouldUpdate'

/**
 * @deprecated Компонент больше не используется
 */
class Row extends React.Component {
    constructor(props) {
        super(props)
    }

    render() {
        return (
            <tr className={this.props.active ? 'table-active' : ''} {...this.props} />
        )
    }
}

const checkPropsChange = (props, nextProps) => nextProps.active !== props.active || nextProps.id !== props.id

export default shouldUpdate(checkPropsChange)(Row)
