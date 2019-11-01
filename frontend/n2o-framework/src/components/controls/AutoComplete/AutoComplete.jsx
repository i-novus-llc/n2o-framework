import React from 'react';
import PropTypes from 'prop-types';
import {
  find,
  isEmpty,
  isFunction,
  get,
  filter,
  includes,
  isEqual,
  map,
  isArray,
  isString,
  isNil,
} from 'lodash';
import { compose } from 'recompose';
import listContainer from '../listContainer';
import onClickOutside from 'react-onclickoutside';
import cn from 'classnames';

import InputContent from '../InputSelect/InputContent';
import InputSelectGroup from '../InputSelect/InputSelectGroup';

import { Manager, Reference, Popper } from 'react-popper';
import { MODIFIERS } from '../DatePicker/utils';
import PopupList from '../InputSelect/PopupList';
import Alert from '../../snippets/Alerts/Alert';

class AutoComplete extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isExpanded: false,
      value: [],
      input: props.value && !props.tags ? props.value : '',
      activeValueId: null,
    };

    this._input = null;
    this._textarea = null;
  }

  componentDidMount = () => {
    const { value, tags } = this.props;

    if (!isEmpty(value)) {
      this.setState({
        value: isArray(value) ? value : value ? [value] : [],
        input: value && !tags ? value : '',
      });
    }
  };

  componentDidUpdate = prevProps => {
    const { value, options, tags } = this.props;

    if (!isEqual(prevProps.options, options)) {
      this.setState({ options });
    }

    if (prevProps.value !== value) {
      this.setState({
        value: isArray(value) ? value : value ? [value] : [],
        input: value && !tags ? value : '',
      });
    }
  };

  handleClickOutside = () => {
    const { onBlur } = this.props;
    const { isExpanded } = this.state;
    if (isExpanded) {
      this._setIsExpanded(false);
      onBlur();
    }
  };

  calcPopperWidth = () => {
    const { _input, _textarea } = this;
    const { popupAutoSize } = this.props;
    if ((_input || _textarea) && !popupAutoSize) {
      return _input
        ? _input.getBoundingClientRect().width
        : _textarea.getBoundingClientRect().width;
    }
  };

  _setIsExpanded = isExpanded => {
    const { disabled, onToggle, onClose, onOpen } = this.props;
    const { isExpanded: previousIsExpanded } = this.state;
    if (!disabled && isExpanded !== previousIsExpanded) {
      this.setState({ isExpanded });
      onToggle(isExpanded);
      isExpanded ? onOpen() : onClose();
    }
  };

  setInputRef = popperRef => {
    return r => {
      this._input = r;
      popperRef(r);
    };
  };

  setTextareaRef = poperRef => {
    return r => {
      this._textarea = r;
      poperRef(r);
    };
  };

  setSelectedListRef = selectedList => {
    this._selectedList = selectedList;
  };

  onFocus = () => {
    const { openOnFocus } = this.props;

    if (openOnFocus) {
      this._setIsExpanded(true);
    }
  };

  onClick = () => {
    this._setIsExpanded(true);
  };

  _handleDataSearch = (input, delay = 400, callback) => {
    const { onSearch, filter, valueFieldId, options } = this.props;

    if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter)) {
      const filterFunc = item => String.prototype[filter].call(item, input);
      const filteredData = filter(options, item =>
        filterFunc(item[valueFieldId])
      );
      this.setState({ options: filteredData });
    } else {
      //серверная фильтрация
      const labels = map(this.state.value, item => item[valueFieldId]);
      if (labels.some(label => label === input)) {
        onSearch('', delay, callback);
      } else {
        onSearch(input, delay, callback);
      }
    }
  };

  onChange = input => {
    const { onInput } = this.props;
    const onSetNewInputValue = input => {
      onInput(input);
      this._handleDataSearch(input);
    };

    if (!isEqual(this.state.input, input)) {
      this.setState({ input }, () => onSetNewInputValue(input));
    }
  };

  onBlur = () => {
    const { onBlur } = this.props;
    const { value } = this.state;

    if (isFunction(onBlur)) {
      onBlur(value);
    }
  };

  onSelect = item => {
    const { valueFieldId, onChange, closePopupOnSelect, tags } = this.props;
    const value = isString(item) ? item : get(item, valueFieldId);

    this.setState(
      prevState => ({
        value: tags ? [...prevState.value, value] : [value],
        input: !tags ? value : '',
      }),
      () => {
        if (closePopupOnSelect) {
          this._setIsExpanded(false);
        }

        if (isString(value)) {
          this.forceUpdate();
        }

        onChange(value);
      }
    );
  };

  _handleElementClear = () => {
    const { onChange, onBlur } = this.props;

    this.setState(
      {
        input: '',
        value: [],
      },
      () => {
        this._handleDataSearch(this.state.input);
        onChange(this.state.value);
        onBlur(null);
      }
    );
  };

  _setActiveValueId = activeValueId => {
    this.setState({ activeValueId });
  };

  _removeSelectedItem = (item, index = null) => {
    const { onChange } = this.props;
    const { value } = this.state;
    let newValue = value.slice();

    if (!isNil(index)) {
      newValue.splice(index, 1);
    } else {
      newValue = value.slice(0, value.length - 1);
    }

    this.setState({ value: newValue }, () => {
      onChange(value);
      this.forceUpdate();
    });
  };

  render() {
    const { isExpanded, value, activeValueId, input } = this.state;
    const {
      loading,
      className,
      valueFieldId,
      iconFieldId,
      disabled,
      placeholder,
      disabledValues,
      imageFieldId,
      groupFieldId,
      hasCheckboxes,
      format,
      badgeFieldId,
      badgeColorFieldId,
      onScrollEnd,
      style,
      alerts,
      autoFocus,
      options,
      data,
      tags,
    } = this.props;
    const needAddFilter = !find(value, item => item[valueFieldId] === input);
    const optionsList = !isEmpty(data) ? data : options;
    const filteredOptions = filter(
      optionsList,
      item => includes(item[valueFieldId], input) || isEmpty(input)
    );

    return (
      <div
        className={cn(
          'n2o-autocomplete w-100 n2o-input-select n2o-input-select--default',
          className
        )}
        style={style}
      >
        <Manager>
          <Reference>
            {({ ref }) => (
              <InputSelectGroup
                withoutButtons={true}
                isExpanded={isExpanded}
                setIsExpanded={this._setIsExpanded}
                loading={loading}
                selected={value}
                iconFieldId={iconFieldId}
                imageFieldId={imageFieldId}
                multiSelect={tags}
                disabled={disabled}
                className={className}
                setSelectedItemsRef={this.setSelectedItemsRef}
                input={input}
                onClearClick={this._handleElementClear}
              >
                <InputContent
                  tags={true}
                  multiSelect={tags}
                  options={filteredOptions}
                  setRef={this.setInputRef(ref)}
                  onInputChange={this.onChange}
                  setActiveValueId={this._setActiveValueId}
                  closePopUp={() => this._setIsExpanded(false)}
                  openPopUp={() => this._setIsExpanded(true)}
                  selected={value}
                  value={input}
                  onFocus={this.onFocus}
                  onClick={this.onClick}
                  onBlur={this.onBlur}
                  onRemoveItem={this._removeSelectedItem}
                  isExpanded={isExpanded}
                  setTextareaRef={this.setTextareaRef(ref)}
                  setSelectedListRef={this.setSelectedListRef}
                  valueFieldId={valueFieldId}
                  activeValueId={activeValueId}
                  onSelect={this.onSelect}
                  loading={loading}
                  disabled={disabled}
                  disabledValues={disabledValues}
                  placeholder={placeholder}
                  iconFieldId={iconFieldId}
                  imageFieldId={imageFieldId}
                  labelFieldId={valueFieldId}
                  autoFocus={autoFocus}
                  _textarea={this._textarea}
                  _selectedList={this._selectedList}
                />
              </InputSelectGroup>
            )}
          </Reference>
          {isExpanded && (
            <Popper
              placement="bottom-start"
              modifiers={MODIFIERS}
              positionFixed={true}
            >
              {({ ref, style, placement, scheduleUpdate }) => (
                <div
                  ref={ref}
                  style={{
                    ...style,
                    minWidth: this.calcPopperWidth(),
                    maxWidth: 600,
                  }}
                  data-placement={placement}
                  className="n2o-pop-up"
                >
                  <PopupList
                    scheduleUpdate={scheduleUpdate}
                    autocomplete={true}
                    isExpanded={isExpanded}
                    activeValueId={activeValueId}
                    setActiveValueId={this._setActiveValueId}
                    onScrollEnd={onScrollEnd}
                    needAddFilter={needAddFilter}
                    options={filteredOptions}
                    valueFieldId={valueFieldId}
                    labelFieldId={valueFieldId}
                    iconFieldId={iconFieldId}
                    imageFieldId={imageFieldId}
                    badgeFieldId={badgeFieldId}
                    badgeColorFieldId={badgeColorFieldId}
                    onSelect={this.onSelect}
                    selected={value}
                    disabledValues={disabledValues}
                    groupFieldId={groupFieldId}
                    hasCheckboxes={hasCheckboxes}
                    format={format}
                  >
                    <div className="n2o-alerts">
                      {alerts &&
                        alerts.map(alert => (
                          <Alert
                            key={alert.id}
                            onDismiss={() => this.props.onDismiss(alert.id)}
                            {...alert}
                          />
                        ))}
                    </div>
                  </PopupList>
                </div>
              )}
            </Popper>
          )}
        </Manager>
      </div>
    );
  }
}

AutoComplete.propTypes = {
  /**
   * Стили
   */
  style: PropTypes.object,
  /**
   * Флаг загрузки
   */
  loading: PropTypes.bool,
  /**
   * Массив данных
   */
  options: PropTypes.array.isRequired,
  /**
   * Ключ значения
   */
  valueFieldId: PropTypes.string.isRequired,
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
   * Ключ цвета badgeColor в данных
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
   * Значение
   */
  value: PropTypes.oneOfType([PropTypes.array, PropTypes.string]),
  /**
   * Callback на переключение
   */
  onToggle: PropTypes.func,
  onInput: PropTypes.func,
  /**
   * Callback на изменение
   */
  onChange: PropTypes.func,
  /**
   * Callback на выбор
   */
  onSelect: PropTypes.func,
  /**
   * Callback на скрол в самый низ
   */
  onScrollEnd: PropTypes.func,
  /**
   * Placeholder контрола
   */
  placeholder: PropTypes.string,
  /**
   * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
   */
  resetOnBlur: PropTypes.bool,
  /**
   * Callback на открытие
   */
  onOpen: PropTypes.func,
  /**
   * Callback на закрытие
   */
  onClose: PropTypes.func,
  /**
   * Мульти выбор значений
   */
  multiSelect: PropTypes.bool,
  /**
   * Поле для группировки
   */
  groupFieldId: PropTypes.string,
  /**
   * Флаг закрытия попапа при выборе
   */
  closePopupOnSelect: PropTypes.bool,
  /**
   * Флаг наличия чекбоксов в селекте
   */
  hasCheckboxes: PropTypes.bool,
  /**
   * Формат
   */
  format: PropTypes.string,
  /**
   * Callback на поиск
   */
  onSearch: PropTypes.func,
  expandPopUp: PropTypes.bool,
  alerts: PropTypes.array,
  /**
   * Авто фокусировка на селекте
   */
  autoFocus: PropTypes.bool,
  /**
   * Флаг авто размера попапа
   */
  popupAutoSize: PropTypes.bool,
  /**
   * Мод работы Autocomplete
   */
  tags: PropTypes.bool,
};

AutoComplete.defaultProps = {
  valueFieldId: 'label',
  iconFieldId: 'icon',
  imageFieldId: 'image',
  badgeFieldId: 'badge',
  loading: false,
  disabled: false,
  disabledValues: [],
  resetOnBlur: false,
  filter: false,
  multiSelect: false,
  closePopupOnSelect: true,
  hasCheckboxes: false,
  expandPopUp: false,
  flip: false,
  autoFocus: false,
  popupAutoSize: false,
  tags: false,
  onSearch() {},
  onSelect() {},
  onToggle() {},
  onInput() {},
  onOpen() {},
  onClose() {},
  onChange() {},
  onScrollEnd() {},
  onBlur() {},
};

const enhance = compose(
  listContainer,
  onClickOutside
);

export { AutoComplete };
export default enhance(AutoComplete);
