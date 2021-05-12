import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import classNames from 'classnames'

import OutputListItem from './OutputListItem'

function OutputList({ value, className, direction, ...rest }) {
    const directionClassName = `n2o-output-list--${direction}`

    return (
        <ul
            className={classNames('n2o-output-list', directionClassName, {
                [className]: className,
            })}
        >
            {map(value, (item, index) => (
                <OutputListItem
                    key={index}
                    {...rest}
                    {...item}
                    isLast={index === value.length - 1}
                />
            ))}
        </ul>
    )
}

OutputList.propTypes = {
    /**
     * элементы OutputList, строки или ссылки
     */
    value: PropTypes.array,
    /**
     * кастомный класс контейнера
     */
    className: PropTypes.string,
    /**
     * id по которому из value берется текст row или link
     */
    labelFieldId: PropTypes.string,
    /**
     * id по которому из value берется href для link
     */
    linkFieldId: PropTypes.string,
    /**
     * Тип ссылки
     */
    target: PropTypes.string,
    /**
     * направление OutputList. row - элементы в строку. column(default) - элементы списком
     */
    direction: PropTypes.string,
    /**
     * разделитель между элементами (space default)
     */
    separator: PropTypes.string,
}

OutputList.defaultProps = {
    value: [],
    labelFieldId: 'name',
    linkFieldId: 'href',
    target: '_blank',
    direction: 'column',
    separator: '',
}

export default OutputList
