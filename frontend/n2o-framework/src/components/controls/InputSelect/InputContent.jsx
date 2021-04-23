import React from 'react'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import cn from 'classnames'
import PropTypes from 'prop-types'

import SelectedItems from './SelectedItems'
import { getNextId, getPrevId, getFirstNotDisabledId } from './utils'

/**
 * InputSelectGroup
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {string} value - текущее значение инпута
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {function} onRemoveItem - callback при нажатии на удаление элемента из выбранных при мульти выборе
 * @reactProps {function} onFocus - событие фокуса
 * @reactProps {function} onBlur - событие потери фокуса
 * @reactProps {node} inputFocus - элемент на котором произошло событие фокуса
 * @reactProps {boolean} isSelected
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} valueFieldId
 * @reactProps {function} clearSelected - callback удаление всех выбранных элементов при мульти выборе
 * @reactProps {boolean} multiSelect - фдаг мульти выбора
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {number} maxTagTextLength - максимальная длина текста в тэге, до усечения
 * @reactProps {function} onInputChange - callback при изменение инпута
 * @reactProps {function} openPopUp - открытие попапа
 * @reactProps {function} closePopUp - закрытие попапа
 * @reactProps {string} activeValueId
 * @reactProps {function} setActiveValueId
 * @reactProps {array} disabledValues
 * @reactProps {object} options
 * @reactProps {function} onSelect - событие выбора
 * @reactProps {function} onClick - событие клика
 * @reactProps {boolean} isExpanded - флаг видимости popUp
 */

class InputContent extends React.Component {
    constructor(props) {
        super(props)

        this._textRef = null
    }

  setTextRef = (el) => {
      this._textRef = el
  };

  onSelect = (item) => {
      this.props.onSelect(item)
  };

  render() {
      const {
          disabled,
          value,
          placeholder,
          onRemoveItem,
          onFocus,
          onBlur,
          inputFocus,
          isSelected,
          selected,
          labelFieldId,
          valueFieldId,
          clearSelected,
          multiSelect,
          collapseSelected,
          lengthToGroup,
          onInputChange,
          openPopUp,
          closePopUp,
          activeValueId,
          setActiveValueId,
          disabledValues,
          options,
          onClick,
          isExpanded,
          autoFocus,
          setSelectedListRef,
          setRef,
          tags,
          mode,
          maxTagTextLength,
      } = this.props
      /**
     * Обработчик изменения инпута при нажатии на клавишу
     * @param e - событие изменения
     * @private
     */
      const handleKeyDown = (e) => {
          if (
              multiSelect &&
        e.key === 'Backspace' &&
        selected.length &&
        !e.target.value
          ) {
              const endElementOfSelect = selected[selected.length - 1]
              onRemoveItem(endElementOfSelect)
          } else if (e.key === 'ArrowDown') {
              e.preventDefault()
              if (!isExpanded) {
                  openPopUp(true)
                  setActiveValueId(
                      getFirstNotDisabledId(
                          options,
                          selected,
                          disabledValues,
                          valueFieldId,
                      ),
                  )
              } else if (activeValueId) {
                  setActiveValueId(
                      getNextId(
                          options,
                          activeValueId,
                          valueFieldId,
                          selected,
                          disabledValues,
                      ),
                  )
              } else {
                  setActiveValueId(
                      getFirstNotDisabledId(
                          options,
                          selected,
                          disabledValues,
                          valueFieldId,
                      ),
                  )
              }
          } else if (e.key === 'ArrowUp') {
              e.preventDefault()
              setActiveValueId(
                  getPrevId(
                      options,
                      activeValueId,
                      valueFieldId,
                      selected,
                      disabledValues,
                  ),
              )
          } else if (e.key === 'Enter') {
              e.preventDefault()

              const findEquals = find(options, item => (
                  String(item[labelFieldId]) === value &&
            !selected.find(entity => isEqual(entity, item))
              ))

              if (mode === 'autocomplete') {
                  const newSelected = findEquals || value

                  this.onSelect(newSelected)
                  setActiveValueId(null)
              } else if (!isEmpty(findEquals)) {
                  this.onSelect(findEquals)
                  setActiveValueId(null)
              }
          } else if (e.key === 'Escape') {
              closePopUp(false)
          }
      }

      const handleClick = ({ target }) => {
          target.select()
          onClick && onClick()
      }

      /**
     * Обработчик изменения инпута
     * @param e - событие изменения
     * @private
     */

      const handleInputChange = (e) => {
          onInputChange(e.target.value)

          if (tags) {
              setActiveValueId(null)
          }
      }

      const getPlaceholder = selected.length > 0 ? '' : placeholder

      return (
          <>
              {multiSelect ? (
                  <>
                      <SelectedItems
                          selected={selected}
                          labelFieldId={labelFieldId}
                          onRemoveItem={onRemoveItem}
                          onDeleteAll={clearSelected}
                          disabled={disabled}
                          collapseSelected={collapseSelected}
                          lengthToGroup={lengthToGroup}
                          maxTagTextLength={maxTagTextLength}
                      />
                      <textarea
                          onKeyDown={handleKeyDown}
                          ref={setRef}
                          placeholder={getPlaceholder}
                          disabled={disabled}
                          value={value}
                          onChange={handleInputChange}
                          onClick={handleClick}
                          onFocus={onFocus}
                          onBlur={onBlur}
                          className={cn('form-control n2o-inp', {
                              'n2o-inp--multi': multiSelect,
                          })}
                          autoFocus={autoFocus}
                      />
                  </>
              ) : (
                  <input
                      onKeyDown={handleKeyDown}
                      ref={setRef}
                      placeholder={getPlaceholder}
                      disabled={disabled}
                      value={value}
                      onChange={handleInputChange}
                      onClick={handleClick}
                      onFocus={onFocus}
                      onBlur={onBlur}
                      type="text"
                      className="form-control n2o-inp"
                      autoFocus={autoFocus}
                  />
              )}
          </>
      )
  }
}

InputContent.propTypes = {
    isExpanded: PropTypes.bool,
    disabled: PropTypes.bool,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    placeholder: PropTypes.string,
    onInputChange: PropTypes.func,
    selected: PropTypes.array,
    labelFieldId: PropTypes.string,
    onRemoveItem: PropTypes.func,
    clearSelected: PropTypes.func,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    inputFocus: PropTypes.node,
    multiSelect: PropTypes.bool,
    collapseSelected: PropTypes.bool,
    lengthToGroup: PropTypes.number,
    openPopUp: PropTypes.func,
    closePopUp: PropTypes.func,
    activeValueId: PropTypes.string,
    setActiveValueId: PropTypes.func,
    disabledValues: PropTypes.array,
    options: PropTypes.object,
    onSelect: PropTypes.func,
    onClick: PropTypes.func,
    isSelected: PropTypes.bool,
    valueFieldId: PropTypes.string,
    autoFocus: PropTypes.bool,
    maxTagTextLength: PropTypes.number,
}

InputContent.defaultProps = {
    multiSelect: false,
    disabled: false,
    collapseSelected: true,
    autoFocus: false,
}

export default InputContent
