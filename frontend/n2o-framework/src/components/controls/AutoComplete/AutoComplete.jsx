import React from 'react';
import PropTypes from 'prop-types';
import find from 'lodash/find';
import isEmpty from 'lodash/isEmpty';
import isFunction from 'lodash/isFunction';
import get from 'lodash/get';
import filter from 'lodash/filter';
import includes from 'lodash/includes';
import isEqual from 'lodash/isEqual';
import map from 'lodash/map';
import isArray from 'lodash/isArray';
import isString from 'lodash/isString';
import isNil from 'lodash/isNil';
import some from 'lodash/some';
import pick from 'lodash/pick';
import { compose, mapProps } from 'recompose';
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
    this._onButtonClick = this._onButtonClick.bind(this);
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

  componentDidUpdate = (prevProps, prevState) => {
    const { value, options } = this.props;
    const compareListProps = ['options', 'value'];
    const compareListState = ['input'];
    if (
      !isEqual(
        pick(prevProps, compareListProps),
        pick(this.props, compareListProps)
      ) ||
      !isEqual(
        pick(prevState, compareListState),
        pick(this.state, compareListState)
      )
    ) {
      const state = {};

      if (!isEqual(prevProps.options, options)) {
        state.options = options;
      }

      if (prevProps.value !== value) {
        state.value = isArray(value) ? value : value ? [] : [];
      }
      if (!isEmpty(state)) this.setState(state);
    }
  };

  handleClickOutside = () => {
    const { isExpanded } = this.state;
    if (isExpanded) {
      this._setIsExpanded(false);
      this.onBlur();
    }
  };

  calcPopperWidth = () => {
    const { _input } = this;
    const { popupAutoSize } = this.props;
    if (_input && !popupAutoSize) {
      return _input.getBoundingClientRect().width
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
    const { onInput, tags, options, data, valueFieldId, onChange } = this.props;
    const onSetNewInputValue = input => {
      onInput(input);
      if (!tags && input === '') {
        onChange([]);
      } else if (!tags) {
        onChange([input]);
      }
      this._handleDataSearch(input);
    };

    if (!isEqual(this.state.input, input)) {
      const getSelected = prevState =>
        tags
          ? prevState.value
          : some(options || data, option => option[valueFieldId] === input)
          ? [input]
          : [input];
      this.setState(
        prevState => ({
          input,
          value: getSelected(prevState),
          isExpanded: true,
        }),
        () => onSetNewInputValue(input)
      );
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
        if (tags) {
          onChange(this.state.value);
        } else {
          onChange(this.state.input);
        }
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
      onChange(newValue);
      this.forceUpdate();
    });
  };
  _onButtonClick() {
    if (this._input) {
      this._input.focus();
    }
  }

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
      maxTagTextLength,
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
                className={`${className} ${isExpanded ? 'focus' : ''}`}
                setSelectedItemsRef={this.setSelectedItemsRef}
                input={input}
                onClearClick={this._handleElementClear}
                onButtonClick={this._onButtonClick}
              >
                <InputContent
                  tags={true}
                  mode="autocomplete"
                  maxTagTextLength={maxTagTextLength}
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
                  onRemoveItem={this._removeSelectedItem}
                  isExpanded={isExpanded}
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
                />
              </InputSelectGroup>
            )}
          </Reference>
          {isExpanded && !isEmpty(filteredOptions) && (
            <Popper
              placement="bottom-start"
              modifiers={MODIFIERS}
              strategy="fixed"
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
                    renderIfEmpty={false}
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
  /**
   * Максимальная длина текста в тэге, до усечения
   */
  maxTagTextLength: PropTypes.number,
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
  mapProps(props => ({
    ...props,
    options: !isEmpty(props.data) ? props.data : props.options,
  })),
  onClickOutside
);

export { AutoComplete };
export default enhance(AutoComplete);
