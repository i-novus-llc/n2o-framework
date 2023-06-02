import React from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle, withHandlers, withState } from 'recompose'
import classNames from 'classnames'
import isEqual from 'lodash/isEqual'
import invoke from 'lodash/invoke'
import omit from 'lodash/omit'

import PopupItems from './PopupItems'
import { isBottom } from './utils'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {array} options - массив данных
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данны
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {object} badge - данные для баджа
 * @reactProps {string} enabledFieldId - поле для активности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} format - формат
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {array} selected - выбранные элементы
 * @reactProps {function} onSelect - callback при выборе элемента
 * @reactProps {function} onScrollEnd - callback при достижения конца прокрутки попапа
 * @reactProps {function} onRemoveItem - callback при удаление элемента
 * @reactProps {boolean} needAddFilter
 * @reactProps {node} children - элемент потомок компонента PopupList
 * @reactProps {function} handleMouseEnter - обработчик для события onMouseEnter
 * @reactProps {function} handleMouseLeave - обработчик для события onMouseLeave
 */

function PopupList({
    children,
    setMenuElement,
    handleMouseEnter,
    handleMouseLeave,
    style,
    ...rest
}) {
    return (
        <div className="n2o-pop-up__wrapper">
            <div
                style={style}
                className={classNames('n2o-dropdown-control n2o-pop-up')}
                ref={setMenuElement}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                {children}
                <PopupItems {...omit(rest, ['needAddFilter', 'filterValue', 'expandPopUp', 'onScrollEnd', 'isExpanded'])} />
            </div>
        </div>
    )
}

PopupList.propTypes = {
    onScrollEnd: PropTypes.func,
    options: PropTypes.array.isRequired,
    valueFieldId: PropTypes.string.isRequired,
    labelFieldId: PropTypes.string.isRequired,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    groupFieldId: PropTypes.string,
    badge: PropTypes.object,
    enabledFieldId: PropTypes.string,
    disabledValues: PropTypes.array,
    onSelect: PropTypes.func,
    selected: PropTypes.array,
    hasCheckboxes: PropTypes.bool,
    onRemoveItem: PropTypes.func,
    format: PropTypes.string,
    children: PropTypes.node,
    needAddFilter: PropTypes.bool,
    filterValue: PropTypes.any,
    setMenuElement: PropTypes.func,
    handleMouseEnter: PropTypes.func,
    handleMouseLeave: PropTypes.func,
}

const enhance = compose(
    withState('menuElement', 'setMenuElement', null),
    withHandlers({
        onScroll: ({ needAddFilter, filterValue, onScrollEnd, loading }) => (e) => {
            if (!loading && isBottom(e.target)) {
                onScrollEnd(needAddFilter ? filterValue : {})
            }
        },
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            if (!isEqual(prevProps.options, this.props.options)) {
                invoke(this.props, 'scheduleUpdate')
            }

            if (!prevProps.menuElement && this.props.menuElement) {
                this.props.menuElement.addEventListener('scroll', this.props.onScroll)
            }
        },

        componentWillUnmount() {
            if (this.props.menuElement) {
                this.props.menuElement.removeEventListener(
                    'scroll',
                    this.props.onScroll,
                )
            }
        },
    }),
)

export default enhance(PopupList)
