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
} from 'lodash';
import { compose } from 'recompose';
import listContainer from '../listContainer';
import onClickOutside from 'react-onclickoutside';
import cn from 'classnames';

import InputContent from '../InputSelect/InputContent';

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
      activeValueId: null,
    };

    this._input = null;
    this._textarea = null;

    this.setInputRef = this.setInputRef.bind(this);
    this._setIsExpanded = this._setIsExpanded.bind(this);
    this._handleClick = this._handleClick.bind(this);
    this.calcPopperWidth = this.calcPopperWidth.bind(this);
    this.setSelectedListRef = this.setSelectedListRef.bind(this);
    this.setTextareaRef = this.setTextareaRef.bind(this);
    this.onFocus = this.onFocus.bind(this);
    this.onClick = this.onClick.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onSelect = this.onSelect.bind(this);
    this.onBlur = this.onBlur.bind(this);
    this._setActiveValueId = this._setActiveValueId.bind(this);
    this._handleDataSearch = this._handleDataSearch.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { value } = this.props;

    if (prevProps.value !== value) {
      this.setState({ value: [value] });
    }
  }

  handleClickOutside() {
    const { onBlur } = this.props;
    const { isExpanded } = this.state;
    if (isExpanded) {
      this._setIsExpanded(false);
      onBlur();
    }
  }

  calcPopperWidth() {
    const { _input, _textarea } = this;
    const { popupAutoSize } = this.props;
    if ((_input || _textarea) && !popupAutoSize) {
      return _input
        ? _input.getBoundingClientRect().width
        : _textarea.getBoundingClientRect().width;
    }
  }

  _setIsExpanded(isExpanded) {
    const { disabled, onToggle, onClose, onOpen } = this.props;
    const { isExpanded: previousIsExpanded } = this.state;
    if (!disabled && isExpanded !== previousIsExpanded) {
      this.setState({ isExpanded });
      onToggle(isExpanded);
      isExpanded ? onOpen() : onClose();
    }
  }

  setInputRef(popperRef) {
    return r => {
      this._input = r;
      popperRef(r);
    };
  }

  setTextareaRef(poperRef) {
    return r => {
      this._textarea = r;
      poperRef(r);
    };
  }

  setSelectedListRef(selectedList) {
    this._selectedList = selectedList;
  }

  _handleClick() {
    this._setIsExpanded(true);
  }

  onFocus() {
    const { openOnFocus } = this.props;

    if (openOnFocus) {
      this._setIsExpanded(true);
    }
  }

  onClick() {
    this._setIsExpanded(true);
  }

  _handleDataSearch(input, delay = 400, callback) {
    const { onSearch, filter, valueFieldId, options } = this.props;

    if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter)) {
      const filterFunc = item => String.prototype[filter].call(item, input);
      const filteredData = options.filter(item =>
        filterFunc(item[valueFieldId])
      );
      this.setState({ options: valueFieldId });
    } else {
      //серверная фильтрация
      const labels = this.state.value.map(item => item[valueFieldId]);
      if (labels.some(label => label === input)) {
        onSearch('', delay, callback);
      } else {
        onSearch(input, delay, callback);
      }
    }
  }

  onChange(value) {
    const { onInput } = this.props;
    const onSetNewInputValue = value => {
      onInput(value);
      this._handleDataSearch(value);
    };

    if (!isEqual(this.state.value, value)) {
      this.setState({ value: [value] }, () => onSetNewInputValue(value));
    }
  }

  onBlur() {
    const { onBlur } = this.props;

    if (isFunction(onBlur)) {
      onBlur();
    }
  }

  onSelect(item) {
    const { valueFieldId, onChange, closePopupOnSelect } = this.props;
    const value = get(item, valueFieldId);

    this.setState(
      {
        value: [value],
      },
      () => {
        if (closePopupOnSelect) {
          this._setIsExpanded(false);
        }

        onChange(value);
      }
    );
  }

  _setActiveValueId(activeValueId) {
    this.setState({ activeValueId });
  }

  render() {
    const { isExpanded, value, activeValueId } = this.state;
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
    } = this.props;
    const needAddFilter = !find(
      this.state.value,
      item => item[valueFieldId] === this.state.input
    );
    const filteredOptions = filter(
      !isEmpty(data) ? data : options,
      item => includes(item[valueFieldId], value) || isEmpty(value)
    );

    return (
      <div className={cn('n2o-autocomplete w-100', className)} style={style}>
        <Manager>
          <Reference>
            {({ ref }) => (
              <InputContent
                options={filteredOptions}
                setRef={this.setInputRef(ref)}
                onInputChange={this.onChange}
                setActiveValueId={this._setActiveValueId}
                closePopUp={() => this._setIsExpanded(false)}
                openPopUp={() => this._setIsExpanded(true)}
                selected={value}
                value={value}
                onFocus={this.onFocus}
                onClick={this.onClick}
                onBlur={this.onBlur}
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
            )}
          </Reference>
          {isExpanded && (
            <Popper
              placement="bottom-start"
              modifiers={MODIFIERS}
              positionFixed={true}
            >
              {({ ref, style, placement }) => (
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
   * Варианты фильтрации
   */
  filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', false]),
  /**
   * Значение
   */
  value: PropTypes.oneOfType([PropTypes.array, PropTypes.object]),
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
