import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName } from 'recompose';
import onClickOutside from 'react-onclickoutside';
import { isEqual, isEmpty } from 'lodash';
import { Button } from 'reactstrap';
import Popup from '../InputSelect/Popup';
import PopupList from '../InputSelect/PopupList';
import InputSelectGroup from '../InputSelect/InputSelectGroup';
import N2OSelectInput from './N2OSelectInput';

/**
 * N2OSelect
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onChange - callback при выборе значения или вводе
 * @reactProps {function} onScrollEnd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {boolean} searchByTap - поиск по нажатию кнопки
 */

class N2OSelect extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: '',
      isExpanded: false,
      options: this.props.options,
      selected: this.props.value ? [this.props.value] : [],
    };

    this._control = null;

    this._handleButtonClick = this._handleButtonClick.bind(this);
    this._handleInputChange = this._handleInputChange.bind(this);
    this._handleInputFocus = this._handleInputFocus.bind(this);
    this._hideOptionsList = this._hideOptionsList.bind(this);
    this._handleItemSelect = this._handleItemSelect.bind(this);
    this._removeSelectedItem = this._removeSelectedItem.bind(this);
    this._clearSelected = this._clearSelected.bind(this);
    this._handleSearchButton = this._handleSearchButton.bind(this);
    this._handleOnBlur = this._handleOnBlur.bind(this);
    this.setControlRef = this.setControlRef.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    let selected = [];
    if (!isEqual(nextProps.value, this.props.value)) {
      if (nextProps.value) {
        selected = [nextProps.value];
      } else {
        selected = [];
      }
    } else {
      selected = this.state.selected;
    }

    this.setState({
      options: nextProps.options,
      selected,
    });
  }

  /**
   * Удаляет элемент из списка выбранных
   * @param item - элемент
   * @private
   */

  _removeSelectedItem(item) {
    const { valueFieldId } = this.props;
    this.setState({
      selected: this.state.selected.filter(
        i => i[valueFieldId] !== item[valueFieldId]
      ),
    });
  }

  /**
   * Изменение видимости попапа
   * @param newState - новое значение видимости
   * @private
   */

  _changePopUpVision(newIsExpanded) {
    const { onOpen, onClose } = this.props;
    const { isExpanded } = this.state;
    if (isExpanded === newIsExpanded) return;
    this.setState(
      {
        isExpanded: newIsExpanded,
      },
      newIsExpanded ? onOpen : onClose
    );
  }

  /**
   * Обрабатывает нажатие на кнопку
   * @private
   */

  _handleButtonClick() {
    if (!this.props.disabled) {
      this._changePopUpVision(!this.state.isExpanded);
    }
  }

  /**
   * Обрабатывает форкус на инпуте
   * @private
   */

  _handleInputFocus() {
    this._changePopUpVision(true);
  }

  /**
   * Скрывает popUp
   * @private
   */

  _hideOptionsList() {
    this._changePopUpVision(false);
  }

  /**
   * Уставнавливает новое значение инпута
   * @param newValue - новое значение
   * @private
   */

  _setNewValue(newValue) {
    this.setState({
      value: newValue,
    });
  }

  /**
   * Удаляет выбранные элементы
   * @private
   */

  _clearSelected() {
    if (!this.props.disabled) {
      this.setState({
        selected: [],
      });
      this.props.onChange(null);
      this.props.onBlur(null);
    }
  }

  /**
   * Выполняет поиск элементов для popUp, если установлен фильтр
   * @param newValue - значение для поиска
   * @private
   */

  _handleDataSearch(input, delay = true, callback) {
    const { onSearch, filter, options: data, labelFieldId } = this.props;
    if (filter) {
      const filterFunc = item =>
        String.prototype[this.props.filter].call(item, input);
      const options = data.filter(item =>
        filterFunc(item[labelFieldId].toString())
      );
      this.setState({ options: options });
    } else {
      onSearch(input, delay, callback);
    }
  }

  /**
   * Устанавливает выбранный элемент
   * @param item - элемент массива options
   * @private
   */

  _insertSelected(item) {
    this.setState({
      selected: [item],
    });
  }

  /**
   * Обрабатывает изменение инпута
   * @param newValue - новое значение
   * @private
   */

  _handleInputChange(newValue) {
    const { searchByTap, onChange, onInput, resetOnBlur } = this.props;

    this._setNewValue(newValue);

    !searchByTap && this._handleDataSearch(newValue);
    !resetOnBlur && onChange(newValue);
    onInput(newValue);
  }

  /**
   * Обрабатывает поиск по нажатию
   * @private
   */

  _handleSearchButton() {
    this._handleDataSearch(this.state.value);
  }

  /**
   * Очищает инпут и результаты поиска
   * @private
   */

  _clearSearchField() {
    this.setState({
      value: '',
      options: this.props.options,
    });
  }

  /**
   * Обрабатывает выбор элемента из popUp
   * @param item - элемент массива options
   * @private
   */

  _handleItemSelect(item) {
    this._insertSelected(item);

    if (this.props.closePopupOnSelect) {
      this._hideOptionsList();
    }

    this._clearSearchField();

    if (this.props.onChange) {
      this.props.onChange(item);
      this.props.onBlur(item);
    }

    if (this._control) {
      this._control.focus();
    }
  }

  /**
   * Обрабатывает поведение инпута при потери фокуса, если есть resetOnBlur
   * @private
   */

  _handleResetOnBlur() {
    if (this.props.resetOnBlur && !this.state.selected) {
      this.setState({
        value: '',
        options: this.props.options,
      });
    }
  }

  /**
   * Обрабатывает клик за пределы компонента
   * @param evt
   */

  handleClickOutside(evt) {
    this._hideOptionsList();
    this._handleResetOnBlur();
  }

  _handleOnBlur(e) {
    e.preventDefault();
    this._handleResetOnBlur();
  }

  setControlRef(el) {
    this._control = el;
  }

  /**
   * Рендер
   */

  render() {
    const {
      loading,
      className,
      valueFieldId,
      labelFieldId,
      iconFieldId,
      disabled,
      disabledValues,
      imageFieldId,
      groupFieldId,
      format,
      placeholder,
      badgeFieldId,
      badgeColorFieldId,
      onScrollEnd,
      hasSearch,
      cleanable,
      style,
    } = this.props;
    const inputSelectStyle = { width: '100%', ...style };

    const { selected } = this.state;

    return (
      <div
        className="n2o-input-select"
        style={inputSelectStyle}
        onBlur={this._handleOnBlur}
      >
        <Button innerRef={this.setControlRef} onClick={this._handleButtonClick}>
          <InputSelectGroup
            className={className}
            isExpanded={this.state.isExpanded}
            loading={loading}
            disabled={disabled}
            iconFieldId={iconFieldId}
            imageFieldId={imageFieldId}
            cleanable={cleanable}
            selected={this.state.selected}
            onClearClick={this._clearSelected}
          >
            {!isEmpty(selected) && selected[0][labelFieldId]}
          </InputSelectGroup>
        </Button>
        <Popup isExpanded={this.state.isExpanded}>
          <React.Fragment>
            {hasSearch && (
              <N2OSelectInput
                placeholder={placeholder}
                onChange={this._handleInputChange}
                onSearch={this._handleSearchButton}
                value={this.state.value}
              />
            )}
            <PopupList
              options={this.state.options}
              valueFieldId={valueFieldId}
              labelFieldId={labelFieldId}
              iconFieldId={iconFieldId}
              badgeFieldId={badgeFieldId}
              badgeColorFieldId={badgeColorFieldId}
              onSelect={this._handleItemSelect}
              onScrollEnd={onScrollEnd}
              isExpanded={this.state.isExpanded}
              selected={this.state.selected}
              disabledValues={disabledValues}
              groupFieldId={groupFieldId}
              hasCheckboxes={false}
              onRemoveItem={this._removeSelectedItem}
              format={format}
            />
          </React.Fragment>
        </Popup>
      </div>
    );
  }
}

N2OSelect.propTypes = {
  /**
   * Флаг загрузки
   */
  loading: PropTypes.bool,
  /**
   * Данные
   */
  options: PropTypes.array.isRequired,
  /**
   * Ключ id в данных
   */
  valueFieldId: PropTypes.string.isRequired,
  /**
   * Ключ label в данных
   */
  labelFieldId: PropTypes.string.isRequired,
  cleanable: PropTypes.bool,
  /**
   * Ключ icon в данных
   */
  iconFieldId: PropTypes.string,
  /**
   * Ключ image в данных
   */
  imageFieldId: PropTypes.string,
  /**
   * Ключ badge в данных
   */
  badgeFieldId: PropTypes.string,
  /**
   * Ключ badgeColor в данных
   */
  badgeColorFieldId: PropTypes.string,
  /**
   * Флаг активности
   */
  disabled: PropTypes.bool,
  /**
   * Неактивные данные
   */
  disabledValues: PropTypes.array,
  /**
   * Фильтрация
   */
  filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', false]),
  /**
   * Значение
   */
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  /**
   * Callback при вводе в инпут
   */
  onInput: PropTypes.func,
  /**
   * Callback на изменение
   */
  onChange: PropTypes.func,
  /**
   * Callback на изменение
   */
  onScrollEnd: PropTypes.func,
  /**
   * Placeholder контрола
   */
  placeholder: PropTypes.string,
  /**
   * Сброс значения при потере фокуса
   */
  resetOnBlur: PropTypes.bool,
  /**
   * Callback на открытие попапа
   */
  onOpen: PropTypes.func,
  /**
   * Callback на закрытие попапа
   */
  onClose: PropTypes.func,
  groupFieldId: PropTypes.string,
  /**
   * Формат
   */
  format: PropTypes.string,
  /**
   * Поиск по нажатию кнопки
   */
  searchByTap: PropTypes.bool,
  /**
   * Callback на поиск
   */
  onSearch: PropTypes.func,
  /**
   * Флаг наличия поиска
   */
  hasSearch: PropTypes.func,
};

N2OSelect.defaultProps = {
  parentFieldId: 'parentId',
  cleanable: true,
  valueFieldId: 'id',
  labelFieldId: 'name',
  iconFieldId: 'icon',
  imageFieldId: 'image',
  badgeFieldId: 'badge',
  loading: false,
  disabled: false,
  disabledValues: [],
  resetOnBlur: false,
  filter: false,
  searchByTap: false,
  hasSearch: false,
  onSearch() {},
  onChange() {},
  onScrollEnd() {},
  onInput() {},
  onOpen() {},
  onClose() {},
  onBlur() {},
};

export { N2OSelect };
export default compose(
  setDisplayName('N2OSelect'),
  onClickOutside
)(N2OSelect);
