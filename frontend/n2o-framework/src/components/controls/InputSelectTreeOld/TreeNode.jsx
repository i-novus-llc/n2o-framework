import React from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'

import ListItem from '../InputSelect/ListItem'

/**
 * Элемент дерева
 * @reactProps {boolean} expanded - флаг видимости попапа
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджа
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {object} item - данные для элемента
 * @reactProps {boolean} selected - флаг того, что элемент выбран
 * @reactProps {boolean} disabled - флаг неактивности элемента
 * @reactProps {boolean} selectable - флаг возможности выбрать элемент
 * @reactProps {function} handleSelect - callback при выборе элемента
 * @reactProps {function} handleDelete - callback при удаление элемента
 * @reactProps {boolean} hasChildren - флаг наличия детей
 * @reactProps {function} onExpandClick - callback при раскрытии/закрытии поддерева
 * @reactProps {function} handleFocus - callback при наведении на элемент
 * @reactProps {boolean} active - наведён ли на элемент курсор
 */

function TreeNode({
    children,
    item,
    disabled,
    selected,
    hasChildren,
    expanded,
    onDelete,
    onSelect,
    onExpandClick,
    ...rest
}) {
    /**
     * Обработчик нажатия на элемент дерева
     * @private
     */
    const handleClick = (e) => {
        e.stopPropagation()
        if (selected) {
            onDelete(item)
        } else {
            onSelect(item)
        }
    }

    /**
     * Обработчик нажатия на кнопку раскрытия
     * @param e - событие
     * @private
     */
    const handleChevronClick = (e) => {
        e.stopPropagation()
        onExpandClick(item)
    }

    const iconClass = expanded ? 'down' : 'right'

    return (
        <ui
            id={uniqueId('n2o-tree-select-item_')}
            className={classNames('n2o-tree-select-item', { 'tree-childs': true })}
        >
            <li id={uniqueId('n2o-tree-select_')} className="n2o-tree-select">
                {hasChildren ? (
                    <span onClick={handleChevronClick} className="tree-toggle">
                        <i className={`fa fa-chevron-${iconClass}`} aria-hidden="true" />
                    </span>
                ) : (
                    <span className="tree-toggle nothing" />
                )}
                <ListItem
                    onClick={handleClick}
                    item={item}
                    selected={selected}
                    handleCheckboxSelect={handleClick}
                    {...rest}
                />
            </li>
            <li
                style={{
                    display: expanded ? 'block' : 'none',
                }}
            >
                {children}
            </li>
        </ui>
    )
}

TreeNode.propTypes = {
    children: PropTypes.any,
    onDelete: PropTypes.func,
    onSelect: PropTypes.func,
    expanded: PropTypes.bool,
    hasCheckboxes: PropTypes.bool,
    imageFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    item: PropTypes.object,
    selected: PropTypes.bool,
    format: PropTypes.string,
    disabled: PropTypes.bool,
    selectable: PropTypes.bool,
    handleSelect: PropTypes.func,
    handleDelete: PropTypes.func,
    hasChildren: PropTypes.bool,
    onExpandClick: PropTypes.func,
    handleFocus: PropTypes.func,
    active: PropTypes.bool,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
}

TreeNode.defaultProps = {
    expanded: false,
    disabled: false,
    selectable: true,
    selected: false,
    // eslint-disable-next-line react/default-props-match-prop-types
    indeterminate: false,
    active: false,
}

export default TreeNode
