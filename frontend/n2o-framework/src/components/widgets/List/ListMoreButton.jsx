import React from 'react'
import Button from 'reactstrap/lib/Button'
import PropTypes from 'prop-types'

ListMoreButton.propTypes = {
    onClick: PropTypes.func,
    style: PropTypes.object,
}

ListMoreButton.defaultProps = {
    onClick: () => {},
}

/**
 * Кнопка "Загрузить еще"
 * @param {function} onClick - callback на клик по кнопке
 * @param {object} style - стили кнопки
 * @returns {*}
 * @constructor
 */
function ListMoreButton({ onClick, style }) {
    return (
        <div style={style} className="n2o-widget-list-more-button">
            <Button onClick={onClick}>Загрузить еще</Button>
        </div>
    )
}

export default ListMoreButton
