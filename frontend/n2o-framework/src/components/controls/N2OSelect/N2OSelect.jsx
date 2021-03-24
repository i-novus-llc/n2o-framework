import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName } from 'recompose';
import onClickOutside from 'react-onclickoutside';
import isEqual from 'lodash/isEqual';
import get from 'lodash/get';
import first from 'lodash/first';
import isEmpty from 'lodash/isEmpty';
import find from 'lodash/find';
import filter from 'lodash/filter';
import Button from 'reactstrap/lib/Button';
import Popup from '../InputSelect/Popup';
import PopupList from '../InputSelect/PopupList';
import InputSelectGroup from '../InputSelect/InputSelectGroup';
import N2OSelectInput from './N2OSelectInput';
import declensionNoun from '../../../utils/declensionNoun';

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

const selectType = {
  SINGLE: 'single',
  CHECKBOXES: 'checkboxes',
};

class N2OSelect extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: '',
      isExpanded: false,
      options: this.props.options,
      selected: this._getSelected(this.props.value),
      hasCheckboxes: this.props.type === selectType.CHECKBOXES,
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

  componentDidMount() {
    const { initial, options, valueFieldId } = this.props;
    if (Array.isArray(initial)) {
      this._setStateFromInitial(initial, options, valueFieldId);
    }
  }

  componentWillReceiveProps(nextProps) {
    const { options } = this.props;
    let state = {};

    if (!isEqual(options, nextProps.options)) {
      state.options = nextProps.options;
    }
    this.setState(state);
  }

  componentDidUpdate(prevProps) {
    const { initial, options, valueFieldId, value } = this.props;

    if (Array.isArray(initial) && !isEqual(initial, prevProps.initial)) {
      this._setStateFromInitial(initial, options, valueFieldId);
      return;
    }
    if (!isEqual(value, prevProps.value)) {
      this.setState({
        selected: this._getSelected(value),
      });
    }
  }

  /**
   * Хак для мапинга айдишников, которые берутся из адресной строки в виде строк, но ожидается число
   * TODO удалить после того, как починится поведение парсинга адресной строки будет опираться на указанные типы
   * @param {Array.<object>} [initial]
   * @param options
   * @param {String} valueFieldId
   * @private
   */
  _setStateFromInitial(initial, options, valueFieldId) {
    const mapOptions = (data, type = 'string') => {
      return data.map(option => ({
        ...option,
        ...{
          [valueFieldId]:
            type === 'number'
              ? Number(option[valueFieldId])
              : String(option[valueFieldId]),
        },
      }));
    };

    if (isEmpty(options)) {
      this.setState({
        selected: mapOptions(initial),
      });
    } else {
      const selected = filter(
        options,
        option => {
          const idType = typeof option[valueFieldId];

          return find(mapOptions(initial, idType), option);
        },
        []
      );

      this.setState({
        selected: selected,
      });
    }
  }

  /**
   * @param {Array | string} [value]
   * @return {Array}
   * @private
   */
  _getSelected(value) {
    if (Array.isArray(value)) {
      return value;
    }
    if (value) {
      return [value];
    }
    return [];
  }

  /**
   * Удаляет элемент из списка выбранных
   * @param item - элемент
   * @private
   */

  _removeSelectedItem(item) {
    const { valueFieldId, onChange } = this.props;
    const selected = this.state.selected.filter(
      i => i[valueFieldId] !== item[valueFieldId]
    );
    this.setState({
      selected,
    });
    if (onChange) {
      onChange(selected);
    }
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

  _clearSelected(e) {
    e.stopPropagation();
    e.preventDefault();

    if (this.props.disabled) {
      return;
    }

    this.setState({
      selected: [],
    });
    this.props.onChange(null);
    this.props.onBlur(null);
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
    const { onChange, onBlur } = this.props;
    let selected = [item];
    let value = item;

    if (this.state.hasCheckboxes) {
      selected = [...this.state.selected, item];
      value = selected;
    }

    this.setState({
      selected
    });

    if (onChange) {
      onChange(value);
      onBlur(value);
    }
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

  renderPlaceholder() {
    const {
      selectFormat = 'Объектов {size} шт',
      selectFormatOne = '',
      selectFormatFew = '',
      selectFormatMany = '',
    } = this.props;

    const { selected, hasCheckboxes } = this.state;
    const selectedCount = selected.length;
    let text;

    if (
      !isEmpty(selectFormatOne) &&
      !isEmpty(selectFormatFew) &&
      !isEmpty(selectFormatMany) &&
      selectedCount >= 1 &&
      hasCheckboxes
    ) {
      text = declensionNoun(
        selectedCount,
        selectFormatOne,
        selectFormatFew,
        selectFormatMany
      ).replace('{size}', selectedCount);
    } else if (selectedCount >= 1 && hasCheckboxes) {
      text = selectFormat.replace('{size}', selectedCount);
    } else {
      text = null;
    }

    return text;
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
      descriptionFieldId,
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

    const title = get(first(selected), `${labelFieldId}`);

    return (
      <div
        className="n2o-input-select"
        title={title}
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
            {this.state.hasCheckboxes
              ? this.renderPlaceholder()
              : !isEmpty(selected) && selected[0][labelFieldId]}
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
              descriptionFieldId={descriptionFieldId}
              badgeColorFieldId={badgeColorFieldId}
              onSelect={this._handleItemSelect}
              onScrollEnd={onScrollEnd}
              isExpanded={this.state.isExpanded}
              selected={this.state.selected}
              disabledValues={disabledValues}
              groupFieldId={groupFieldId}
              hasCheckboxes={this.state.hasCheckboxes}
              onRemoveItem={this._removeSelectedItem}
              format={format}
              loading={loading}
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
  hasSearch: PropTypes.bool,
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
