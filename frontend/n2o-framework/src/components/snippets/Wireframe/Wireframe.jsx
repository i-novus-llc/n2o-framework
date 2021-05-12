import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Компонент WireFrame
 * @reactProps {string} className - класс компонента WireFrame
 * @reactProps {string} title - текст тайтла
 * @reactProps {object} style - стили компонента WireFrame
 */
function Wireframe({ style, className, title }) {
    return (
        <div style={style} className={classNames('n2o-wireframe', className)}>
            {title}
        </div>
    )
}

Wireframe.propTypes = {
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Заголовок заглушки
     */
    title: PropTypes.string,
    /**
     * Стили заглушки
     */
    style: PropTypes.object,
}

export default Wireframe
