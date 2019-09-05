import React from 'react';
import PropTypes from 'prop-types';
import { find, isEqual, isFunction, get, filter, includes } from 'lodash';
import { compose } from 'recompose';
import listContainer from '../listContainer';
import onClickOutside from 'react-onclickoutside';

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
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
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
    this.setState({ isExpanded });
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

  onChange(value) {
    this.setState({ value: [value] });
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
      labelFieldId,
      iconFieldId,
      disabled,
      placeholder,
      multiSelect,
      disabledValues,
      imageFieldId,
      groupFieldId,
      hasCheckboxes,
      format,
      badgeFieldId,
      badgeColorFieldId,
      onScrollEnd,
      expandPopUp,
      style,
      alerts,
      flip,
      autoFocus,
      options,
    } = this.props;
    const needAddFilter = !find(
      this.state.value,
      item => item[labelFieldId] === this.state.input
    );

    const filteredOptions = filter(options, item =>
      includes(item[labelFieldId], value)
    );

    return (
      <div>
        <Manager>
          <Reference>
            {({ ref }) => (
              <div style={{ position: 'relative' }}>
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
                  labelFieldId={labelFieldId}
                  autoFocus={autoFocus}
                  _textarea={this._textarea}
                  _selectedList={this._selectedList}
                />
              </div>
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
                    labelFieldId={labelFieldId}
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
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  onChange: PropTypes.func,
  closePopupOnSelect: PropTypes.bool,
};

AutoComplete.defaultProps = {
  onFocus: () => {},
  onBlur: () => {},
  onChange: () => {},
  closePopupOnSelect: true,
};

const enhance = compose(
  listContainer,
  onClickOutside
);

export { AutoComplete };
export default enhance(AutoComplete);
