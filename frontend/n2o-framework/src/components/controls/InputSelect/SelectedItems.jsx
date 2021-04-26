import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import split from 'lodash/split'
import { useTranslation } from 'react-i18next'

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
    setRef,
    maxTagTextLength,
}) {
    const { t } = useTranslation()
    const selectedItem = (id, title, callback) => {
        const truncatedTitle = `${split(title, '', maxTagTextLength).join('')}...`

        const tagTitle =
      maxTagTextLength && title.length > maxTagTextLength
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
                    onClick={callback}
                    disabled={disabled}
                >
                    <i className="fa fa-times fa-1" />
                </button>
            </span>
        )
    }

    const selectedList = () => {
        if (collapseSelected && selected.length > lengthToGroup) {
            const id = selected.length
            const title = `${t('selected')} ${selected.length}`

            return selectedItem(id, title, onDeleteAll)
        }

        return map(selected, (item, index) => selectedItem(
            item.id || index,
            item[labelFieldId] || item,
            onRemoveItem.bind(null, item, index),
        ))
    }

    return (
        <div className="n2o-input-select-selected-list" ref={setRef}>
            {selectedList()}
        </div>
    )
}

InputElements.propTypes = {
    selected: PropTypes.array,
    labelFieldId: PropTypes.string,
    onRemoveItem: PropTypes.func,
    onDeleteAll: PropTypes.func,
    collapseSelected: PropTypes.bool,
    lengthToGroup: PropTypes.number,
    disabled: PropTypes.bool,
}

export default InputElements
