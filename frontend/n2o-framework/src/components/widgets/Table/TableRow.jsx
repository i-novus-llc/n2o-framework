import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Строка таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model
 */
// eslint-disable-next-line react/prefer-stateless-function
class TableRow extends React.Component {
    // eslint-disable-next-line no-useless-constructor
    constructor(props) {
        super(props)
    }

    render() {
        const { color, className } = this.props

        return (
            <tr
                {...this.props}
                className={classNames(className, 'n2o-table-row', color)}
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
