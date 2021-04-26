import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

/**
 * Строка таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model
 */
class TableRow extends React.Component {
    constructor(props) {
        super(props)
    }

    render() {
        const { color, className } = this.props
        return (
            <tr
                {...this.props}
                className={cx(className, 'n2o-table-row', color)}
                tabIndex={1}
            />
        )
    }
}

TableRow.propTypes = {
    /* Default props */
    color: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.string,
    model: PropTypes.object,
    /* Specific props */
}

export default TableRow
