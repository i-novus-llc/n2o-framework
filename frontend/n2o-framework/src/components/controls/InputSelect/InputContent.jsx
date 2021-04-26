import React from 'react'
import find from 'lodash/find'
import reduce from 'lodash/reduce'
import split from 'lodash/split'
import isEqual from 'lodash/isEqual'
import assign from 'lodash/assign'
import cn from 'classnames'
import PropTypes from 'prop-types'
import ReactDOM from 'react-dom'

import SelectedItems from './SelectedItems'
import { getNextId, getPrevId, getFirstNotDisabledId } from './utils'

const textLengthStyle = {
    position: 'absolute',
    zIndex: -9999,
    opacity: 0,
    pointerEvents: 'none',
}

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

        this.selectedItemsRef = React.createRef()
        this._textRef = null

        this.state = {
            paddingTextArea: {},
            paddingLeft: { paddingLeft: 10 },
            notEnoughPlace: false,
        }
    }

    componentDidMount() {
        this.calcPaddingTextarea()
    }

    componentDidUpdate(prevProps, prevState) {
        if (
            !isEqual(prevProps.selected, this.props.selected) ||
      !isEqual(prevProps.value, this.props.value)
        ) {
            this.calcPaddingTextarea()

            if (this.props.multiSelect) {
                const defaultPaddingLeft = 10

                const selectedItemsWidth = this.selectedItemsRef
                    ? this.selectedItemsRef.current.clientWidth
                    : defaultPaddingLeft

                const currentSize =
          selectedItemsWidth === 0
              ? defaultPaddingLeft
              : selectedItemsWidth + 5

                this.setState({
                    paddingLeft: assign({}, this.state.paddingLeft, {
                        paddingLeft: currentSize,
                    }),
                })
            }
        }
        if (
            !isEqual(prevProps.value, this.props.value) &&
      !this.state.notEnoughPlace
        ) {
            this.checkTextOnEnoughPlace()
        }
    }

    getHeight(el) {
        return el.clientHeight
    }

    getWidth(el) {
        return el.clientWidth
    }

    getMargin(item, propertyName) {
        return +split(window.getComputedStyle(item)[propertyName], 'px')[0]
    }

  setTextRef = (el) => {
      this._textRef = el
  };

  onSelect = (item) => {
      this.props.onSelect(item)

      if (this.state.notEnoughPlace) {
          this.setState(prevState => ({
              notEnoughPlace: false,
              paddingTextArea: {
                  ...prevState.paddingTextArea,
                  paddingTop: prevState.paddingTextArea.paddingTop - 45,
              },
          }))
      }
  };

  checkTextOnEnoughPlace = () => {
      const { _textarea } = this.props
      if (!_textarea) { return }
      const textareaStyles = window.getComputedStyle(_textarea)
      const notEnoughPlace =
      _textarea.offsetWidth -
        (parseInt(textareaStyles.paddingLeft) +
          parseInt(textareaStyles.paddingRight)) <=
      this._textRef.offsetWidth

      if (notEnoughPlace) {
          this.setState(prevState => ({
              notEnoughPlace,
              paddingTextArea: {
                  paddingTop:
            prevState.paddingTextArea.paddingTop +
            45 * Math.ceil(this._textRef.offsetWidth / _textarea.offsetWidth),
                  paddingLeft: 12,
              },
          }))
      }

      return notEnoughPlace
  };

  calcPaddingTextarea() {
      const { _textarea, _selectedList, selected } = this.props
      if (_textarea && _selectedList) {
          let mainWidth
          let mainHeight
          const selectedList = ReactDOM.findDOMNode(_selectedList).querySelectorAll(
              '.selected-item',
          )
          mainWidth = reduce(
              selectedList,
              (acc, item) => {
                  const marginLeft = this.getMargin(item, 'margin-left')
                  const marginRight = this.getMargin(item, 'margin-right')
                  const newWidth = acc + item.offsetWidth + marginRight + marginLeft
                  if (newWidth >= this.getWidth(_selectedList)) {
                      acc = 0
                  }
                  return acc + item.offsetWidth + marginLeft + marginRight
              },
              0,
          )

          const lastItem = selectedList[selectedList.length - 1]

          if (lastItem) {
              mainHeight = this.getHeight(_textarea) - this.getHeight(lastItem)
          }

          if (!this.state.notEnoughPlace) {
              this.setState({
                  paddingTextArea: {
                      paddingTop: selected.length === 0 ? 5 : mainHeight,
                      paddingLeft: selected.length === 0 ? 10 : mainWidth || undefined,
                  },
              })
          }
      }
  }

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
          selectedPadding,
          setTextareaRef,
          setSelectedListRef,
          setRef,
          tags,
          mode,
          maxTagTextLength,
      } = this.props
      const { paddingTextArea, paddingLeft } = this.state
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

              const findEquals = find(
                  options,
                  item => item[valueFieldId] === activeValueId,
              )

              const newValue =
          mode === 'autocomplete' ? findEquals || value : findEquals

              if (newValue) {
                  this.onSelect(newValue)
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
      const inputEl = null

      const handleRef = (input) => {
          const el = input && ReactDOM.findDOMNode(input)
          if (el && isSelected) {
              el.select()
          } else if (el && inputFocus) {
              inputFocus && el.focus()
          }
      }

      const INPUT_STYLE = {
          paddingLeft: selectedPadding || undefined,
      }

      return (
          <>
              <span style={textLengthStyle} ref={this.setTextRef}>
                  {value}
              </span>
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
                          setRef={this.selectedItemsRef}
                          maxTagTextLength={maxTagTextLength}
                      />
                      <textarea
                          onKeyDown={handleKeyDown}
                          ref={setTextareaRef}
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
                          style={{
                              paddingTextArea,
                              ...paddingLeft,
                          }}
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
                      style={INPUT_STYLE}
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
