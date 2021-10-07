import React, { useCallback, useMemo } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import split from 'lodash/split'
import { useTranslation } from 'react-i18next'

const SelectedItem = ({ id, title, callback, maxTagTextLength, disabled }) => {
    const callbackWrapper = useCallback((event) => {
        event.stopPropagation()
        callback(event)
    }, [callback])
    const truncatedTitle = `${split(title, '', maxTagTextLength).join('')}...`
    const tagTitle = maxTagTextLength && title.length > maxTagTextLength
        ? truncatedTitle
        : title

    return (
        <span
            key={id}
            className={classNames('selected-item n2o-multiselect', {
                'max-text-length': maxTagTextLength,
            })}
            title={title}
        >
            <span className="n2o-eclipse-content">{tagTitle}</span>
            <button
                type="button"
                className="close"
                onClick={callbackWrapper}
                disabled={disabled}
            >
                <i className="fa fa-times fa-1" />
            </button>
        </span>
    )
}

SelectedItem.propTypes = {
    id: PropTypes.string,
    title: PropTypes.string,
    callback: PropTypes.func,
    maxTagTextLength: PropTypes.number,
    disabled: PropTypes.bool,
}

const ItemWrapper = ({ onRemoveItem, item, index, ...props }) => {
    const onRemove = useMemo(
        () => (...args) => onRemoveItem.call(null, item, index, ...args),
        [item, index, onRemoveItem],
    )

    return (
        <SelectedItem
            {...props}
            callback={onRemove}
        />
    )
}

ItemWrapper.propTypes = {
    id: PropTypes.string,
    title: PropTypes.string,
    onRemoveItem: PropTypes.func,
    maxTagTextLength: PropTypes.number,
    disabled: PropTypes.bool,
    item: PropTypes.any,
    index: PropTypes.number,
}

/**
 * Компонент выбранных элементов для {@Link InputSelectGroup}
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {function} onRemoveItem - callback при нажатии на удаление элемента из выбранных при мульти выборе
 * @reactProps {function} onDeleteAll - callback удаление всех выбранных элементов при мульти выборе
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {number} maxTagTextLength - максимальная длина текста в тэге, до усечения
 */

function InputElements({
    onRemoveItem,
    selected,
    labelFieldId,
    disabled,
    collapseSelected,
    lengthToGroup,
    onDeleteAll,
    maxTagTextLength,
}) {
    const { t } = useTranslation()

    if (collapseSelected && selected.length > lengthToGroup) {
        const id = selected.length
        const title = `${t('selected')} ${selected.length}`

        return (
            <SelectedItem
                id={id}
                title={title}
                callback={onDeleteAll}
                maxTagTextLength={maxTagTextLength}
                disabled={disabled}
            />
        )
    }

    const list = map(selected, (item, index) => (
        <ItemWrapper
            id={item.id || index}
            title={item[labelFieldId] || item}
            maxTagTextLength={maxTagTextLength}
            disabled={disabled}
            item={item}
            index={index}
            onRemoveItem={onRemoveItem}
        />
    ))

    return <>{list}</>
}

InputElements.propTypes = {
    selected: PropTypes.array,
    labelFieldId: PropTypes.string,
    onRemoveItem: PropTypes.func,
    onDeleteAll: PropTypes.func,
    collapseSelected: PropTypes.bool,
    lengthToGroup: PropTypes.number,
    maxTagTextLength: PropTypes.number,
    disabled: PropTypes.bool,
}

export default InputElements
